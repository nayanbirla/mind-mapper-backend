package com.mindmapper.utility;

import com.mindmapper.entity.UserInfo;

public class TokenExtractor {

    public static UserInfo getUserFromToken(String token){
        // This method should contain the logic to extract user information from the token.
        // For now, we will return a dummy UserInfo object.

        UserInfo user = new UserInfo();
        user.setUserId(1L); // Dummy ID

        return user;
    }
}
