package com.projects.employee_management.services;

import com.projects.employee_management.dto.RegisterDTO;
import com.projects.employee_management.dto.UserResponse;
import com.projects.employee_management.entities.Role;
import com.projects.employee_management.entities.User;
import com.projects.employee_management.repositories.RoleRepository;
import com.projects.employee_management.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse insert(RegisterDTO dto) {

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        Role role = roleRepository.findByAuthority("MANAGER")
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada"));

        user.addRole(role);

        user = userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));
    }
}
