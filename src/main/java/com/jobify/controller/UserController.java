package com.jobify.controller;

import com.jobify.entity.User;
import com.jobify.payload.request.LoginDTO;
import com.jobify.payload.request.UserDTO;
import com.jobify.payload.response.GetAllUsersWithPagination;
import com.jobify.payload.response.JwtAuthResponse;
import com.jobify.payload.response.UserApiResponse;
import com.jobify.security.CustomUserDetailsService;
import com.jobify.security.TokenHelper;
import com.jobify.service.UserService;
import com.jobify.service.export.UserCsvExporter;
import com.jobify.service.export.UserExcelExporter;
import com.jobify.service.export.UserPdfExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
@Tag(name = "Jobify User Service", description = "to perform all CRUD operations")
public class UserController
{
    private static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenHelper tokenHelper;

    private final ModelMapper modelMapper;

    /**
     * @param userService
     * @param authenticationManager
     * @param customUserDetailsService
     * @param tokenHelper
     * @param modelMapper
     */
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          CustomUserDetailsService customUserDetailsService, TokenHelper tokenHelper,
                          ModelMapper modelMapper)
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenHelper = tokenHelper;
        this.modelMapper = modelMapper;
    }

    /**
     * @param userDTO
     * @return
     */
    @Operation(summary = "Register a new user", description = "A POST request to register users", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created the user"),
        @ApiResponse(responseCode = "400", description = "Input Validation Failed"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred"),
        @ApiResponse(responseCode = "409", description = "User Already Exists")})
    @PostMapping("/users/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO)
    {
        HttpStatus httpStatus = checkEmailDuplicate(userDTO).getStatusCode();

        if (httpStatus.is2xxSuccessful())
        {
            UserDTO registeredUser = userService.registerUser(userDTO);

            LOGGER.info("{}", "User registered successfully!");

            UserApiResponse userApiResponse = this.modelMapper.map(registeredUser, UserApiResponse.class);

            return new ResponseEntity<>(userApiResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User already exists with email: " + userDTO.getEmailID(), HttpStatus.valueOf(httpStatus.value()));
    }

    /**
     * @param loginDTO
     * @return
     */
    @Operation(summary = "User login", description = "A POST request for logging-in a user", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully logged-in the user"),
        @ApiResponse(responseCode = "403", description = "Log-in unsuccessful"),
        @ApiResponse(responseCode = "400", description = "Input Validation Failed"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @PostMapping("/users/login")
    public ResponseEntity<JwtAuthResponse> userLogin(@Valid @RequestBody LoginDTO loginDTO)
    {
        Authentication authentication;
        try
        {
            authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmailID(), loginDTO.getPassword()));

        } catch (BadCredentialsException e)
        {
            throw new BadCredentialsException(e.getLocalizedMessage());
        }

        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(loginDTO.getEmailID());

        String token = this.tokenHelper.generateToken(userDetails);

        User user = (User) authentication.getPrincipal();

        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

        JwtAuthResponse response = JwtAuthResponse.builder()
                                                  .token(token)
                                                  .emailID(userDTO.getEmailID())
                                                  .location(userDTO.getLocation())
                                                  .age(userDTO.getAge())
                                                  .userId(userDTO.getUserID())
                                                  .firstName(userDTO.getFirstName())
                                                  .lastName(userDTO.getLastName())
                                                  .fullName(userDTO.getFirstName() + " " + userDTO.getLastName())
                                                  .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @return
     */
    @Operation(summary = "Get all users", description = "A GET request to get all users", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found all the users"),
        @ApiResponse(responseCode = "404", description = "No Users Found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/all")
    public ResponseEntity<GetAllUsersWithPagination> getAllUsers(@RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                                 @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                                                 @RequestParam(required = false, defaultValue = "userId", value = "sortBy") String sortByUserId,
                                                                 @RequestParam(required = false, defaultValue = "location", value = "sortBy") String sortByLocation,
                                                                 @RequestParam(required = false, defaultValue = "emailID", value = "sortBy") String sortByUsername,
                                                                 @RequestParam(required = false, defaultValue = "asc", value = "sortDir") String sortDir)
    {
        List<UserDTO> userDTO = this.userService.getAllUsers(pageNumber, pageSize, sortByUserId, sortByLocation, sortByUsername, sortDir);

        List<UserApiResponse> apiResponse = userDTO.stream()
                                                   .map(u -> this.modelMapper.map(u, UserApiResponse.class))
                                                   .collect(Collectors.toList());

        GetAllUsersWithPagination getAllUsersWithPagination = new GetAllUsersWithPagination();

        getAllUsersWithPagination.setUsers(apiResponse);
        getAllUsersWithPagination.setNumberOfUsers((long) apiResponse.size());

        List<GetAllUsersWithPagination> users = userDTO.stream().map(j ->
        {
            getAllUsersWithPagination.setNumberOfPages(j.getNumberOfPages());
            getAllUsersWithPagination.setTotalNumberOfUsers(j.getTotalNumberOfUsers());
            return getAllUsersWithPagination;
        }).toList();

        return new ResponseEntity<>(users.get(0), HttpStatus.OK);
    }

    /**
     *
     * @param searchVal
     * @return
     */
    @Operation(summary = "Search user by keyword", description = "A GET request to search users", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/search")
    public ResponseEntity<Map<String, List<UserApiResponse>>> searchUserBy(@RequestParam(value = "search", required = false) String searchVal)
    {
        List<UserDTO> userDTO = this.userService.searchByAllFields(searchVal);

        List<UserApiResponse> userApiResponses = userDTO.stream().map(j -> this.modelMapper.map(j, UserApiResponse.class)).collect(Collectors.toList());

        Map<String, List<UserApiResponse>> responseMap = new HashMap<>();

        responseMap.put("usersOnSearch", userApiResponses);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    /**
     * @param emailID
     * @return
     */
    @Operation(summary = "Get user by username", description = "A GET request to get user by username", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/get/{username}")
    public ResponseEntity<UserApiResponse> getUserByUsername(@PathVariable(value = "username") String emailID)
    {
        UserDTO userDTO = this.userService.getUserByUsername(emailID);

        UserApiResponse userApiResponse = this.modelMapper.map(userDTO, UserApiResponse.class);

        return new ResponseEntity<>(userApiResponse, HttpStatus.OK);
    }

    /**
     * @param userId
     * @return
     */
    @Operation(summary = "Get user by userId", description = "A GET request to get user by userId", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/get/user/{userId}")
    public ResponseEntity<UserApiResponse> getUserByUserId(@PathVariable(value = "userId") Integer userId)
    {
        UserDTO userDTO = this.userService.getUserByUserId(userId);

        UserApiResponse userApiResponse = this.modelMapper.map(userDTO, UserApiResponse.class);

        return new ResponseEntity<>(userApiResponse, HttpStatus.OK);
    }

    /**
     * @param userDTO
     * @param id
     * @return
     */
    @Operation(summary = "Update user by userId", description = "A PUT request to update user", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated user"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @PutMapping("/users/update/{userId}")
    public ResponseEntity<UserApiResponse> updateUserByUserId(@Valid @RequestBody UserDTO userDTO, @PathVariable("userId") Integer id)
    {
        UserDTO updatedUser = this.userService.updatedUserByUserId(userDTO, id);

        LOGGER.info("{}", "User updated successfully with ID: " + id);

        UserApiResponse userApiResponse = this.modelMapper.map(updatedUser, UserApiResponse.class);

        return new ResponseEntity<>(userApiResponse, HttpStatus.OK);

    }

    /**
     * @param response
     * @throws IOException
     */
    @Operation(summary = "Export user's data in Csv", description = "A GET request to download user's list in a Csv file", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found all the users"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException
    {
        List<UserDTO> listUsers = this.userService.listAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }

    /**
     * @param response
     * @throws IOException
     */
    @Operation(summary = "Export user's data in Excel", description = "A GET request to download user's list in a Excel file", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found all the users"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException
    {
        List<UserDTO> listUsers = this.userService.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    /**
     * @param response
     * @throws IOException
     */
    @Operation(summary = "Export user's data in Pdf", description = "A GET request to download user's list in a Pdf file", tags = {"Jobify User Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found all the users"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException
    {
        List<UserDTO> listUsers = this.userService.listAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }

    /**
     * @param userDTO
     * @return
     */
    private ResponseEntity<String> checkEmailDuplicate(UserDTO userDTO)
    {
        if (!isNull(this.userService.getUserByUsername(userDTO.getEmailID())))
        {
            return new ResponseEntity<>(" ", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(" ", HttpStatus.OK);
    }
}
