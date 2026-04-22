package com.example.bookhaven.service;

import com.example.bookhaven.dto.RegisterResponseDTO;
import com.example.bookhaven.dto.RegisterRequestDTO;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // <-- ADD THIS

        User savedUser = userRepository.save(user);

        return new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getUsername()
        );
    }

    public User updateUser(Long id, Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String firstName = body.get("firstName");
        String lastName  = body.get("lastName");
        String username  = body.get("username");
        String password  = body.get("password");
        String role      = body.get("role");

        if (firstName != null && !firstName.isBlank()) user.setFirstName(firstName.trim());
        if (lastName  != null && !lastName.isBlank())  user.setLastName(lastName.trim());

        if (username != null && !username.isBlank() && !username.equals(user.getUsername())) {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username already exists.");
            }
            user.setUsername(username.trim());
        }

        if (password != null && !password.isBlank()) {
            if (password.length() < 8) {
                throw new RuntimeException("Password must be at least 8 characters.");
            }
            user.setPassword(passwordEncoder.encode(password));
        }

        if (role != null && (role.equals("user") || role.equals("admin"))) {
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
