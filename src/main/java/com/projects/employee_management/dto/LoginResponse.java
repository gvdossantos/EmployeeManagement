package com.projects.employee_management.dto;

public record LoginResponse(
        String acessToken,
        String refreshToken,
        Long expiresIn
) {
}
