package com.mindmapper.security.filters;

import com.mindmapper.security.dto.request.JWTAuthenticationToken;
import com.mindmapper.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTRefereshFilter extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationProvider;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/refresh-token")){
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractJwtFromRequest(request);

        if(refreshToken == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        JWTAuthenticationToken authenticationToken = new JWTAuthenticationToken(refreshToken);
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);
        if(authentication.isAuthenticated()) {
            String newToken = jwtUtil.generateToken(authentication.getName(), 1);
            response.setHeader("authorization", "Bearer " + newToken);
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if(cookies ==null){
            return null;
        }
        String refeshToken = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refreshToken")){
                refeshToken = cookie.getValue();
            }
        }
        return refeshToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/") ||
                path.startsWith("/security/user/register") ||
                path.startsWith("/public/") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/login/") ||
                path.equals("/generate-token") ||
                path.startsWith("/course-images/");
    }
}
