package com.jobify.service;

import com.jobify.payload.request.UserDTO;

import java.util.List;

public interface UserService
{
    UserDTO registerUser(UserDTO userDTO);

    List<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortByUserId, String sortByLocation, String sortByUsername, String sortDir);

    List<UserDTO> searchByAllFields(String searchVal);

    UserDTO updatedUserByUserId(UserDTO userDTO, Integer userId);

    UserDTO getUserByUsername(String emailID);

    UserDTO getUserByUserId(Integer userId);

    List<UserDTO> listAll();
}
