package com.raphael.conferenceapp.auth.usecase.response;

import com.raphael.conferenceapp.auth.domain.User;


public record UserResponse(Long id, String name, String email, String token) {

    public UserResponse withToken(String token) {
        return new UserResponse(id, name, email, token);
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
    }
}
