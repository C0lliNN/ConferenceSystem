package com.raphael.conferenceapp.conferencemanagement.exception;

import java.io.Serial;

public class ParticipantAlreadyRegisteredException  extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParticipantAlreadyRegisteredException(final String message, Object... args) {
        super(String.format(message, args));
    }
}
