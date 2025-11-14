package com.newbank.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhoneDTO {
    private Long number;
    private Integer citycode;
    private String contrycode;
}
