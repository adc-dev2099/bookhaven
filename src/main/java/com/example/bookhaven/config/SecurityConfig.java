package com.example.bookhaven.config;

//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // allow preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // static
                        .requestMatchers(
                                "/",
                                "/favicon.ico",
                                "/*.html",
                                "/*.css",
                                "/*.js",
                                "/*.png",
                                "/*.jpg",
                                "/*.svg",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // public auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // public book & category reads only
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // admin routes
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // everything else requires login
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "http://192.168.100.221:8080"
        ));

        config.setAllowedMethods(List.of(
                "GET","POST","PUT","PATCH","DELETE","OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}