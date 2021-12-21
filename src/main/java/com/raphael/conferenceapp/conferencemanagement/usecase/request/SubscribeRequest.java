package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Participant;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

public record SubscribeRequest(
        @NotBlank(message = "the field is mandatory")
        String name,

        @NotBlank(message = "the field is mandatory")
        @Email(message = "the field must contain a valid email")
        String email,

        Long conferenceId) {

    public SubscribeRequest withConferenceId(Long conferenceId) {
        return new SubscribeRequest(name, email, conferenceId);
    }

    public Participant toParticipant(Instant subscribedAt) {
        return Participant.builder()
                .name(name)
                .email(email)
                .conferenceId(conferenceId)
                .subscribedAt(subscribedAt)
                .build();
    }
}
