package com.projects.employee_management.factories;

import com.projects.employee_management.entities.User;

public class UserFactory {

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("Teste123@gmail.com");
        user.setPassword("Teste123");

        return user;
    }
}
