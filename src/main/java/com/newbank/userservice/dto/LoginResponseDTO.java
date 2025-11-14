package com.newbank.userservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhoneDTO {
        private long number;
        private int citycode;
        private String contrycode;
    }
}
