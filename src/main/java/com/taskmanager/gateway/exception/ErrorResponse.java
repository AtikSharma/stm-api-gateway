package com.taskmanager.gateway.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"status", "correlationId", "title", "detail", "instance", "type"})
public class ErrorResponse extends ProblemDetail {

    public ResponseEntity<ErrorResponse> build(String detail, String title, String requestUrl, HttpStatus status) {
        setStatus(status.value());
        setTitle(title);
        setDetail(detail);
        setInstance(requestUrl != null ? URI.create(requestUrl) : null);
        return new ResponseEntity<ErrorResponse>(this, status);
    }
}
