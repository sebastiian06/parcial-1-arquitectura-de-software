package com.iglesia.validation;

import java.util.regex.Pattern;

public class PhoneValidator implements Validator<String> {
    private final boolean required;
    // Patrón para teléfono colombiano: fijo 7-10 dígitos, celular 3 + 9 dígitos
    private static final Pattern COLOMBIAN_PHONE = Pattern.compile(
        "^(3\\d{9}|\\d{7,10})$"
    );

    public PhoneValidator(boolean required) {
        this.required = required;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (!required && (value == null || value.trim().isEmpty())) {
            return;
        }

        if (required && (value == null || value.trim().isEmpty())) {
            throw new ValidationException("phone", value, 
                "El teléfono es obligatorio");
        }

        // Limpiar espacios, guiones y paréntesis
        String cleanPhone = value.replaceAll("[\\s\\-()]", "");
        
        if (!COLOMBIAN_PHONE.matcher(cleanPhone).matches()) {
            throw new ValidationException("phone", value, 
                "El teléfono no tiene un formato válido. Debe ser un número fijo (7-10 dígitos) o celular (3xx xxx xxxx)");
        }
    }

    @Override
    public String getFieldName() {
        return "phone";
    }
}