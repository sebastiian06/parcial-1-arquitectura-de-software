package com.iglesia.validation;

public class ValidationException extends RuntimeException {
    private final String field;
    private final String invalidValue;

    public ValidationException(String field, String invalidValue, String message) {
        super(message);
        this.field = field;
        this.invalidValue = invalidValue;
    }

    public String getField() { return field; }
    public String getInvalidValue() { return invalidValue; }
}