package com.substring.auth.service;

import com.substring.auth.dto.UserDto;

public interface IAuthService {

    UserDto registerUser(UserDto userDto);
}
