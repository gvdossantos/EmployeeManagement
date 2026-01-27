package com.projects.employee_management.repositories;

import com.projects.employee_management.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

     Optional<RefreshToken> findByToken(String givenToken);

    void deleteByToken(String token);
}
