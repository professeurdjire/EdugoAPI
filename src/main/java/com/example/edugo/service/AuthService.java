package com.example.edugo.service;


import com.example.edugo.dto.EleveProfileResponse;
import com.example.edugo.dto.LoginRequest;
import com.example.edugo.dto.LoginResponse;
import com.example.edugo.dto.RegisterRequest;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Niveau;
import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import com.example.edugo.repository.ClasseRepository;
import com.example.edugo.repository.NiveauRepository;
import com.example.edugo.repository.UserRepository;
import com.example.edugo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClasseRepository classeRepository;
    private final NiveauRepository niveauRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final AdminNotificationService adminNotificationService;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Eleve eleve = new Eleve();
        eleve.setNom(request.getNom());
        eleve.setPrenom(request.getPrenom());
        eleve.setEmail(request.getEmail());
        eleve.setVille(request.getVille());
        eleve.setTelephone(request.getTelephone());
        eleve.setPhotoProfil(request.getPhotoProfil());
        eleve.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        eleve.setRole(Role.ELEVE);
        
        if (request.getClasseId() != null) {
            Classe classe = classeRepository.findById(request.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe non trouvée"));
            eleve.setClasse(classe);
        }

        if (request.getNiveauId() != null) {
           Niveau niveau = niveauRepository.findById(request.getNiveauId())
                    .orElseThrow(() -> new RuntimeException("niveau non trouvée"));
            eleve.setNiveau(niveau);
        }
        
        eleve.setPointAccumule(0);
        eleve = (Eleve) userRepository.save(eleve);
        
        // Envoyer un email de bienvenue
        try {
            emailService.sendWelcomeEmail(eleve.getEmail(), eleve.getNom(), eleve.getPrenom());
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas l'inscription
            System.err.println("Erreur lors de l'envoi de l'email de bienvenue: " + e.getMessage());
        }
        
        // Notifier les administrateurs du nouvel élève inscrit (OneSignal + Email)
        try {
            String titre = "👤 Nouvel élève inscrit";
            String message = String.format("Un nouvel élève vient de s'inscrire : %s %s", eleve.getPrenom(), eleve.getNom());
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("type", "NOUVEL_ELEVE");
            data.put("eleveId", eleve.getId());
            data.put("nom", eleve.getNom());
            data.put("prenom", eleve.getPrenom());
            data.put("email", eleve.getEmail());
            
            adminNotificationService.notifyAdmins(titre, message, data);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas l'inscription
            System.err.println("Erreur lors de la notification aux administrateurs: " + e.getMessage());
        }
        
        String token = jwtUtil.generateToken(eleve);
        String refreshToken = jwtUtil.generateRefreshToken(eleve);
        
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(eleve.getEmail())
                .nom(eleve.getNom())
                .prenom(eleve.getPrenom())
                .role(eleve.getRole().name())
                .id(eleve.getId())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole().name())
                .id(user.getId())
                .build();
    }
    
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Token invalide");
        }
        
        String email = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String newToken = jwtUtil.generateToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        
        return LoginResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole().name())
                .id(user.getId())
                .build();
    }

    public EleveProfileResponse getCurrentEleve(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Si l'utilisateur est un Eleve, cast explicite
        if (user instanceof Eleve) {
            Eleve eleve = (Eleve) user;
            return EleveProfileResponse.builder()
                    .id(eleve.getId())
                    .email(eleve.getEmail())
                    .nom(eleve.getNom())
                    .prenom(eleve.getPrenom())
                    .role(eleve.getRole().name())
                    .telephone(eleve.getTelephone())
                    .ville(eleve.getVille())
                    .photoProfil(eleve.getPhotoProfil())
                    .pointAccumule(eleve.getPointAccumule())
                    // Champs spécifiques avec les bons noms d'attributs
                    .classeId(eleve.getClasse() != null ? eleve.getClasse().getId() : null)
                    .classeNom(eleve.getClasse() != null ? eleve.getClasse().getNom() : null)
                    .niveauId(eleve.getNiveau() != null ? eleve.getNiveau().getId() : null)
                    .niveauNom(eleve.getNiveau() != null ? eleve.getNiveau().getNom() : null)
                    .build();
        } else {
            throw new RuntimeException("L'utilisateur n'est pas un élève");
        }
    }
}