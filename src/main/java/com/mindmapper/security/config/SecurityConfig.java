package com.mindmapper.security.config;

import com.mindmapper.security.authenticationProvider.JWTAuthenticationProvider;
import com.mindmapper.security.filters.JWTAuthenticationFilter;
import com.mindmapper.security.filters.JWTRefereshFilter;
import com.mindmapper.security.filters.JWTValidationFilter;
import com.mindmapper.security.filters.OAuth2SuccessHandler;
import com.mindmapper.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // You can use BCryptPasswordEncoder or any other encoder as per your requirement
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(){
        return new JWTAuthenticationProvider();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager,JwtUtil jwtUtil) throws Exception {
        // Configure your security filter chain here
        // For example, you can set up HTTP security, authentication providers, etc.

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager,jwtUtil);

        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager);

        JWTRefereshFilter jwtRefereshFilter = new JWTRefereshFilter(jwtAuthenticationProvider(),jwtUtil);

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/security/user/register", "/public/**","/oauth2/**", "/login/**","/course-images/**").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                .oauth2Login(oAuth-> oAuth
                        .loginPage("/oauth2/authorization/gitlab")
                        .successHandler(oAuth2SuccessHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter,JWTAuthenticationFilter.class)
                .addFilterAfter(jwtRefereshFilter, JWTValidationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider()
        ,jwtAuthenticationProvider()));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Add your frontend URLs
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.addExposedHeader("authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
