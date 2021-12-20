package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

public record UpdateSpeakerRequest(
        @Size(max = 200, message = "the field must contain at most {max} characters")
        String firstName,

        @Size(max = 200, message = "the field must contain at most {max} characters")
        String lastName,

        @Email(message = "the field must contain a valid email")
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
