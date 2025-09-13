package com.taskmanager.gateway.exception.handler;

import com.taskmanager.gateway.exception.CustomSecurityException;
import com.taskmanager.gateway.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomSecurityException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleCustomSecurityException(
            CustomSecurityException exception) {
        return new ErrorResponse().build(exception.getMessage(), exception.getStatus().getReasonPhrase(),
                null, exception.getStatus());
    }
}
