package com.example.bookhaven.service;

import com.example.bookhaven.dto.RegisterRequestDTO;
import com.example.bookhaven.dto.RegisterResponseDTO;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setUsername("johndoe");
        existingUser.setPassword("hashedpassword");
        existingUser.setRole("user");
        existingUser.setActive(true);
    }

    // ─── registerUser ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when username is null")
    void registerUser_nullUsername_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername(null);
        request.setPassword("password123");

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    @DisplayName("Should throw when username is empty")
    void registerUser_emptyUsername_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("  ");
        request.setPassword("password123");

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username cannot be empty");
    }

    @Test
    @DisplayName("Should throw when password is null")
    void registerUser_nullPassword_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("newuser");
        request.setPassword(null);

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password must be at least 8 characters");
    }

    @Test
    @DisplayName("Should throw when password is less than 8 characters")
    void registerUser_shortPassword_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("newuser");
        request.setPassword("short");

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password must be at least 8 characters");
    }

    @Test
    @DisplayName("Should throw when username already exists")
    void registerUser_duplicateUsername_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("johndoe");
        request.setPassword("password123");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
    }

    @Test
    @DisplayName("Should register user successfully and return response DTO")
    void registerUser_validRequest_returnsResponseDTO() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setUsername("janesmith");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("janesmith");

        when(userRepository.findByUsername("janesmith")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponseDTO result = userService.registerUser(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getUsername()).isEqualTo("janesmith");
    }

    @Test
    @DisplayName("Should encode password before saving")
    void registerUser_validRequest_encodesPassword() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setUsername("janesmith");
        request.setPassword("password123");

        when(userRepository.findByUsername("janesmith")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.registerUser(request);

        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("Should save user to repository on successful registration")
    void registerUser_validRequest_savesUser() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setUsername("janesmith");
        request.setPassword("password123");

        when(userRepository.findByUsername("janesmith")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.registerUser(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    // ─── updateUser ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when user not found on update")
    void updateUser_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, Map.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("Should update first name when provided")
    void updateUser_newFirstName_updatesFirstName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("firstName", "UpdatedName");

        userService.updateUser(1L, body);

        assertThat(existingUser.getFirstName()).isEqualTo("UpdatedName");
    }

    @Test
    @DisplayName("Should update last name when provided")
    void updateUser_newLastName_updatesLastName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("lastName", "UpdatedLastName");

        userService.updateUser(1L, body);

        assertThat(existingUser.getLastName()).isEqualTo("UpdatedLastName");
    }

    @Test
    @DisplayName("Should update username when new username is available")
    void updateUser_newAvailableUsername_updatesUsername() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("newusername")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("username", "newusername");

        userService.updateUser(1L, body);

        assertThat(existingUser.getUsername()).isEqualTo("newusername");
    }

    @Test
    @DisplayName("Should throw when new username already exists")
    void updateUser_duplicateUsername_throwsException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("takenusername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("takenusername")).thenReturn(Optional.of(anotherUser));

        Map<String, String> body = new HashMap<>();
        body.put("username", "takenusername");

        assertThatThrownBy(() -> userService.updateUser(1L, body))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists.");
    }

    @Test
    @DisplayName("Should not update username when same as current")
    void updateUser_sameUsername_doesNotUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("username", "johndoe");

        userService.updateUser(1L, body);

        verify(userRepository, never()).findByUsername("johndoe");
        assertThat(existingUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Should update password when valid new password provided")
    void updateUser_validNewPassword_updatesPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword123")).thenReturn("newhashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("password", "newpassword123");

        userService.updateUser(1L, body);

        verify(passwordEncoder, times(1)).encode("newpassword123");
        assertThat(existingUser.getPassword()).isEqualTo("newhashedpassword");
    }

    @Test
    @DisplayName("Should throw when new password is less than 8 characters")
    void updateUser_shortPassword_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        Map<String, String> body = new HashMap<>();
        body.put("password", "short");

        assertThatThrownBy(() -> userService.updateUser(1L, body))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Password must be at least 8 characters.");
    }

    @Test
    @DisplayName("Should update role to admin when valid role provided")
    void updateUser_validAdminRole_updatesRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("role", "admin");

        userService.updateUser(1L, body);

        assertThat(existingUser.getRole()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should not update role when invalid role provided")
    void updateUser_invalidRole_doesNotUpdateRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("role", "superadmin");

        userService.updateUser(1L, body);

        assertThat(existingUser.getRole()).isEqualTo("user");
    }

    @Test
    @DisplayName("Should save user after update")
    void updateUser_validUpdate_savesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Map<String, String> body = new HashMap<>();
        body.put("firstName", "Updated");

        userService.updateUser(1L, body);

        verify(userRepository, times(1)).save(existingUser);
    }

    // ─── deleteUser ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should throw when user not found on delete")
    void deleteUser_userNotFound_throwsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_existingUser_deletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should not call deleteById when user not found")
    void deleteUser_userNotFound_doesNotCallDeleteById() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).deleteById(any());
    }
}