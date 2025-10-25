package com.example.edugo.config;

import com.example.edugo.entity.Admin;
import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import com.example.edugo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitialisation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Vérifie si un admin existe déjà
        if (userRepository.findByEmail("admin@edugo.com").isEmpty()) {

            User admin = new Admin();
            admin.setNom("Admin");
            admin.setPrenom("Principal");
            admin.setEmail("admin@edugo.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123")); // mot de passe par défaut
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Administrateur par défaut créé : admin@edugo.com / admin123");
        } else {
            System.out.println("Administrateur déjà présent, aucune création nécessaire.");
        }
    }
}

