package com.projects.employee_management.factories;

import com.projects.employee_management.entities.RefreshToken;
import com.projects.employee_management.entities.User;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class RefreshTokenFactory {


    public static RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofDays(7)));

        return refreshToken;
    }
}
