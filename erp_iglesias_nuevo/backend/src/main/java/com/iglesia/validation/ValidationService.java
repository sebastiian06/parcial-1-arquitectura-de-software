package com.iglesia.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.iglesia.dto.request.PersonRequest;

@Service
public class ValidationService {

    public void validatePerson(PersonRequest request) {
        List<ValidationException> errors = new ArrayList<>();

        // Crear validadores (todos requeridos por ahora)
        Validator<String> documentValidator = new DocumentValidator(true);
        Validator<String> phoneValidator = new PhoneValidator(true);
        Validator<String> emailValidator = new EmailValidator(true);

        // Ejecutar validaciones y capturar errores
        try {
            documentValidator.validate(request.document());
        } catch (ValidationException e) {
            errors.add(e);
        }

        try {
            phoneValidator.validate(request.phone());
        } catch (ValidationException e) {
            errors.add(e);
        }

        try {
            emailValidator.validate(request.email());
        } catch (ValidationException e) {
            errors.add(e);
        }

        // Si hay errores, lanzar excepción compuesta
        if (!errors.isEmpty()) {
            throw new CompositeValidationException(errors);
        }
    }
}