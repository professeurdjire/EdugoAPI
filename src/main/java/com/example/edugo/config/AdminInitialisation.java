package com.example.edugo.config;

import com.example.edugo.entity.Admin;
import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import com.example.edugo.repository.UserRepository;
import com.example.edugo.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitialisation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    private static final Logger log = LoggerFactory.getLogger(AdminInitialisation.class);

    @Override
    public void run(String... args) {
        // Vérifie si un admin existe déjà dans AdminRepository ou UserRepository
        try {
            boolean exists = adminRepository.findByEmail("admin@edugo.com").isPresent()
                    || userRepository.findByEmail("admin@edugo.com").isPresent();

            if (!exists) {
                User admin = new com.example.edugo.entity.Admin();
                admin.setNom("Admin");
                admin.setPrenom("Principal");
                admin.setEmail("admin@edugo.com");
                admin.setMotDePasse(passwordEncoder.encode("admin123")); // mot de passe par défaut
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                log.info("Administrateur par défaut créé : admin@edugo.com / admin123");
            } else {
                log.info("Administrateur déjà présent, aucune création nécessaire.");
            }
        } catch (Exception e) {
            // Ne pas faire planter le démarrage pour une erreur d'init
            log.error("Erreur lors de l'initialisation de l'administrateur par défaut", e);
        }
    }
}

