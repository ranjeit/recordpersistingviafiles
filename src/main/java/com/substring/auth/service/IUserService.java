package com.substring.auth.service;

import com.substring.auth.dto.UserDto;
import com.substring.auth.exceptions.ResourceNotFoundException;

public interface IUserService {

    // create user
    UserDto createUser(UserDto userDto);

    // get user y email
    UserDto getUserByEmail(String email) throws ResourceNotFoundException;

    // update user
    UserDto updateUser(UserDto userDto, String userId) throws ResourceNotFoundException;

    //delete user
    String deleteUser(String userId) throws ResourceNotFoundException;

    //get user by id
    UserDto getUserById(String userId) throws ResourceNotFoundException;

    //get all users
    Iterable<UserDto> getAllUsers();
}
