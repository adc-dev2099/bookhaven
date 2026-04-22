package com.example.bookhaven.service;

import com.example.bookhaven.model.User;
import com.example.bookhaven.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;
    private User admin;
    private User inactive;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        user.setRole("user");
        user.setActive(true);

        admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setRole("admin");
        admin.setActive(true);

        inactive = new User();
        inactive.setId(3L);
        inactive.setUsername("inactive");
        inactive.setPassword("password");
        inactive.setRole("user");
        inactive.setActive(false);
    }

    @Test
    void loadUserByUsername_activeUser_hasCorrectAuthority() {

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername("user");

        List<String> authorities =
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        assertThat(userDetails.getUsername()).isEqualTo("user");
        assertThat(authorities).containsExactly("ROLE_USER");
    }

    @Test
    void loadUserByUsername_adminUser_hasAdminAuthority() {

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(admin));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername("admin");

        List<String> authorities =
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        assertThat(userDetails.getUsername()).isEqualTo("admin");
        assertThat(authorities).containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_userNotFound() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                customUserDetailsService.loadUserByUsername("missing")
        ).isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void loadUserByUsername_inactiveUser() {

        when(userRepository.findByUsername("inactive"))
                .thenReturn(Optional.of(inactive));

        assertThatThrownBy(() ->
                customUserDetailsService.loadUserByUsername("inactive")
        ).isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account is deactivated");
    }
}