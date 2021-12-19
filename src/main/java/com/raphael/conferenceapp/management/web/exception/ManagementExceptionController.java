package com.raphael.conferenceapp.management.web.exception;

import com.raphael.conferenceapp.management.exception.DeletionConflictException;
import com.raphael.conferenceapp.management.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ManagementExceptionController {

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
}
