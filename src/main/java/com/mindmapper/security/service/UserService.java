package com.mindmapper.security.service;

import com.mindmapper.security.dto.request.RegisterUserRequest;
import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.security.utils.UserRole;
import com.mindmapper.utility.Response;
import com.mindmapper.entity.Role;
import com.mindmapper.entity.UserInfo;
import com.mindmapper.entity.embeddable.Name;
import com.mindmapper.repository.RoleRepository;
import com.mindmapper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Response registerUser(RegisterUserRequest registerUserRequest){
        //validate the request
        validateUserRequest(registerUserRequest);

        UserInfo userInfo = new UserInfo();

        Name name = new Name();
        name.setFirstName(registerUserRequest.getFirstName());
        name.setMiddleName(registerUserRequest.getMiddleName());
        name.setLastName(registerUserRequest.getLastName());

        userInfo.setName(name);
        userInfo.setEmail(registerUserRequest.getEmail());
        userInfo.setPassword(registerUserRequest.getPassword());
        // Assuming role is an integer representing the role ID

        Role role = new Role();
        role.setRoleId(Long.valueOf(2));

        userInfo.setUserRole(Arrays.asList(role));
        // Save the user information to the repository
        userRepository.save(userInfo);
        return new Response("User registered successfully", "201");

    }

    private void validateUserRequest(RegisterUserRequest registerUserRequest) {

        if (registerUserRequest.getEmail() == null || registerUserRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (registerUserRequest.getPassword() == null || registerUserRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserInfo loadOrCreateUser(String email) {

        if(email == null || email.isBlank()) {
            // Option 1: throw exception
            throw new IllegalArgumentException("Email is required to create user");

            // Option 2: generate a placeholder
            // email = "user_" + UUID.randomUUID() + "@gitlab.com";
        }
        Optional<UserInfo> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(email);
            Role role = new Role();
            role.setRoleId(Long.valueOf(2));
            userInfo.setUserRole(Arrays.asList(role));
            return userRepository.save(userInfo);
        }
        return userOptional.get();
    }

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
