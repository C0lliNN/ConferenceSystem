package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Conference;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateConferenceRequest(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer participantLimit) {

    public Conference apply(Conference conference) {
        Conference.ConferenceBuilder builder = conference.toBuilder();

        Optional.ofNullable(title).map(builder::title);
        Optional.ofNullable(description).map(builder::description);
        Optional.ofNullable(startTime).map(builder::startTime);
        Optional.ofNullable(endTime).map(builder::endTime);
        Optional.ofNullable(participantLimit).map(builder::participantLimit);

        return builder.build();
    }
}
