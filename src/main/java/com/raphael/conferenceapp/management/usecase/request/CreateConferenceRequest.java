package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Conference;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateConferenceRequest(
        @NotBlank(message = "the field is mandatory")
        @Size(max = 150, message = "the field must contain at most {max} characters")
        String title,

        @NotBlank(message = "the field is mandatory")
        String description,

        @NotNull(message = "the field is mandatory")
        LocalDateTime startTime,

        @NotNull(message = "the field is mandatory")
        LocalDateTime endTime,

        @Min(value = 1, message = "the field must contain values greater or equal to {value}")
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
