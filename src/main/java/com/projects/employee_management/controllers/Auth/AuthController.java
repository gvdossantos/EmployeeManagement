package com.projects.employee_management.controllers.Auth;

import com.projects.employee_management.dto.LoginRequest;
import com.projects.employee_management.dto.LoginResponse;
import com.projects.employee_management.dto.RegisterDTO;
import com.projects.employee_management.dto.UserResponse;
import com.projects.employee_management.repositories.UserRepository;
import com.projects.employee_management.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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


    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty() ||
                !user.get().passwordMatches(loginRequest.password(), passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválido");
        }

        var expiresIn = 3600L;
        var claims = JwtClaimsSet.builder()
                .issuer("apiemployee")
                .subject(user.get().getId().toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("roles", user.get().getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList())
                .build();


        var JwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(JwtValue, expiresIn));
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


}

