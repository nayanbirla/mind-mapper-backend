package com.mindmapper.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindmapper.security.dto.request.LoginRequest;
import com.mindmapper.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!request.getServletPath().equals("/generate-token")){
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);



        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authenticate =authenticationManager.authenticate(authenticationToken);

        if(authenticate.isAuthenticated()){

            String token = jwtUtil.generateToken(authenticate.getName(),1);
            response.setHeader("authorization","Bearer "+token);

            String refreshToken = jwtUtil.generateToken(authenticate.getName(),60*24*7);

            String cookieHeader = String.format(
                    "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax",
                    refreshToken, 7 * 24 * 60 * 60
            );

            response.setHeader("Set-Cookie", cookieHeader);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/") ||
                path.startsWith("/security/user/register") ||
                path.startsWith("/public/") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/login/") ||
                path.startsWith("/course-images/") ||
                path.startsWith("/refresh-token");
    }
}
