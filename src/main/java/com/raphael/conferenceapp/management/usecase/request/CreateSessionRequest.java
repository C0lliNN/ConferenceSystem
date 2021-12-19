package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Session;
import com.raphael.conferenceapp.management.entity.Speaker;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateSessionRequest(
        @NotBlank(message = "the field is mandatory")
        @Size(max = 150, message = "the field must contain at most {max} characters")
        String title,

        @NotBlank(message = "the field is mandatory")
        String description,

        @NotNull(message = "the field is mandatory")
        @FutureOrPresent(message = "the must contain a future date")
        LocalDateTime startTime,

        @NotNull(message = "the field is mandatory")
        @FutureOrPresent(message = "the must contain a future date")
        LocalDateTime endTime,

        @NotBlank(message = "the field is mandatory")
        @URL(message = "the field must contain a valid URL")
        String accessLink,

        @NotNull(message = "the field is mandatory")
        Long speakerId,

        @NotNull(message = "the field is mandatory")
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
