package com.jobify.service.impl;

import com.jobify.entity.User;
import com.jobify.payload.request.UserDTO;
import com.jobify.payload.response.SearchCriteria;
import com.jobify.payload.response.SearchSpecificationForUsers;
import com.jobify.repository.UserRepo;
import com.jobify.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param userDTO
     * @return
     */
    @Override
    public UserDTO registerUser(UserDTO userDTO)
    {
        User user = this.modelMapper.map(userDTO, User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepo.save(user);
        UserDTO userToDTO = this.modelMapper.map(savedUser, UserDTO.class);

        return userToDTO;
    }

    /**
     * @return
     */
    @Override
    public List<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortByUserId, String sortByLocation, String sortByUsername, String sortDir)
    {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ?
                    Sort.by(sortByUserId, sortByLocation, sortByUsername).ascending() :
                    Sort.by(sortByUserId, sortByLocation, sortByUsername).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        List<User> users = this.userRepo.findAll(pageable).getContent();

        if (users.isEmpty())
        {
            throw new UsernameNotFoundException("No users exists!");
        }

        Long numOfPages = (long) this.userRepo.findAll(pageable).getTotalPages();

        Long totalNumberOfUsers = this.userRepo.findAll(pageable).getTotalElements();

        List<UserDTO> userDTOs = users.stream()
                                      .map(u -> this.modelMapper.map(u, UserDTO.class))
                                      .collect(Collectors.toList());

        userDTOs = userDTOs.stream().peek(u ->
        {
            u.setNumberOfPages(numOfPages);
            u.setTotalNumberOfUsers(totalNumberOfUsers);
        }).toList();

        return userDTOs;
    }

    @Override
    public List<UserDTO> searchByAllFields(String searchVal)
    {
        SearchSpecificationForUsers spec1 =
            new SearchSpecificationForUsers(new SearchCriteria("location", ":", searchVal));

        SearchSpecificationForUsers spec2 =
            new SearchSpecificationForUsers(new SearchCriteria("emailID", ":", searchVal));

        SearchSpecificationForUsers spec3 =
            new SearchSpecificationForUsers(new SearchCriteria("firstName", ":", searchVal));

        SearchSpecificationForUsers spec4 =
            new SearchSpecificationForUsers(new SearchCriteria("lastName", ":", searchVal));

        List<User> results =
            userRepo.findAll(Specification.where(spec1).or(spec2).or(spec3).or(spec4));

        return results.stream().map(j -> this.modelMapper.map(j, UserDTO.class)).toList();
    }

    @Override
    public UserDTO updatedUserByUserId(UserDTO userDTO, Integer userId)
    {
        User userInDB = this.userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        userInDB.setFirstName(userDTO.getFirstName());
        userInDB.setLastName(userDTO.getLastName());
        userInDB.setLocation(userDTO.getLocation());
        userInDB.setEmailID(userDTO.getEmailID());
        userInDB.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        userInDB.setAge(userDTO.getAge());

        User saveUser = this.userRepo.save(userInDB);

        UserDTO updatedUser = this.modelMapper.map(saveUser, UserDTO.class);

        return updatedUser;
    }

    /**
     * @param emailID
     * @return
     */
    @Override
    public UserDTO getUserByUsername(String emailID)
    {
        Optional<User> user = this.userRepo.findByEmailID(emailID);

        return user.map(value -> this.modelMapper.map(value, UserDTO.class)).orElse(null);

    }

    @Override
    public UserDTO getUserByUserId(Integer userId)
    {
        Optional<User> user = this.userRepo.findById(userId);

        if (user.isEmpty())
        {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> listAll()
    {
        List<User> users = this.userRepo.findAll();

        List<UserDTO> userDTOs = users.stream()
                                      .map(u -> this.modelMapper.map(u, UserDTO.class))
                                      .collect(Collectors.toList());

        return userDTOs;
    }
}
