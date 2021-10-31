package com.raphael.conferenceapp.auth.exception;

import java.io.Serial;

public class DuplicateEmailException  extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateEmailException(final String message, Object... args) {
        super(String.format(message, args));
    }
}