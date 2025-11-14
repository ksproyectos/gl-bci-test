package com.newbank.userservice.controller;

import com.newbank.userservice.dto.ErrorItem;
import com.newbank.userservice.dto.ErrorResponse;
import com.newbank.userservice.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        int status = ex.getCode() > 0 ? ex.getCode() : HttpStatus.BAD_REQUEST.value();
        ErrorItem item = new ErrorItem(Instant.now(), status, ex.getMessage());
        ErrorResponse body = new ErrorResponse(java.util.Collections.singletonList(item));
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex, HttpServletRequest request) {
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manv = (MethodArgumentNotValidException) ex;
            message = manv.getBindingResult().getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else { // BindException
            BindException be = (BindException) ex;
            message = be.getBindingResult().getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ErrorItem item = new ErrorItem(Instant.now(), HttpStatus.BAD_REQUEST.value(), message);
        ErrorResponse body = new ErrorResponse(java.util.Collections.singletonList(item));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        ErrorItem item = new ErrorItem(Instant.now(), HttpStatus.BAD_REQUEST.value(), msg);
        ErrorResponse body = new ErrorResponse(java.util.Collections.singletonList(item));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        ErrorItem item = new ErrorItem(Instant.now(), HttpStatus.BAD_REQUEST.value(), message);
        ErrorResponse body = new ErrorResponse(java.util.Collections.singletonList(item));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        ErrorItem item = new ErrorItem(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        ErrorResponse body = new ErrorResponse(java.util.Collections.singletonList(item));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
