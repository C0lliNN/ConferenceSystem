package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Speaker;

public record CreateSpeakerRequest(
        String firstName,
        String lastName,
        String email,
        String professionalTitle) {

    public Speaker toDomain() {
        return Speaker.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .professionalTitle(professionalTitle)
                .build();
    }
}
