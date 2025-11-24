package com.example.edugo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:no-reply@edugo.ml}")
    private String fromEmail;

    @Value("${app.password-reset.base-url:https://www.edugo.ml}")
    private String baseUrl;

    /**
     * Envoie un email simple (texte)
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas l'inscription
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

    /**
     * Envoie un email HTML
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML
            
            mailSender.send(message);
        } catch (MessagingException e) {
            // Log l'erreur mais ne bloque pas l'inscription
            System.err.println("Erreur lors de l'envoi de l'email HTML: " + e.getMessage());
        }
    }

    /**
     * Envoie un email de bienvenue lors de l'inscription
     */
    public void sendWelcomeEmail(String email, String nom, String prenom) {
        String subject = "Bienvenue sur la plateforme EDUGO ! 🎓";
        String htmlContent = generateWelcomeEmailContent(nom, prenom);
        
        sendHtmlEmail(email, subject, htmlContent);
    }

    /**
     * Génère le contenu HTML de l'email de bienvenue
     */
    private String generateWelcomeEmailContent(String nom, String prenom) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎓 Bienvenue sur EDUGO !</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Bonjour " + (prenom != null ? prenom : "") + " " + (nom != null ? nom : "") + ",</h2>" +
                "<p>Nous sommes ravis de vous accueillir sur la plateforme EDUGO, votre compagnon d'apprentissage numérique !</p>" +
                "<p>Vous pouvez maintenant :</p>" +
                "<ul>" +
                "<li>📚 Accéder à une bibliothèque de livres éducatifs</li>" +
                "<li>✏️ Faire des exercices interactifs</li>" +
                "<li>🎯 Participer à des défis et challenges</li>" +
                "<li>🏆 Gagner des points et des badges</li>" +
                "<li>📱 Convertir vos points en forfaits data internet</li>" +
                "</ul>" +
                "<p>Commencez votre parcours d'apprentissage dès maintenant !</p>" +
                "<a href='https://www.edugo.ml' class='button'>Accéder à la plateforme</a>" +
                "<p style='margin-top: 30px;'>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p>Bonne chance dans votre apprentissage !</p>" +
                "<p><strong>L'équipe EDUGO</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2024 EDUGO - Plateforme éducative pour le Mali</p>" +
                "<p>Email: support@edugo.ml | Site: https://www.edugo.ml</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Envoie un email de réinitialisation de mot de passe
     */
    public void sendPasswordResetEmail(String email, String nom, String prenom, String token) {
        String subject = "Réinitialisation de votre mot de passe - EDUGO 🔐";
        String htmlContent = generatePasswordResetEmailContent(nom, prenom, token);
        
        sendHtmlEmail(email, subject, htmlContent);
    }

    /**
     * Génère le contenu HTML de l'email de réinitialisation de mot de passe
     */
    private String generatePasswordResetEmailContent(String nom, String prenom, String token) {
        // URL du frontend pour la réinitialisation (utilise la configuration)
        String resetUrl = baseUrl + "/reset-password?token=" + token;
        
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #FF6B6B; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #FF6B6B; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                ".warning { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }" +
                ".token-box { background-color: #e9ecef; padding: 15px; border-radius: 5px; margin: 20px 0; word-break: break-all; font-family: monospace; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🔐 Réinitialisation de mot de passe</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Bonjour " + (prenom != null ? prenom : "") + " " + (nom != null ? nom : "") + ",</h2>" +
                "<p>Vous avez demandé à réinitialiser votre mot de passe sur la plateforme EDUGO.</p>" +
                "<p>Cliquez sur le bouton ci-dessous pour réinitialiser votre mot de passe :</p>" +
                "<a href='" + resetUrl + "' class='button'>Réinitialiser mon mot de passe</a>" +
                "<p style='margin-top: 30px;'>Ou copiez et collez ce lien dans votre navigateur :</p>" +
                "<div class='token-box'>" + resetUrl + "</div>" +
                "<div class='warning'>" +
                "<p><strong>⚠️ Important :</strong></p>" +
                "<ul>" +
                "<li>Ce lien est valide pendant 24 heures</li>" +
                "<li>Si vous n'avez pas demandé cette réinitialisation, ignorez cet email</li>" +
                "<li>Ne partagez jamais ce lien avec quelqu'un d'autre</li>" +
                "</ul>" +
                "</div>" +
                "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p><strong>L'équipe EDUGO</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2024 EDUGO - Plateforme éducative pour le Mali</p>" +
                "<p>Email: support@edugo.ml | Site: https://www.edugo.ml</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Envoie un email de confirmation après réinitialisation du mot de passe
     */
    public void sendPasswordResetConfirmationEmail(String email, String nom, String prenom) {
        String subject = "Mot de passe modifié avec succès - EDUGO ✅";
        String htmlContent = generatePasswordResetConfirmationEmailContent(nom, prenom);
        
        sendHtmlEmail(email, subject, htmlContent);
    }

    /**
     * Génère le contenu HTML de l'email de confirmation de réinitialisation
     */
    private String generatePasswordResetConfirmationEmailContent(String nom, String prenom) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                ".warning { background-color: #d1ecf1; border-left: 4px solid #0c5460; padding: 15px; margin: 20px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>✅ Mot de passe modifié</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Bonjour " + (prenom != null ? prenom : "") + " " + (nom != null ? nom : "") + ",</h2>" +
                "<p>Votre mot de passe a été modifié avec succès sur la plateforme EDUGO.</p>" +
                "<div class='warning'>" +
                "<p><strong>🔒 Sécurité :</strong></p>" +
                "<ul>" +
                "<li>Si vous n'avez pas effectué cette modification, contactez-nous immédiatement</li>" +
                "<li>Assurez-vous d'utiliser un mot de passe fort et unique</li>" +
                "<li>Ne partagez jamais votre mot de passe</li>" +
                "</ul>" +
                "</div>" +
                "<p>Vous pouvez maintenant vous connecter avec votre nouveau mot de passe.</p>" +
                "<a href='https://www.edugo.ml/login' class='button'>Se connecter</a>" +
                "<p style='margin-top: 30px;'>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p><strong>L'équipe EDUGO</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2024 EDUGO - Plateforme éducative pour le Mali</p>" +
                "<p>Email: support@edugo.ml | Site: https://www.edugo.ml</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

