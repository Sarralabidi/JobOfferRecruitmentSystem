package com.example.piproject.config;

import com.example.piproject.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS and disable CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                // Set session management to stateless
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set authorization requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/api/auth/validate-reset-token",
                                "/oauth2/**",
                                "/api/**",
                                "/api/auth/**"
                        ).permitAll()


                        // 🔓 Public: anyone can view job offers
                        //.requestMatchers(HttpMethod.GET, "/api/job-offers", "/api/job-offers/**").permitAll()
                        // 🔐 Authenticated users can create, update, delete job offers
                        //.requestMatchers(HttpMethod.POST, "/api/job-offers").authenticated()
                        //.requestMatchers(HttpMethod.PUT, "/api/job-offers/**").authenticated()
                        //.requestMatchers(HttpMethod.DELETE, "/api/job-offers/**").authenticated()
                        // 🔓 Public: CV upload and recommendation endpoint (if you want to make it private, change this!)
                        //.requestMatchers(HttpMethod.POST, "/api/job-offers/recommend").permitAll()

                        // api mtaa apply to job + api mtaa admins user has to connect first


                        //public anyone can apply
                        //.requestMatchers(HttpMethod.GET, "/api/job-offers", "/api/job-offers/**").permitAll()














                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**").permitAll() // Allow reading posts
                        .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()           // Must be logged in to create
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()          // Must be logged in to update
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()       // Must be logged in to delete

                        .requestMatchers(HttpMethod.GET, "/api/comments/post/**").permitAll() // Allow anyone to read comments for a post
                        .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()       // Require authentication to create comments
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()    // Require authentication to update comments
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated() // Require authentication to delete comments

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Add JWT token filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Set logout
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT))
                        .permitAll()
                );

        return http.build();
    }

    // CORS Configuration Source
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
