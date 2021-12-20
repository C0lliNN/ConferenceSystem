package com.raphael.conferenceapp.conferencemanagement.usecase.request;

import com.raphael.conferenceapp.conferencemanagement.entity.Speaker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateSpeakerRequest(
        @NotBlank(message = "the field is mandatory")
        @Size(max = 200, message = "the field must contain at most {max} characters")
        String firstName,

        @NotBlank(message = "the field is mandatory")
        @Size(max = 200, message = "the field must contain at most {max} characters")
        String lastName,

        @NotBlank(message = "the field is mandatory")
        @Email(message = "the field must contain a valid email")
        String email,

        @NotBlank(message = "the field is mandatory")
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
