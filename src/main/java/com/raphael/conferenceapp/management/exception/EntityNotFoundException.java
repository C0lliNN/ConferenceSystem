package com.raphael.conferenceapp.management.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(final String message, Object... args) {
        super(String.format(message, args));
    }
}
