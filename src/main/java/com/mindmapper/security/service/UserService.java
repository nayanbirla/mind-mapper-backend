package com.mindmapper.security.service;

import com.mindmapper.security.dto.request.RegisterUserRequest;
import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.utility.Response;
import com.mindmapper.entity.Role;
import com.mindmapper.entity.UserInfo;
import com.mindmapper.entity.embeddable.Name;
import com.mindmapper.repository.RoleRepository;
import com.mindmapper.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JavaMailSender mailSender;

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
    public String forgetPassword(String email) throws MessagingException {

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required to reset password");
        }

        Optional<UserInfo> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isEmpty()) {
            // Logic to send OTP to the user's email
            // For example, generate OTP and send email
            String otp = generateOTP();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Mind Mapper Password Reset OTP");
            helper.setText("Your OTP for password reset is: " + otp);
            mailSender.send(message);

        }
        return "If email exist OTP sent successfully";
    }

    private String generateOTP() {

        // Generate a random 6-digit OTP
        int otp = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }


}
