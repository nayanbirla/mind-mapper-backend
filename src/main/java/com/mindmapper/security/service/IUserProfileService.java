package com.mindmapper.security.service;

import com.mindmapper.security.dto.response.UserProfile;

public interface IUserProfileService {

    UserProfile getUserProfileById(Long profileId);
}
