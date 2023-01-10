package com.jobify.controller;

import com.jobify.entity.User;
import com.jobify.payload.request.JobDTO;
import com.jobify.payload.response.*;
import com.jobify.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Jobify Job Service", description = "to perform all CRUD operations")
public class JobController
{
    @Autowired
    private JobService jobService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param jobDTO
     * @param userId
     * @return
     */
    @Operation(summary = "Create job", description = "A POST request to create job by userId", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created the job"),
        @ApiResponse(responseCode = "400", description = "Input Validation Failed"),
        @ApiResponse(responseCode = "401", description = "UnAuthorized"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @PostMapping("/jobs/create/{userId}")
    public ResponseEntity<JobApiResponse> createJob(@RequestBody @Valid JobDTO jobDTO, @PathVariable Integer userId) throws Exception
    {
        JobDTO jobCreate = this.jobService.createJob(jobDTO, userId);

        JobApiResponse jobApiResponse = this.modelMapper.map(jobCreate, JobApiResponse.class);

        return new ResponseEntity<>(jobApiResponse, HttpStatus.CREATED);
    }

    /**
     * @param jobId
     * @return
     */
    @Operation(summary = "Get job by jobId", description = "A GET request to get job by jobId", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/get/{id}")
    public ResponseEntity<JobApiResponse> getJobById(@PathVariable(value = "id") Integer jobId)
    {
        JobDTO jobDTO = this.jobService.getJobById(jobId);

        JobApiResponse jobApiResponse = this.modelMapper.map(jobDTO, JobApiResponse.class);

        return new ResponseEntity<>(jobApiResponse, HttpStatus.OK);
    }

    /**
     * @param searchVal
     * @return
     */
    @Operation(summary = "Search job by keyword", description = "A GET request to search jobs", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/search")
    public ResponseEntity<Map<String, List<JobApiResponse>>> searchJobBy(@RequestParam(value = "search", required = false) String searchVal)
    {
        List<JobDTO> jobDTO = this.jobService.searchByAllFields(searchVal);

        List<JobApiResponse> jobApiResponse = jobDTO.stream()
                                                    .map(j -> this.modelMapper.map(j, JobApiResponse.class))
                                                    .collect(Collectors.toList());

        Map<String, List<JobApiResponse>> responseMap = new HashMap<>();

        responseMap.put("jobsOnSearch", jobApiResponse);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortByJobId
     * @param sortByCompany
     * @param sortByPosition
     * @param sortByJobLocation
     * @param sortDir
     * @return
     */
    @Operation(summary = "Get list of jobs", description = "A GET request to get all jobs", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found jobs"),
        @ApiResponse(responseCode = "404", description = "Jobs not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/get/all")
    public ResponseEntity<GetAllJobsWithPagination> getJobs(@RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
                                                            @RequestParam(required = false, defaultValue = "jobId", value = "sortBy") String sortByJobId,
                                                            @RequestParam(required = false, defaultValue = "company", value = "sortBy") String sortByCompany,
                                                            @RequestParam(required = false, defaultValue = "position", value = "sortBy") String sortByPosition,
                                                            @RequestParam(required = false, defaultValue = "jobLocation", value = "sortBy") String sortByJobLocation,
                                                            @RequestParam(required = false, defaultValue = "asc", value = "sortDir") String sortDir)
    {
        List<JobDTO> jobDTO = this.jobService.getAllJobs(pageNumber,
                                                         pageSize,
                                                         sortByJobId,
                                                         sortByCompany,
                                                         sortByPosition,
                                                         sortByJobLocation,
                                                         sortDir);

        List<JobApiResponse> list = jobDTO.stream()
                                          .map(j -> this.modelMapper.map(j, JobApiResponse.class))
                                          .toList();

        GetAllJobsWithPagination getAllJobsWithPagination = new GetAllJobsWithPagination();

        getAllJobsWithPagination.setJobs(list);
        getAllJobsWithPagination.setNumberOfJobs((long) list.size());

        List<GetAllJobsWithPagination> jobs = jobDTO.stream()
                                                    .map(j ->
                                                         {
                                                             getAllJobsWithPagination.setNumberOfPages(j.getNumberOfPages());
                                                             getAllJobsWithPagination.setTotalNumberOfJobs(j.getTotalNumberOfJobs());
                                                             return getAllJobsWithPagination;
                                                         })
                                                    .toList();

        return new ResponseEntity<>(jobs.get(0), HttpStatus.OK);
    }

    /**
     * @param userId
     * @return
     */
    @Operation(summary = "Get list of jobs by userId with stats", description = "A GET request to get jobs by userId with stats", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found jobs with stats"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/stats/user/{userId}")
    public ResponseEntity<Map<String, List<StatsResponse>>> getJobsByUserWithStats(@PathVariable Integer userId)
    {
        List<JobDTO> jobDTOs = this.jobService.getJobsByUserWithStats(userId);

        List<List<StatsResponse>>
            statsResponses =
            jobDTOs.stream()
                   .map(JobDTO::getStatsResponses)
                   .toList();

        Map<String, List<StatsResponse>> response = new HashMap<>();

        response.put("stats", statsResponses.get(0));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get list of jobs by userId with monthly apps", description = "A GET request to get jobs by userId with monthly apps", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found jobs with monthly apps"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/monthly-apps/user/{userId}")
    public ResponseEntity<Map<String, List<MonthlyAppResponse>>> getJobsByUserWithMonthlyApplications(@PathVariable Integer userId)
    {
        List<JobDTO> jobDTOs = this.jobService.getJobsByUserWithMonthlyStats(userId);

        List<List<MonthlyAppResponse>> monthlyAppResponseList = jobDTOs.stream()
                                                                       .map(JobDTO::getMonthlyAppResponseList)
                                                                       .toList();

        Map<String, List<MonthlyAppResponse>> response = new HashMap<>();

        response.put("monthlyApplications", monthlyAppResponseList.get(0));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @param jobDTO
     * @param jobId
     * @return
     */
    @Operation(summary = "Update job by jobId", description = "A PUT request to update job", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @PutMapping("/jobs/update/{jobId}")
    public ResponseEntity<JobApiResponse> updateJobByJobId(@Valid @RequestBody JobDTO jobDTO, @PathVariable Integer jobId) throws Exception
    {
        JobApiResponse apiResponse;
        try
        {
            JobDTO updatedJob = this.jobService.updateJob(jobDTO, jobId);

            apiResponse = this.modelMapper.map(updatedJob, JobApiResponse.class);
        } catch (Exception e)
        {
            throw new Exception(e.getLocalizedMessage());
        }

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * @param user
     * @return
     */
    @Operation(summary = "Get current loggedIn User", description = "A GET request to get current user", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User is loggedIn"),
        @ApiResponse(responseCode = "401", description = "User not loggedIn"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @GetMapping("/jobs/current/user")
    public ResponseEntity<Map<String, UserApiResponse>> currentUserName(@AuthenticationPrincipal User user)
    {
        UserApiResponse userApiResponse = this.modelMapper.map(user, UserApiResponse.class);

        Map<String, UserApiResponse> responseMap = new HashMap<>();

        responseMap.put("user", userApiResponse);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    /**
     * @param jobId
     * @return
     */
    @Operation(summary = "Delete job by jobId", description = "A DELETE request to delete job", tags = {"Jobify Job Service"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Some Exception Occurred")})
    @DeleteMapping("/jobs/delete/{jobId}")
    public ResponseEntity<?> deleteJobByJobId(@PathVariable Integer jobId)
    {
        JobDTO deletedJob = this.jobService.deleteJob(jobId);

        JobApiResponse jobApiResponse = this.modelMapper.map(deletedJob, JobApiResponse.class);

        return new ResponseEntity<>("Job deleted successfully with ID: " + jobApiResponse.getJobId(), HttpStatus.OK);
    }
}
