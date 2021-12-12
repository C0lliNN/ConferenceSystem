package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.entity.Speaker;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateSessionRequest(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String accessLink,
        Long speakerId) {

    public Session apply(Session session) {
        Session.SessionBuilder builder = session.toBuilder();

        Optional.ofNullable(title).map(builder::title);
        Optional.ofNullable(description).map(builder::description);
        Optional.ofNullable(startTime).map(builder::startTime);
        Optional.ofNullable(endTime).map(builder::endTime);
        Optional.ofNullable(accessLink).map(builder::accessLink);
        Optional.ofNullable(speakerId).map(speakerId -> builder.speaker(Speaker.builder().id(speakerId).build()));

        return builder.build();
    }
}
