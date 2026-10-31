package com.example.appointmentservice.config;

import com.example.appointmentservice.dto.ApiResponseError;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class RateLimiterExceptionHandler {

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiResponseError> handleRateLimitExceeded(RequestNotPermitted exception) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ApiResponseError(
                        "Bạn đã gửi quá 5 request trong 10 giây",
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Too Many Requests",
                        Instant.now()));
    }
}
