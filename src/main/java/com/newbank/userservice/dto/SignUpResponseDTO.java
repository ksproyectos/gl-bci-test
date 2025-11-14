package com.newbank.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignUpResponseDTO {
    private String id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
}
