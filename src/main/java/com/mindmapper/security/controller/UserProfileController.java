package com.mindmapper.security.controller;

import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.security.service.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    @Autowired
    IUserProfileService iUserProfileService;

    @GetMapping("/profile/{profileId}")
    ResponseEntity<UserProfile> getUserProfile(Long profileId){
        // Call the service to get user profile by ID
        UserProfile userProfile=iUserProfileService.getUserProfileById(profileId);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
