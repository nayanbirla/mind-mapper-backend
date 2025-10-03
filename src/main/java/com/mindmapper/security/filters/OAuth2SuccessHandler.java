package com.mindmapper.security.filters;

import com.mindmapper.entity.UserInfo;
import com.mindmapper.security.service.UserService;
import com.mindmapper.security.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Get the authorized client to access the access token
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "gitlab", authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        // Fetch user info from GitLab API
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> userAttributes = restTemplate.getForObject(
                "https://gitlab.com/api/v4/user?access_token=" + accessToken, Map.class
        );

        String email = (String) userAttributes.get("email");

         UserInfo userInfo = userService.loadOrCreateUser(email);

         if(userInfo!=null) {
             String token = jwtUtil.generateToken(userInfo.getEmail(), 1);
             response.sendRedirect("http://localhost:3000/login?token=" + token);
         }else{
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found or could not be created");
         }
    }
}
