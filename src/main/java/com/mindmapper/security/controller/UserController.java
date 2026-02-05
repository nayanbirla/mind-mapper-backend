package com.mindmapper.security.controller;

import com.mindmapper.security.dto.request.RegisterUserRequest;
import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.security.service.LogoutService;
import com.mindmapper.utility.Response;
import com.mindmapper.security.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private LogoutService logoutService;

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

    @GetMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        // Call the service to get user profile by ID
        String message= logoutService.logout(request,response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/forget-password/{email}")
    ResponseEntity<String> forgetPassword(String email) throws MessagingException {
        // Call the service to get user profile by ID
        String message = userService.forgetPassword(email);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}
