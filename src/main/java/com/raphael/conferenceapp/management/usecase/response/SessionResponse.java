package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Session;

import java.time.LocalDateTime;

public record SessionResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String title,
        String description,
        String accessLink,
        SpeakerResponse speaker) {

    public static SessionResponse fromDomain(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getStartTime(),
                session.getEndTime(),
                session.getTitle(),
                session.getDescription(),
                session.getAccessLink(),
                SpeakerResponse.fromDomain(session.getSpeaker())
        );
    }
}
