package com.newbank.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequestDTO {
    private String name;
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    private List<PhoneRequestDTO> phones;
}
