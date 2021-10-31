package com.raphael.conferenceapp.auth.usecase.request;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
}
