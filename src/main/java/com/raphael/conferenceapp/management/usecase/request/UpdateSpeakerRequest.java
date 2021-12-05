package com.raphael.conferenceapp.management.usecase.request;

import com.raphael.conferenceapp.management.entity.Speaker;

import java.util.Optional;

public record UpdateSpeakerRequest(
        String firstName,
        String lastName,
        String email,
        String professionalTitle) {

    public Speaker apply(Speaker speaker) {
        Speaker.SpeakerBuilder builder = speaker.toBuilder();

        Optional.ofNullable(firstName).map(builder::firstName);
        Optional.ofNullable(lastName).map(builder::lastName);
        Optional.ofNullable(email).map(builder::email);
        Optional.ofNullable(professionalTitle).map(builder::professionalTitle);

        return builder.build();
    }
}
