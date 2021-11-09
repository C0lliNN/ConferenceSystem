package com.raphael.conferenceapp.auth.usecase.request;

import com.raphael.conferenceapp.auth.domain.User;

public record RegisterRequest(String name, String email, String password) {
    public User toUser() {
        return User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
