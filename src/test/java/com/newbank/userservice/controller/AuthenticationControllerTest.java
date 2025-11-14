package com.newbank.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbank.userservice.dto.LoginRequestDTO;
import com.newbank.userservice.dto.LoginResponseDTO;
import com.newbank.userservice.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController userController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void login_missingToken_returnsValidationError() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setToken("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").exists());
    }

    @Test
    void login_success_returns200() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setToken("valid-token");

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setId("1");
        resp.setToken("tkn");
        resp.setCreated(LocalDateTime.now());

        when(authenticationService.login(any())).thenReturn(resp);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tkn"));
    }
}
