package com.iglesia.validation;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<String> {
    private final boolean required;
    // Regex robusto para email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public EmailValidator(boolean required) {
        this.required = required;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (!required && (value == null || value.trim().isEmpty())) {
            return;
        }

        if (required && (value == null || value.trim().isEmpty())) {
            throw new ValidationException("email", value, 
                "El email es obligatorio");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new ValidationException("email", value, 
                "El email no tiene un formato válido. Debe ser nombre@dominio.com");
        }
    }

    @Override
    public String getFieldName() {
        return "email";
    }
}