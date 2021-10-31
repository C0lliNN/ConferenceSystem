package com.raphael.conferenceapp.auth.usecase;

public interface PasswordEncoder {
    String hashPassword(String password);
    boolean comparePasswordAndHash(String password, String hash);
}
