package com.projects.employee_management.config;

import com.projects.employee_management.entities.Role;
import com.projects.employee_management.entities.User;
import com.projects.employee_management.repositories.RoleRepository;
import com.projects.employee_management.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
public class AdminSetupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@123.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("1234567"));

            Role adminRole = roleRepository.findByAuthority("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN não encontrada"));

            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            System.out.println("Admin inicial criado");
            System.out.println("Email: " + adminEmail);
            System.out.println("Senha: " + 1234567);
        }
    }
}

