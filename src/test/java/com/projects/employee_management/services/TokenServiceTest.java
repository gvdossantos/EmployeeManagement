package com.projects.employee_management.services;

import com.projects.employee_management.entities.RefreshToken;
import com.projects.employee_management.entities.User;
import com.projects.employee_management.factories.RefreshTokenFactory;
import com.projects.employee_management.factories.UserFactory;
import com.projects.employee_management.repositories.RefreshTokenRepository;
import com.projects.employee_management.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private RefreshTokenService service;

    @Mock
    private RefreshTokenRepository repository;

    @Mock
    private UserRepository userRepository;
    private Long existingId;
    private User user;
    private String token;
    private RefreshToken refreshToken;

    @BeforeEach
    public void setUp() {

        existingId = 1L;
        token = UUID.randomUUID().toString();
        user = UserFactory.createUser();
        refreshToken = RefreshTokenFactory.createRefreshToken(user);

        Mockito.when(userRepository.findById(existingId))
                .thenReturn(Optional.of(user));

        Mockito.when(repository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));
    }


    @Test
    public void generateRefreshTokenShouldReturnNewTokenWhenIdExits() {

       ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);

        String result = service.generateRefreshToken(existingId);
        Assertions.assertNotNull(result);

        Mockito.verify(repository).save(tokenCaptor.capture());

        RefreshToken saved = tokenCaptor.getValue();

        Assertions.assertEquals(existingId, saved.getUser().getId());
        Assertions.assertEquals(result, saved.getToken());

    }

    @Test
    public void tokenShouldNotBeExpiredWhenExpiryDateIsInFuture(){

        boolean expired = service.isTokenExpired(refreshToken);

        Assertions.assertFalse(expired);


    }


    @Test
    public void validateRefreshTokenAndGetUsernameShouldReturnEmailWhenTokenExists(){

        String result = service.validateRefreshTokenAndGetUsername(token);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result);

    }

    @Test
    public void deleteTokenShouldReturnNothing(){

        service.deleteByToken(token);

        Mockito.verify(repository).deleteByToken(token);

    }
}
