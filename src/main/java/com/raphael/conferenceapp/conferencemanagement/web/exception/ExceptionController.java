package com.raphael.conferenceapp.conferencemanagement.web.exception;

import com.raphael.conferenceapp.conferencemanagement.exception.DeletionConflictException;
import com.raphael.conferenceapp.conferencemanagement.exception.EntityNotFoundException;
import com.raphael.conferenceapp.conferencemanagement.exception.FullConferenceException;
import com.raphael.conferenceapp.conferencemanagement.exception.ParticipantAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component("conferenceManagementExceptionController")
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionController {

    @ExceptionHandler(DeletionConflictException.class)
    public ResponseEntity<ExceptionBody> handleDeletionConflictException(Exception exception) {
        log.warn("A conflict happened when trying to delete an entity: {}", exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromException(exception));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.warn("An attempt to fetch a not found entity was made: {}", exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionBody.fromException(exception));
    }

    @ExceptionHandler(ParticipantAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionBody> handleParticipantAlreadyRegisteredException(ParticipantAlreadyRegisteredException e) {
        log.warn("An attempt to subscribe twice in the same conference was made: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromException(e));
    }

    @ExceptionHandler(FullConferenceException.class)
    public ResponseEntity<ExceptionBody> handleFullConferenceException(FullConferenceException exception) {
        log.warn("An attempt to subscribe to a full conference wa made: {}", exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionBody.fromException(exception));
    }
}
