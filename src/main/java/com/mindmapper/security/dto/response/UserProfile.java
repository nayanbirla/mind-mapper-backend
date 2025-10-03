package com.mindmapper.security.dto.response;

import lombok.Data;

@Data
public class UserProfile {

    private Long profileId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String profileUrl;

}
