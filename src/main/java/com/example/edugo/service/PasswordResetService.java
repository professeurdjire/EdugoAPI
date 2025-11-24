package com.example.edugo.service;

import com.example.edugo.dto.PasswordResetResponse;
import com.example.edugo.entity.PasswordResetToken;
import com.example.edugo.entity.User;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.PasswordResetTokenRepository;
import com.example.edugo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.password-reset.token-expiration-hours:24}")
    private int tokenExpirationHours;

    /**
     * Génère un token de réinitialisation et envoie un email à l'utilisateur
     */
    @Transactional
    public PasswordResetResponse requestPasswordReset(String email) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun compte trouvé avec cet email"));

        // Invalider les anciens tokens non utilisés pour cet email
        tokenRepository.invalidateTokensByEmail(email);

        // Générer un nouveau token
        String token = generateSecureToken();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(tokenExpirationHours);

        // Créer et sauvegarder le token
        PasswordResetToken resetToken = new PasswordResetToken(token, email, expirationDate);
        tokenRepository.save(resetToken);

        // Envoyer l'email de réinitialisation
        try {
            emailService.sendPasswordResetEmail(email, user.getNom(), user.getPrenom(), token);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas la création du token
            System.err.println("Erreur lors de l'envoi de l'email de réinitialisation: " + e.getMessage());
            // On continue quand même pour ne pas révéler si l'email existe ou non
        }

        // Toujours retourner un succès pour des raisons de sécurité
        // (ne pas révéler si l'email existe ou non)
        return PasswordResetResponse.builder()
                .message("Si un compte existe avec cet email, un lien de réinitialisation a été envoyé")
                .success(true)
                .email(email)
                .build();
    }

    /**
     * Vérifie si un token est valide
     */
    @Transactional(readOnly = true)
    public PasswordResetResponse verifyToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token invalide ou expiré"));

        if (!resetToken.isValid()) {
            throw new ResourceNotFoundException("Token invalide ou expiré");
        }

        return PasswordResetResponse.builder()
                .message("Token valide")
                .success(true)
                .email(resetToken.getEmail())
                .build();
    }

    /**
     * Réinitialise le mot de passe avec un token valide
     */
    @Transactional
    public PasswordResetResponse resetPassword(String token, String nouveauMotDePasse) {
        // Vérifier le token
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token invalide ou expiré"));

        if (!resetToken.isValid()) {
            throw new ResourceNotFoundException("Token invalide ou expiré");
        }

        // Trouver l'utilisateur
        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // Mettre à jour le mot de passe
        user.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        userRepository.save(user);

        // Marquer le token comme utilisé
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        // Invalider tous les autres tokens pour cet email
        tokenRepository.invalidateTokensByEmail(resetToken.getEmail());

        // Envoyer un email de confirmation
        try {
            emailService.sendPasswordResetConfirmationEmail(
                    user.getEmail(), 
                    user.getNom(), 
                    user.getPrenom()
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de confirmation: " + e.getMessage());
        }

        return PasswordResetResponse.builder()
                .message("Mot de passe réinitialisé avec succès")
                .success(true)
                .email(user.getEmail())
                .build();
    }

    /**
     * Génère un token sécurisé aléatoire
     */
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Nettoie les tokens expirés (peut être appelé par un job planifié)
     */
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}

