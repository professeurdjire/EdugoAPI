package com.example.edugo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String motDePasse;

    public LoginRequest(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(@NotBlank String motDePasse) {
        this.motDePasse = motDePasse;
    }
}