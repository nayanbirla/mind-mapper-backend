package com.mindmapper.security.service;

import com.mindmapper.entity.UserInfo;
import com.mindmapper.security.dto.request.RegisterUserRequest;
import com.mindmapper.security.dto.response.UserProfile;
import com.mindmapper.utility.Response;

public interface IUserService {

    Response registerUser(RegisterUserRequest registerUserRequest);

    UserInfo loadOrCreateUser(String email);

    UserProfile getUserProfileById(Long profileId);
}
