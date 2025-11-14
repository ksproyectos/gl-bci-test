package com.newbank.userservice.controller;

import com.newbank.userservice.dto.ErrorItem;
import com.newbank.userservice.dto.ErrorResponse;
import com.newbank.userservice.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void handleBusinessException_returnsGivenCodeAndMessage() {
        BusinessException ex = new BusinessException("business failed", 409);

        ResponseEntity<ErrorResponse> resp = handler.handleBusinessException(ex, request);

        assertThat(resp.getStatusCodeValue()).isEqualTo(409);
        ErrorItem item = resp.getBody().getError().get(0);
        assertThat(item.getCodigo()).isEqualTo(409);
        assertThat(item.getDetail()).isEqualTo("business failed");
        assertThat(item.getTimestamp()).isNotNull();
    }

    @Test
    void handleBindException_formatsFieldErrors() {
        BindException bind = new BindException(new Object(), "obj");
        FieldError fe = new FieldError("obj", "name", "must not be blank");
        bind.addError(fe);

        ResponseEntity<ErrorResponse> resp = handler.handleValidation(bind, request);

        assertThat(resp.getStatusCodeValue()).isEqualTo(400);
        ErrorItem item = resp.getBody().getError().get(0);
        assertThat(item.getDetail()).contains("name: must not be blank");
    }

    @Test
    void handleConstraintViolation_joinsViolations() {
        ConstraintViolation<?> cv = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("phone.number");
        when(cv.getPropertyPath()).thenReturn(path);
        when(cv.getMessage()).thenReturn("must be positive");

        Set<ConstraintViolation<?>> set = Collections.singleton(cv);
        ConstraintViolationException cve = new ConstraintViolationException(set);

        ResponseEntity<ErrorResponse> resp = handler.handleConstraintViolation(cve, request);

        assertThat(resp.getStatusCodeValue()).isEqualTo(400);
        ErrorItem item = resp.getBody().getError().get(0);
        assertThat(item.getDetail()).contains("phone.number: must be positive");
    }

    @Test
    void handleAll_returns500OnUnexpected() {
        RuntimeException ex = new RuntimeException("boom");

        ResponseEntity<ErrorResponse> resp = handler.handleAll(ex, request);

        assertThat(resp.getStatusCodeValue()).isEqualTo(500);
        ErrorItem item = resp.getBody().getError().get(0);
        assertThat(item.getDetail()).isEqualTo("boom");
    }
}
