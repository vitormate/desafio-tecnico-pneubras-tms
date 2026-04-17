package com.pneubras.tms.service;

import com.pneubras.tms.dto.request.LoginRequest;
import com.pneubras.tms.dto.request.RegisterRequest;
import com.pneubras.tms.dto.response.LoginResponse;
import com.pneubras.tms.dto.response.RegisterResponse;
import com.pneubras.tms.entity.User;
import com.pneubras.tms.exception.BusinessException;
import com.pneubras.tms.infra.security.TokenService;
import com.pneubras.tms.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager manager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager manager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    public LoginResponse login(LoginRequest data) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = manager.authenticate(authToken);
        String token = tokenService.generateToker((User) authentication.getPrincipal());

        return new LoginResponse(data.login(), token);
    }

    public RegisterResponse register(RegisterRequest data) {
        Optional<User> user = userRepository.findByLogin(data.login());

        if (user.isPresent()) {
            throw new BusinessException("");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return new RegisterResponse(data.login(), data.role());
    }
}
