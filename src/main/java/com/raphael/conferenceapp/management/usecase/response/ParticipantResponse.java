package com.raphael.conferenceapp.management.usecase.response;

import com.raphael.conferenceapp.management.entity.Participant;

import java.time.LocalDateTime;

public record ParticipantResponse(
        Long id,
        String name,
        String email,
        LocalDateTime subscribedAt,
        Long conferenceId) {

    public static ParticipantResponse fromDomain(Participant participant) {
        return new ParticipantResponse(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getSubscribedAt(),
                participant.getConferenceId()
        );
    }
}
