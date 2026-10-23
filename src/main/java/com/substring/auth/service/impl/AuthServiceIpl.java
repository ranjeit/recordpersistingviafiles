package com.substring.auth.service.impl;

import com.substring.auth.dto.UserDto;
import com.substring.auth.service.IAuthService;
import com.substring.auth.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceIpl implements IAuthService {
    private final IUserService userService;

    public AuthServiceIpl(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDto registerUser(UserDto userDto) {
        // logic
        // verify email
        // verify password
        // default roles 
        return userService.createUser(userDto);
    }
}
