package com.raphael.conferenceapp.notifier.entity;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Conference {
    Long id;
    String title;
    String description;
    LocalDateTime startTime;
}
