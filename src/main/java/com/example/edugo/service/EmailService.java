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
}

