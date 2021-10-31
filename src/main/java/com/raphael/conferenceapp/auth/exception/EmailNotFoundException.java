package com.raphael.conferenceapp.auth.exception;

import java.io.Serial;

public class EmailNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailNotFoundException(final String message, Object... args) {
        super(String.format(message, args));
    }
}
