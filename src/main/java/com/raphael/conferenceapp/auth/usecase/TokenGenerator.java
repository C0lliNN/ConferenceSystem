package com.raphael.conferenceapp.auth.usecase;

import com.raphael.conferenceapp.auth.entity.User;

public interface TokenGenerator {
    String generateTokenForUser(User user);
}
