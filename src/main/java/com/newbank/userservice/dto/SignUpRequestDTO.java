package com.newbank.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpRequestDTO {
    private String name;
    private String email;
    private String password;
    private List<PhoneRequestDTO> phones;
}
