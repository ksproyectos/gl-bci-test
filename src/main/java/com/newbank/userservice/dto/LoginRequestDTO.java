package com.newbank.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "token is required")
    private String token;
}
