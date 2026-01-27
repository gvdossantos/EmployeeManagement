package com.projects.employee_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @Email String email,
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres") String password) {
}
