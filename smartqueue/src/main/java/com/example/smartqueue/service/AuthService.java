package com.example.smartqueue.service;

import com.example.smartqueue.dto.LoginRequest;
import com.example.smartqueue.dto.LoginResponse;
import com.example.smartqueue.entity.User;
import com.example.smartqueue.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        // 2. Check password using BCrypt
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate JWT
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        // 4. Return token
        return new LoginResponse(
                token,
                "Bearer",
                user.getRole()
        );
    }
}