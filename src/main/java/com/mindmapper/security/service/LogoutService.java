package com.mindmapper.security.service;

import com.mindmapper.security.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LogoutService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    JwtUtil jwtUtil;

    public String logout(HttpServletRequest request, HttpServletResponse response){

        String accessToken = request.getHeader("Authorization");
        String refreshToken = extractRefreshToken(request);

        //Taking time how long this token will be alive
        long ttl_access_token = jwtUtil.getRemainingTimeInSeconds(accessToken);
        redisTemplate.opsForValue().set(accessToken,"Blocked Access token", Duration.ofSeconds(ttl_access_token));

        long ttl_refresh_token = jwtUtil.getRemainingTimeInSeconds(refreshToken);
        redisTemplate.opsForValue().set(refreshToken,"Blocked Refresh token", Duration.ofSeconds(ttl_refresh_token));

        String cookieHeader = String.format(
                "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax",
                null, null
        );

        response.setHeader("Set-Cookie", cookieHeader);

        return "Logged out successfully";
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // or throw exception if not found
    }
}
