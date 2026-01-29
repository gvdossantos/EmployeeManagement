package com.projects.employee_management.services;

import com.projects.employee_management.services.exceptions.InvalidRefreshTokenException;
import com.projects.employee_management.entities.RefreshToken;
import com.projects.employee_management.entities.User;
import com.projects.employee_management.repositories.RefreshTokenRepository;
import com.projects.employee_management.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private static final Duration REFRESH_TOKEN_EXPIRY_DURATION = Duration.ofDays(7);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public String generateRefreshToken(Long userId) {

         User user  = userRepository.findById(userId)
                 .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado:" + userId));
         String newToken = UUID.randomUUID().toString();

         RefreshToken refreshToken = new RefreshToken();
         refreshToken.setUser(user);
         refreshToken.setToken(newToken);
         refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRY_DURATION));

         refreshTokenRepository.save(refreshToken);
         return newToken;
    }

    public String validateRefreshTokenAndGetUsername(String givenToken) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(givenToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token inválido"));

        if (isTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("Refresh token expirado. Faça login novamente.");
        }

        return refreshToken.getUser().getEmail();
    }

    public boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
