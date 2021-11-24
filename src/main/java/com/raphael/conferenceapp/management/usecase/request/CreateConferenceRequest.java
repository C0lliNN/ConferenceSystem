package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Conference;

import java.time.LocalDateTime;

public record CreateConferenceRequest(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer participantLimit) {

    public Conference toDomain(Long userId) {
        return Conference.builder()
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .participantLimit(participantLimit)
                .userId(userId)
                .build();
    }
}
