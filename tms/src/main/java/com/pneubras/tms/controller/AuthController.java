package com.pneubras.tms.controller;

import com.pneubras.tms.dto.request.LoginRequest;
import com.pneubras.tms.dto.request.RegisterRequest;
import com.pneubras.tms.dto.response.LoginResponse;
import com.pneubras.tms.dto.response.RegisterResponse;
import com.pneubras.tms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest data) {
        LoginResponse dto = authService.login(data);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest data) {
        RegisterResponse dto = authService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
