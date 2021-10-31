package com.raphael.conferenceapp.auth.usecase;

import com.raphael.conferenceapp.auth.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
