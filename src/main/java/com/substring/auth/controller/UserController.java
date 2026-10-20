package com.substring.auth.controller;

import com.substring.auth.dto.UserDto;
import com.substring.auth.exceptions.ResourceNotFoundException;
import com.substring.auth.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;

    // create user api
    @PostMapping
    public ResponseEntity<UserDto> createUsers(@RequestBody UserDto userDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userDto));
    }
        // get single user by Id

         @GetMapping("/{userId}")
        public ResponseEntity<UserDto> getSingleUserById(@PathVariable String userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
        }

    //get all users api
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    // get single user by email id
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmailId(@PathVariable String email) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    // update user
    @PutMapping(
            value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUser(userDto,userId));

    }
    // delete api
    // api/v1/users/{userId}
    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable("userId") String userId) throws ResourceNotFoundException {
        return userService.deleteUser(userId);
    }
}
