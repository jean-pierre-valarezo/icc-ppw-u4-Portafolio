package com.ups.portafolio.portafolio_backend.users.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}