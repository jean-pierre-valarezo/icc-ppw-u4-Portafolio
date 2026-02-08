package com.ups.portafolio.portafolio_backend.users.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ups.portafolio.portafolio_backend.security.JwtUtil;
import com.ups.portafolio.portafolio_backend.users.dtos.AuthResponse;
import com.ups.portafolio.portafolio_backend.users.dtos.LoginRequest;
import com.ups.portafolio.portafolio_backend.users.dtos.RegisterRequest;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import com.ups.portafolio.portafolio_backend.users.roles.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya existe");
        }

        var user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER) 
                .active(true)
                .build();
        
        userRepository.save(user);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, 
        null, 
        userDetails.getAuthorities()
        );

        var jwtToken = jwtUtil.generateToken(authentication);

        return AuthResponse.builder()
        .token(jwtToken)
        .email(user.getEmail())
        .userId(user.getId())
        .role(user.getRole()) 
        .build();
    }

    public AuthResponse login(LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    String token = jwtUtil.generateToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return AuthResponse.builder()
            .token(token)
            .email(userDetails.getEmail())
            .userId(userDetails.getId())
            .role(
                userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse(null)
            )
            .build();
    }
}