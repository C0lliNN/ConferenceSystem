package com.raphael.conferenceapp.auth.usecase.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "the field is mandatory")
        @Email(message = "the field must be a valid email")
        String email,

        @NotBlank(message = "the field is mandatory")
        String password) {
}
