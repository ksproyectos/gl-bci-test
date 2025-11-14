package com.newbank.userservice.controller;

import com.newbank.userservice.dto.LoginRequestDTO;
import com.newbank.userservice.dto.LoginResponseDTO;
import com.newbank.userservice.dto.SignUpRequestDTO;
import com.newbank.userservice.dto.SignUpResponseDTO;
import com.newbank.userservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}