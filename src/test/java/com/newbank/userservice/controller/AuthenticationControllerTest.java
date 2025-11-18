package com.newbank.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbank.userservice.dto.LoginRequestDTO;
import com.newbank.userservice.dto.SignUpRequestDTO;
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
    void shouldReturnValidationErrorsWhenLogin() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setToken("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("token: token is required"));
    }

    @Test
    void shouldReturnValidationErrorsWhenSignUp() throws Exception {
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"goodemail@domain.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("password: password is required"));

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"abcdef44E\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("email: email is required"));

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"goodemail@domain.com\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("password: password must be 8-12 characters long, contain exactly one uppercase letter and exactly two digits"));

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bad-email\",\"password\":\"abcdef44E\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("email: invalid email format"));
    }

    @Test
    void shouldReturnSuccess200WhenLogin() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setToken("valid-token");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSuccess200WhenSignUp() throws Exception {
        SignUpRequestDTO req = new SignUpRequestDTO();
        req.setEmail("goodemail@domain.com");
        req.setPassword("Abcdef12");

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());


    }


}
