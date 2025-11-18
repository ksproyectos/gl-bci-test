package com.newbank.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class SignUpRequestDTO {
    private String name;
    @NotBlank(message = "email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "invalid email format")
    private String email;
    @NotBlank(message = "password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?!.*[A-Z].*[A-Z])(?=(?:.*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z0-9]{8,12}$",
            message = "password must be 8-12 characters long, contain exactly one uppercase letter and exactly two digits")
    private String password;
    private List<PhoneRequestDTO> phones;
}
