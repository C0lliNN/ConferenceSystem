package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.entity.Speaker;

import java.time.LocalDateTime;

public record CreateSessionRequest(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String accessLink,
        Long speakerId,
        Long conferenceId) {

    public CreateSessionRequest withConferenceId(Long conferenceId) {
        return new CreateSessionRequest(
                title,
                description,
                startTime,
                endTime,
                accessLink,
                speakerId,
                conferenceId
        );
    }

    public Session toDomain() {
        return Session.builder()
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .accessLink(accessLink)
                .speaker(Speaker.builder().id(speakerId).build())
                .conferenceId(conferenceId)
                .build();
    }
}
