package com.raphael.conferenceapp.auth.web;

import com.raphael.conferenceapp.auth.usecase.AuthUseCase;
import com.raphael.conferenceapp.auth.usecase.request.LoginRequest;
import com.raphael.conferenceapp.auth.usecase.request.RegisterRequest;
import com.raphael.conferenceapp.auth.usecase.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCase useCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody RegisterRequest request) {
        return useCase.register(request);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        return useCase.login(request);
    }
}
