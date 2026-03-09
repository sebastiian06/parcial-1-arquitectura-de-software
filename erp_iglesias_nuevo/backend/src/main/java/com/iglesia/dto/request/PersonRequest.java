package com.iglesia.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PersonRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String firstName,
    
    @NotBlank(message = "El apellido es obligatorio")
    String lastName,
    
    @Pattern(regexp = "^[0-9]*$", message = "El documento debe contener solo números")
    String document,
    
    @Pattern(regexp = "^[0-9+]*$", message = "El teléfono debe contener solo números y el símbolo +")
    String phone,
    
    @Email(message = "El email debe tener un formato válido")
    String email
) {}