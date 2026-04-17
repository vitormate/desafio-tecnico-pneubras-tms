package com.pneubras.tms.exception;

import com.pneubras.tms.dto.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> businessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CustomErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage())
        );
    }
}
