package com.raphael.conferenceapp.conferencemanagement.exception;

import java.io.Serial;

public class FullConferenceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FullConferenceException(final String message, Object... args) {
        super(String.format(message, args));
    }
}
