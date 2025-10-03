package com.mindmapper.security.controller;

import com.mindmapper.security.dto.request.RegisterUserRequest;
import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.utility.Response;
import com.mindmapper.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;
    //Register a new user
    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        // Call the service to register the user
        registerUserRequest.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        Response response=userService.registerUser(registerUserRequest);
        return new ResponseEntity<>(response.getMessage(), HttpStatus.CREATED);
    }

    @GetMapping("/profile/{profileId}")
    ResponseEntity<UserProfile> getUserProfile(Long profileId){
        // Call the service to get user profile by ID
        UserProfile userProfile=userService.getUserProfileById(profileId);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
