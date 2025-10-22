package com.example.edugo.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private String nom;
    private String prenom;
    private String role;
}
