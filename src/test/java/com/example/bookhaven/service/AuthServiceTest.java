package com.example.bookhaven.service;

import com.example.bookhaven.config.JwtTokenUtil;
import com.example.bookhaven.dto.LoginRequestDTO;
import com.example.bookhaven.dto.LoginResponseDTO;
import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthService authService;

    private User activeUser;
    private User inactiveUser;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername("testuser");
        activeUser.setPassword("hashedpassword");
        activeUser.setRole("user");
        activeUser.setActive(true);

        inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setPassword("hashedpassword");
        inactiveUser.setRole("user");
        inactiveUser.setActive(false);
    }

    // Username validation

    @Test
    void loginUser_nullUsername_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO(null, "password123");

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    void loginUser_emptyUsername_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("", "password123");

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    void loginUser_blankUsername_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("   ", "password123");

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username cannot be empty");
    }

    // Password validation

    @Test
    void loginUser_nullPassword_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", null);

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password cannot be empty.");
    }

    @Test
    void loginUser_emptyPassword_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "");

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password cannot be empty.");
    }

    @Test
    void loginUser_shortPassword_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "short");

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password must be at least 8 characters.");
    }

    // User lookup

    @Test
    void loginUser_userNotFound_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("unknownuser", "password123");

        when(userRepository.findByUsername("unknownuser"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // Password mismatch

    @Test
    void loginUser_incorrectPassword_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "wrongpassword");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(activeUser));

        when(passwordEncoder.matches("wrongpassword", "hashedpassword"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Incorrect password");
    }

    // Inactive account

    @Test
    void loginUser_deactivatedAccount_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("inactiveuser", "password123");

        when(userRepository.findByUsername("inactiveuser"))
                .thenReturn(Optional.of(inactiveUser));

        when(passwordEncoder.matches("password123", "hashedpassword"))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("This account has been deactivated. Please contact an admin to reactivate your account.");
    }

    // Successful login

    @Test
    void loginUser_validCredentials_returnsLoginResponse() {

        LoginRequestDTO request =
                new LoginRequestDTO("testuser", "password123");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(activeUser));

        when(passwordEncoder.matches("password123", "hashedpassword"))
                .thenReturn(true);

        when(jwtTokenUtil.generateToken("testuser", "user"))
                .thenReturn("mocked.jwt.token");

        LoginResponseDTO response = authService.loginUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(response.getRole()).isEqualTo("user");
    }

    @Test
    void loginUser_callsGenerateToken() {

        LoginRequestDTO request =
                new LoginRequestDTO("testuser", "password123");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(activeUser));

        when(passwordEncoder.matches("password123", "hashedpassword"))
                .thenReturn(true);

        when(jwtTokenUtil.generateToken("testuser", "user"))
                .thenReturn("mocked.jwt.token");

        authService.loginUser(request);

        verify(jwtTokenUtil)
                .generateToken("testuser", "user");
    }

    @Test
    void loginUser_neverCallsGenerateTokenOnFailure() {

        LoginRequestDTO request =
                new LoginRequestDTO("testuser", "wrongpassword");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(activeUser));

        when(passwordEncoder.matches("wrongpassword", "hashedpassword"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.loginUser(request))
                .isInstanceOf(RuntimeException.class);

        verify(jwtTokenUtil, never())
                .generateToken(any(), any());
    }

    @Test
    void loginUser_adminLogin_returnsAdminRole() {

        activeUser.setRole("admin");

        LoginRequestDTO request =
                new LoginRequestDTO("testuser", "password123");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(activeUser));

        when(passwordEncoder.matches("password123", "hashedpassword"))
                .thenReturn(true);

        when(jwtTokenUtil.generateToken("testuser", "admin"))
                .thenReturn("mocked.jwt.token");

        LoginResponseDTO response = authService.loginUser(request);

        assertThat(response.getRole()).isEqualTo("admin");
    }
}