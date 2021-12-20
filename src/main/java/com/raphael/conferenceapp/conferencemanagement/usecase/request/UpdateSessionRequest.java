package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Session;
import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateSessionRequest(
        @Size(max = 150, message = "the field must contain at most {max} characters")
        String title,

        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,

        @URL(message = "the field must contain a valid URL")
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
