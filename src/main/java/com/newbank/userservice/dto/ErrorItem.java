package com.newbank.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorItem {
    private Instant timestamp;
    private int codigo;
    private String detail;
}
