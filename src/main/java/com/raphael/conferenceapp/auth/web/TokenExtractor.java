package com.raphael.conferenceapp.auth.web;

import com.raphael.conferenceapp.auth.entity.User;

public interface TokenExtractor {
    User extractUserFromToken(String token);
}
