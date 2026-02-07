package com.ups.portafolio.portafolio_backend.users.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private UUID userId;
    private String role;
}