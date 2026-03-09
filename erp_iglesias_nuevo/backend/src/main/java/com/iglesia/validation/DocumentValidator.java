package com.iglesia.validation;

import java.util.regex.Pattern;

public class DocumentValidator implements Validator<String> {
    private final boolean required;
    private static final Pattern DIGITS_ONLY = Pattern.compile("^\\d+$");
    
    public DocumentValidator(boolean required) {
        this.required = required;
    }

    @Override
    public void validate(String value) throws ValidationException {
        // Si no es requerido y está vacío, pasa la validación
        if (!required && (value == null || value.trim().isEmpty())) {
            return;
        }

        // Si es requerido y está vacío
        if (required && (value == null || value.trim().isEmpty())) {
            throw new ValidationException("document", value, 
                "El documento es obligatorio");
        }

        // Validar que solo contenga dígitos
        if (!DIGITS_ONLY.matcher(value).matches()) {
            throw new ValidationException("document", value, 
                "El documento debe contener solo números");
        }

        // Validar longitud mínima (5 dígitos)
        if (value.length() < 5) {
            throw new ValidationException("document", value, 
                "El documento debe tener al menos 5 dígitos");
        }

        // Validar longitud máxima (10 dígitos)
        if (value.length() > 10) {
            throw new ValidationException("document", value, 
                "El documento debe tener máximo 10 dígitos");
        }
    }

    @Override
    public String getFieldName() {
        return "document";
    }
}