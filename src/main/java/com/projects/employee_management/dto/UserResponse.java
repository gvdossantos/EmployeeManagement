package com.projects.employee_management.dto;

import com.projects.employee_management.entities.User;

public record UserResponse(
        Long id,
        String email
) {
    public UserResponse(User entity){
        this(
                entity.getId(),
                entity.getEmail()
        );
    }
}
