package com.ups.portafolio.portafolio_backend.users.security;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String role;

}
