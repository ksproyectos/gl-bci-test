package com.newbank.userservice.service;

import com.newbank.userservice.dto.LoginRequestDTO;
import com.newbank.userservice.dto.LoginResponseDTO;
import com.newbank.userservice.dto.UserDTO;
import com.newbank.userservice.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceLoginTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void login_success_setsTokenAndReturnsData() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setToken("dummy-token");

        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUsername(anyString())).thenReturn("user@example.com");

        UserDTO returned = new UserDTO();
    returned.setId(java.util.UUID.randomUUID().toString());
        returned.setName("Test User");
        returned.setEmail("user@example.com");
        returned.setActive(true);

        when(userService.updateUserLastLogin("user@example.com")).thenReturn(returned);
        when(jwtUtil.generateToken(anyString())).thenReturn("new-token");

        // Act
        LoginResponseDTO resp = authenticationService.login(request);

        // Assert
        assertThat(resp).isNotNull();
        assertThat(resp.getEmail()).isEqualTo("user@example.com");
        assertThat(resp.getToken()).isEqualTo("new-token");
    }
}
