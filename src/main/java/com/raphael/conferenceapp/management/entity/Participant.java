package com.raphael.conferenceapp.management.entity;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Participant {
    Long id;
    String name;
    String email;
    LocalDateTime subscribedAt;
    Long conferenceId;
}
