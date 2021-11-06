package com.raphael.conferenceapp.auth.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String name;
    String email;
    @With
    String password;
}
