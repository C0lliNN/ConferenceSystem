package com.raphael.conferenceapp.conferencemanagement.usecase.response;

import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;

public record SpeakerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String professionalTitle) {

    public static SpeakerResponse fromDomain(Speaker speaker) {
        return new SpeakerResponse(
                speaker.getId(),
                speaker.getFirstName(),
                speaker.getLastName(),
                speaker.getEmail(),
                speaker.getProfessionalTitle()
        );
    }
}
