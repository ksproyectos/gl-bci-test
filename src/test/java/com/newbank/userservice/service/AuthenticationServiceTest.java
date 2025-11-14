package com.newbank.userservice.service;

import com.newbank.userservice.dto.*;
import com.newbank.userservice.exception.BusinessException;
import com.newbank.userservice.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpRequestDTO validSignUp;

    @BeforeEach
    void setUp() {
        validSignUp = new SignUpRequestDTO();
        validSignUp.setName("Alice");
        validSignUp.setEmail("alice@example.com");
        validSignUp.setPassword("Abcde12f");
        validSignUp.setPhones(Collections.emptyList());
    }

    @Test
    void signUp_success_generatesToken() {
    UserDTO created = new UserDTO();
    created.setId(java.util.UUID.randomUUID().toString());
        created.setName(validSignUp.getName());
        created.setEmail(validSignUp.getEmail());
        created.setPassword(validSignUp.getPassword());

        when(userService.createUser(any())).thenReturn(created);
        when(jwtUtil.generateToken(any())).thenReturn("tok-123");

        SignUpResponseDTO resp = authenticationService.signUp(validSignUp);

    assertThat(resp).isNotNull();
    assertThat(resp.getToken()).isEqualTo("tok-123");
    assertThat(resp.getId()).isNotBlank();
    }

    @Test
    void signUp_invalidEmail_throwsBusinessException() {
        validSignUp.setEmail("bad-email");

        assertThatThrownBy(() -> authenticationService.signUp(validSignUp))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(authenticationService.EMAIL_FORMAT_ERROR_MESSAGE);
    }

    @Test
    void signUp_invalidPassword_throwsBusinessException() {
        validSignUp.setPassword("short");

        assertThatThrownBy(() -> authenticationService.signUp(validSignUp))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(authenticationService.PASSWORD_FORMAT_ERROR_MESSAGE);
    }
}
