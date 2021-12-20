package com.raphael.conferenceapp.conferencemanagement.entity;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class Participant {
    Long id;
    String name;
    String email;
    Instant subscribedAt;
    Long conferenceId;
}
