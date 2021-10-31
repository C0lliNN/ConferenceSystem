package com.raphael.conferenceapp.auth.web.exception;

import com.raphael.conferenceapp.auth.exception.DuplicateEmailException;
import com.raphael.conferenceapp.auth.exception.EmailNotFoundException;
import com.raphael.conferenceapp.auth.exception.IncorrectPasswordException;
import com.raphael.conferenceapp.auth.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    private static final String GENERIC_SERVER_ERROR_BODY_MESSAGE = "There's been an unexpected error. Please, check logs or contact support.";

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionBody> handleDuplicateEmailException(DuplicateEmailException e) {
        log.error("An attempt to create a user with existing email was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("An attempt to login with a not found email was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ExceptionBody> handleIncorrectPasswordException(IncorrectPasswordException e) {
        log.error("An attempt to login with a wrong password was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionBody> handleInvalidTokenException(InvalidTokenException e) {
        log.error("An attempt to perform a request with an invalid token was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionBody> handleGeneralError(Throwable throwable) {
        log.error("An unexpected error was thrown when performing the request: {}", throwable.getMessage(), throwable);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionBody.fromMessage(GENERIC_SERVER_ERROR_BODY_MESSAGE));
    }
}
