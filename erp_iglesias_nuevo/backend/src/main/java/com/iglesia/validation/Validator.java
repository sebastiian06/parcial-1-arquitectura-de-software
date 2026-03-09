package com.iglesia.validation;

public interface Validator<T> {
    void validate(T value) throws ValidationException;
    String getFieldName();
}