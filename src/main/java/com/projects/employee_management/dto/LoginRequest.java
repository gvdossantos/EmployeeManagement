package com.projects.employee_management.dto;

public record LoginRequest(
        String email,
        String password
) {
}
