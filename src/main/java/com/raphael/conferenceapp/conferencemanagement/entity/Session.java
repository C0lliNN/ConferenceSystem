package com.raphael.conferenceapp.conferencemanagement.entity;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Session {
    Long id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String title;
    String description;
    String accessLink;
    Long conferenceId;
    Speaker speaker;
}
