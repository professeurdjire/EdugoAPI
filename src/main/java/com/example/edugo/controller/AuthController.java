package com.example.edugo.controller;


import com.example.edugo.dto.LoginRequest;
import com.example.edugo.dto.LoginResponse;
import com.example.edugo.dto.RegisterRequest;
import com.example.edugo.entity.User;
import com.example.edugo.service.AuthService;
import com.example.edugo.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.authenticate(request);
        String jwt = jwtUtil.generateToken(user);

        LoginResponse response = LoginResponse.builder()
                .token(jwt)
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Pour JWT stateless, le logout est géré côté client
        return ResponseEntity.ok("Déconnexion réussie");
    }
}
