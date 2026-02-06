package com.ups.portafolio.portafolio_backend.users.auth;



import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import com.ups.portafolio.portafolio_backend.users.security.JwtUtil;
import com.ups.portafolio.portafolio_backend.users.security.RegisterRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserEntity request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new RuntimeException("El email ya está registrado");
    }

    UserEntity user = UserEntity.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .active(true)
            .build();

    userRepository.save(user);
}


}
