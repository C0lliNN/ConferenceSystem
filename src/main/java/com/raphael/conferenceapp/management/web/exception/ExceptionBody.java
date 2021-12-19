package com.raphael.conferenceapp.management.web.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionBody {
    String message;
    Iterable<ErrorDetail> details;

    public static ExceptionBody fromMessage(String message) {
        return new ExceptionBody(message, Collections.emptyList());
    }

    public static ExceptionBody fromException(MethodArgumentNotValidException exception) {
        final List<ErrorDetail> errorDetails = exception
                .getFieldErrors()
                .stream()
                .sorted(comparing(FieldError::getField))
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ExceptionBody("The given payload is invalid. Check the 'details' field.", errorDetails);
    }

    public static ExceptionBody fromException(Throwable exception) {
        return fromMessage(exception.getMessage());
    }

    public record ErrorDetail(String field, String message) {
    }
}