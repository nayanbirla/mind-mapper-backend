package com.mindmapper.security.filters;

import com.mindmapper.security.dto.request.JWTAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTValidationFilter extends OncePerRequestFilter {

    public final AuthenticationManager authenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractJwtFromRequest(request);

        try {
            if (token != null) {
                JWTAuthenticationToken authenticationToken = new JWTAuthenticationToken(token);
                Authentication authResult = authenticationManager.authenticate(authenticationToken);

                if (authResult.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    filterChain.doFilter(request, response);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
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
