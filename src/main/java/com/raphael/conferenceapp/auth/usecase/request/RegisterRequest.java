package com.raphael.conferenceapp.auth.usecase.request;

import com.raphael.conferenceapp.auth.domain.User;
import lombok.Value;

@Value
public class RegisterRequest {
    String name;
    String email;
    String password;

    public User toUser() {
        return User
                .builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
