package com.example.piproject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String token;
    private String username;
    private String email;
    private String role;

    public AuthResponseDTO(String token, String username, String email, String role) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
