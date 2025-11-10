package com.example.edugo.service;

import com.example.edugo.dto.LoginRequest;
import com.example.edugo.dto.RegisterRequest;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import com.example.edugo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Enregistrement d'un nouvel élève
     */
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Eleve eleve = new Eleve();
        eleve.setNom(request.getNom());
        eleve.setPrenom(request.getPrenom());
        eleve.setEmail(request.getEmail());
        eleve.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        eleve.setRole(Role.ELEVE); // 👈 par défaut

        return userRepository.save(eleve);
    }

    /**
     * Authentification (admin ou élève)
     */
    public User authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        return userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
