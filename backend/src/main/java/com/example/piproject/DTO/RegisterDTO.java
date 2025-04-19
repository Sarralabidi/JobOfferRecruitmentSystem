package com.example.piproject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;  // New field
    private String lastName;   // New field
    private LocalDate birthdate; // New field
}
