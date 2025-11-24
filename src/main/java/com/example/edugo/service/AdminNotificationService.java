package com.example.edugo.service;

import com.example.edugo.entity.User;
import com.example.edugo.entity.Role;
import com.example.edugo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service pour envoyer des notifications aux administrateurs
 * Combine OneSignal (push notifications) et Email
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminNotificationService {

    private final OneSignalService oneSignalService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    /**
     * Envoie une notification aux administrateurs (OneSignal + Email)
     */
    public void notifyAdmins(String title, String message, Map<String, Object> data) {
        try {
            // Récupérer tous les administrateurs actifs
            List<User> admins = userRepository.findByRole(Role.ADMIN);
            List<User> activeAdmins = admins.stream()
                    .filter(User::getEstActive)
                    .toList();

            if (activeAdmins.isEmpty()) {
                log.warn("Aucun administrateur actif trouvé pour l'envoi de notification");
                return;
            }

            // Envoyer notification OneSignal à tous les admins
            oneSignalService.sendNotificationToRole("ADMIN", title, message, data);

            // Envoyer email à chaque administrateur
            for (User admin : activeAdmins) {
                if (admin.getEmail() != null && !admin.getEmail().isEmpty()) {
                    try {
                        emailService.sendHtmlEmail(
                            admin.getEmail(),
                            title,
                            generateAdminNotificationEmailHtml(title, message, data)
                        );
                        log.info("Email de notification envoyé à l'admin: {}", admin.getEmail());
                    } catch (Exception e) {
                        log.error("Erreur lors de l'envoi d'email à l'admin {}: {}", admin.getEmail(), e.getMessage());
                    }
                }
            }

            log.info("Notifications envoyées à {} administrateur(s)", activeAdmins.size());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notifications aux administrateurs: {}", e.getMessage(), e);
        }
    }

    /**
     * Envoie une notification à un administrateur spécifique (OneSignal + Email)
     */
    public void notifyAdmin(Long adminId, String title, String message, Map<String, Object> data) {
        try {
            User admin = userRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Administrateur introuvable: " + adminId));

            if (!admin.getEstActive()) {
                log.warn("L'administrateur {} n'est pas actif", adminId);
                return;
            }

            // Envoyer notification OneSignal
            oneSignalService.sendNotificationToUser(adminId, "ADMIN", title, message, data);

            // Envoyer email
            if (admin.getEmail() != null && !admin.getEmail().isEmpty()) {
                emailService.sendHtmlEmail(
                    admin.getEmail(),
                    title,
                    generateAdminNotificationEmailHtml(title, message, data)
                );
                log.info("Email de notification envoyé à l'admin: {}", admin.getEmail());
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification à l'admin {}: {}", adminId, e.getMessage(), e);
        }
    }

    /**
     * Génère le contenu HTML de l'email de notification pour les admins
     */
    private String generateAdminNotificationEmailHtml(String title, String message, Map<String, Object> data) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<meta charset='UTF-8'>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }")
            .append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }")
            .append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }")
            .append(".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }")
            .append(".notification-box { background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }")
            .append(".title { color: #667eea; font-size: 24px; margin-bottom: 10px; }")
            .append(".message { font-size: 16px; color: #555; margin-bottom: 20px; }")
            .append(".data-section { background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin-top: 20px; }")
            .append(".data-item { margin: 5px 0; }")
            .append(".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }")
            .append(".button { display: inline-block; padding: 12px 30px; background-color: #667eea; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class='container'>")
            .append("<div class='header'>")
            .append("<h1>🔔 Notification EDUGO</h1>")
            .append("</div>")
            .append("<div class='content'>")
            .append("<div class='notification-box'>")
            .append("<h2 class='title'>").append(title).append("</h2>")
            .append("<p class='message'>").append(message.replace("\n", "<br>")).append("</p>");

        // Ajouter les données additionnelles si présentes
        if (data != null && !data.isEmpty()) {
            html.append("<div class='data-section'>")
                .append("<h3 style='margin-top: 0;'>Détails :</h3>");
            
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                html.append("<div class='data-item'>")
                    .append("<strong>").append(entry.getKey()).append(":</strong> ")
                    .append(entry.getValue())
                    .append("</div>");
            }
            
            html.append("</div>");
        }

        html.append("</div>")
            .append("<p style='margin-top: 20px;'>Connectez-vous à la plateforme pour plus de détails.</p>")
            .append("<a href='https://admin.edugo.ml' class='button'>Accéder à l'administration</a>")
            .append("<div class='footer'>")
            .append("<p>© 2024 EDUGO - Plateforme éducative pour le Mali</p>")
            .append("<p>Email: support@edugo.ml | Site: https://www.edugo.ml</p>")
            .append("</div>")
            .append("</div>")
            .append("</div>")
            .append("</body>")
            .append("</html>");

        return html.toString();
    }
}

