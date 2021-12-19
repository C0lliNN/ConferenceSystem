package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Conference;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateConferenceRequest(
        @Size(max = 150, message = "the field must contain at most {max} characters")
        String title,

        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,

        @Min(value = 1, message = "the field must contain values greater or equal to {value}")
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
