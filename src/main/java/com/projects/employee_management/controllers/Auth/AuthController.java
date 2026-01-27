package com.projects.employee_management.controllers.Auth;

import com.projects.employee_management.dto.*;
import com.projects.employee_management.repositories.UserRepository;
import com.projects.employee_management.services.RefreshTokenService;
import com.projects.employee_management.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private static final long ACCESS_TOKEN_EXPIRY = 3600L;

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty() || !user.get().passwordMatches(loginRequest.password(), passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválido");
        }

        var userDetails = userService.loadUserByUsername(loginRequest.email());
        var accessToken = generateAccessToken(userDetails);
        var refreshToken = refreshTokenService.generateRefreshToken(user.get().getId());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken,ACCESS_TOKEN_EXPIRY));
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO dto) {

        var userExistsInDB = userRepository.findByEmail(dto.email());

        if (userExistsInDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email já cadastrado.");
        }

        UserResponse created = userService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(uri).build();
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {

        String username = refreshTokenService.validateRefreshTokenAndGetUsername(request.refreshToken());
        UserDetails userDetails = userService.loadUserByUsername(username);

        var accessToken = generateAccessToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(request.refreshToken());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        refreshTokenService.deleteByToken(request.refreshToken());
        return ResponseEntity.noContent().build();
    }


    private String generateAccessToken( UserDetails userDetails) {

        var claims = JwtClaimsSet.builder()
                .issuer("apiemployee")
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRY))
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}

