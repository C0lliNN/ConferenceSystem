package com.raphael.conferenceapp.auth.usecase;

import com.raphael.conferenceapp.auth.domain.User;

public interface TokenGenerator {
    String generateTokenForUser(User user);
}
