package com.example.bookhaven.service;

import com.example.bookhaven.dto.LoginResponseDTO;
import com.example.bookhaven.dto.LoginRequestDTO;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.UserRepository;
import com.example.bookhaven.config.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil =  jwtTokenUtil;
    }

    public LoginResponseDTO loginUser(LoginRequestDTO request) {

        if(request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }

        if(request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty.");
        }

        if(request.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters.");
        }

        User existingUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        if (!existingUser.isActive()) {
            throw new RuntimeException("This account has been deactivated. Please contact an admin to reactivate your account.");
        }

        String token = jwtTokenUtil.generateToken(
                existingUser.getUsername(),
                existingUser.getRole()
        );

        return new LoginResponseDTO(
                token,
                existingUser.getRole()
        );
    }
}
