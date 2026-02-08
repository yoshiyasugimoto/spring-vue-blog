package com.blog.service;

import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
        User user = TestDataFactory.createUser(1L, "admin", "Admin", "ADMIN");
        user.setPassword("hashed_password");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getPassword()).isEqualTo("hashed_password");
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_ThrowsException_WhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found: unknown");
    }

    @Test
    void loadUserByUsername_CorrectlyPrefixesRoleWithROLE() {
        User user = TestDataFactory.createUser(1L, "editor", "Editor", "EDITOR");
        when(userRepository.findByUsername("editor")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("editor");

        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_EDITOR");
    }

    @Test
    void findByUsername_ReturnsUser_WhenExists() {
        User user = TestDataFactory.createUser(1L, "admin", "Admin User", "ADMIN");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getDisplayName()).isEqualTo("Admin User");
        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void findByUsername_ThrowsException_WhenNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found: unknown");
    }
}
