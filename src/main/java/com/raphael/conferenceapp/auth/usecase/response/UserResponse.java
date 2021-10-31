package com.raphael.conferenceapp.auth.usecase.response;

import com.raphael.conferenceapp.auth.domain.User;
import lombok.Value;
import lombok.With;

@Value
public class UserResponse {
    Long id;
    String name;
    String email;
    @With
    String token;

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), null);
    }
}
