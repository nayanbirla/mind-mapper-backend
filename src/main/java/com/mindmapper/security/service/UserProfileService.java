package com.mindmapper.security.service;

import com.mindmapper.entity.UserInfo;
import com.mindmapper.repository.UserRepository;
import com.mindmapper.security.dto.response.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserProfileService implements IUserProfileService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserProfile getUserProfileById(Long profileId) {

        Optional<UserInfo> userOptional = userRepository.findById(profileId);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found for ID: " + profileId);
        }
        UserInfo userInfo = userOptional.get();
        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId(userInfo.getUserId());
        userProfile.setFirstName(userInfo.getName().getFirstName());
        userProfile.setMiddleName(userInfo.getName().getMiddleName());
        userProfile.setLastName(userInfo.getName().getLastName());
        userProfile.setEmail(userInfo.getEmail());

        return userProfile;
    }
}
