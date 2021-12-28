package com.raphael.conferenceapp.notifier.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Participant {
    Long id;
    String name;
    String email;
}
