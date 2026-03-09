package com.iglesia.validation;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeValidationException extends RuntimeException {
    private final List<ValidationException> errors;

    public CompositeValidationException(List<ValidationException> errors) {
        super(buildMessage(errors));
        this.errors = errors;
    }

    private static String buildMessage(List<ValidationException> errors) {
        return errors.stream()
            .map(e -> e.getField() + ": " + e.getMessage())
            .collect(Collectors.joining("; "));
    }

    public List<ValidationException> getErrors() {
        return errors;
    }
}