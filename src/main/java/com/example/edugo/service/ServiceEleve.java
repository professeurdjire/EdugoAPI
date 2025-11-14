package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.entity.Role;
import com.example.edugo.entity.User;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceEleve {

    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;
    private final NiveauRepository niveauRepository;
    private final ExerciceRepository exerciceRepository;
    private final DefiRepository defiRepository;
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final ProgressionRepository progressionRepository;
    private final EleveDefiRepository eleveDefiRepository;
    private final FaireExerciceRepository faireExerciceRepository;
    private final BadgeRepository badgeRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== GESTION PROFIL ÉLÈVE ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public Eleve getProfil(Long eleveId) {
        return eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Eleve updateProfil(Long eleveId, Eleve eleveDetails) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        eleve.setNom(eleveDetails.getNom());
        eleve.setPrenom(eleveDetails.getPrenom());
        eleve.setEmail(eleveDetails.getEmail());
        eleve.setPhotoProfil(eleveDetails.getPhotoProfil());
        eleve.setVille(eleveDetails.getVille());
        
        if (eleveDetails.getClasse() != null) {
            Classe classe = classeRepository.findById(eleveDetails.getClasse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", eleveDetails.getClasse().getId()));
            eleve.setClasse(classe);
        }

        if (eleveDetails.getNiveau() != null) {
            Niveau niveau = niveauRepository.findById(eleveDetails.getClasse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", eleveDetails.getNiveau().getId()));
            eleve.setNiveau(niveau);
        }

        return eleveRepository.save(eleve);
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public void changePassword(Long eleveId, String oldPassword, String newPassword) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, eleve.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        
        eleve.setMotDePasse(passwordEncoder.encode(newPassword));
        eleveRepository.save(eleve);
    }

    // ==================== GESTION POINTS ET BADGES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Eleve ajouterPoints(Long eleveId, Integer points) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        eleve.setPointAccumule(eleve.getPointAccumule() + points);
        return eleveRepository.save(eleve);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public Integer getPoints(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        return eleve.getPointAccumule();
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Badge> getBadges(Long eleveId) {
        // Cette méthode devrait récupérer les badges gagnés par l'élève
        // Pour l'instant, retournons tous les badges disponibles
        return badgeRepository.findAll();
    }

    // ==================== GESTION EXERCICES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Exercice> getExercicesDisponibles(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Retourner les exercices actifs
        return exerciceRepository.findByActiveTrue();
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public FaireExercice soumettreExercice(Long eleveId, Long exerciceId, String reponse) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercice", exerciceId));
        
        FaireExercice faireExercice = new FaireExercice();
        faireExercice.setEleve(eleve);
        faireExercice.setExercice(exercice);
        // Adapter aux champs réels: pas de reponse/ dateSoumission
        // On marque comme ENCOURS et date du jour via dateExercice
        faireExercice.setStatut(StatutExercice.ENCOURS);
        
        return faireExerciceRepository.save(faireExercice);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<FaireExercice> getHistoriqueExercices(Long eleveId) {
        return faireExerciceRepository.findByEleveId(eleveId);
    }

    // ==================== GESTION DÉFIS ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Defi> getDefisDisponibles(Long eleveId) {
        return defiRepository.findAll();
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public EleveDefi participerDefi(Long eleveId, Long defiId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Defi defi = defiRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", defiId));
        
        // Vérifier si l'élève n'a pas déjà participé à ce défi
        Optional<EleveDefi> existingParticipation = eleveDefiRepository
                .findByEleveIdAndDefiId(eleveId, defiId);
        
        if (existingParticipation.isPresent()) {
            throw new RuntimeException("Vous avez déjà participé à ce défi");
        }
        
        EleveDefi eleveDefi = new EleveDefi();
        eleveDefi.setEleve(eleve);
        eleveDefi.setDefi(defi);
        eleveDefi.setDateParticipation(LocalDateTime.now());
        eleveDefi.setStatut("EN_COURS");
        
        return eleveDefiRepository.save(eleveDefi);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<EleveDefi> getDefisParticipes(Long eleveId) {
        return eleveDefiRepository.findByEleveId(eleveId);
    }

    // ==================== GESTION CHALLENGES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Challenge> getChallengesDisponibles(Long eleveId) {
        return challengeRepository.findAll();
    }

    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Participation participerChallenge(Long eleveId, Long challengeId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", challengeId));
        
        Participation participation = new Participation();
        participation.setEleve(eleve);
        participation.setChallenge(challenge);
        participation.setDateParticipation(LocalDateTime.now());
        participation.setStatut("ACTIF");
        
        return participationRepository.save(participation);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Participation> getChallengesParticipes(Long eleveId) {
        return participationRepository.findByEleveId(eleveId);
    }

    // ==================== GESTION PROGRESSION ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Progression updateProgression(Long eleveId, Long livreId, Integer pageActuelle, Integer pourcentageCompletion) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        // Chercher une progression existante ou en créer une nouvelle
        Optional<Progression> existingProgression = progressionRepository
                .findByEleveIdAndLivreId(eleveId, livreId);
        
        Progression progression;
        if (existingProgression.isPresent()) {
            progression = existingProgression.get();
        } else {
            progression = new Progression();
            progression.setEleve(eleve);
            // Note: Il faudrait récupérer le livre par son ID
        }
        
        progression.setPageActuelle(pageActuelle);
        progression.setPourcentageCompletion(pourcentageCompletion);
        progression.setDateMiseAJour(LocalDateTime.now());
        
        return progressionRepository.save(progression);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Progression> getProgression(Long eleveId) {
        return progressionRepository.findByEleveId(eleveId);
    }

    // ==================== STATISTIQUES ÉLÈVE ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public com.example.edugo.dto.StatistiquesEleveResponse getStatistiques(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        Long exercicesRealises = faireExerciceRepository.countByEleveId(eleveId);
        Long defisParticipes = eleveDefiRepository.countByEleveId(eleveId);
        Long challengesParticipes = participationRepository.countByEleveId(eleveId);

        return new com.example.edugo.dto.StatistiquesEleveResponse(
                eleve.getId(),
                eleve.getNom(),
                eleve.getPrenom(),
                eleve.getPointAccumule(),
                exercicesRealises,
                defisParticipes,
                challengesParticipes
        );
    }

    // ==================== GESTION CLASSE ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Eleve> getCamaradesClasse(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        if (eleve.getClasse() == null) {
            return List.of();
        }
        
        return eleveRepository.findByClasseId(eleve.getClasse().getId());
    }
}
