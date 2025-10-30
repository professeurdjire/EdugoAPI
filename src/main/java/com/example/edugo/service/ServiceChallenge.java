package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceChallenge {

    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final EleveRepository eleveRepository;
    private final BadgeRepository badgeRepository;

    // ==================== CRUD CHALLENGES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Challenge updateChallenge(Long id, Challenge challengeDetails) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
        
        challenge.setTitre(challengeDetails.getTitre());
        challenge.setDescription(challengeDetails.getDescription());
        challenge.setDateDebut(challengeDetails.getDateDebut());
        challenge.setDateFin(challengeDetails.getDateFin());
        challenge.setRewardMode(challengeDetails.getRewardMode());
        challenge.setTypeChallenge(challengeDetails.getTypeChallenge());
        
        return challengeRepository.save(challenge);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
        challengeRepository.delete(challenge);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
    }

    // ==================== CHALLENGES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<Challenge> getChallengesDisponibles(Long eleveId) {
        LocalDateTime maintenant = LocalDateTime.now();
        return challengeRepository.findActiveChallenges(maintenant);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Challenge> getChallengesActifs() {
        LocalDateTime maintenant = LocalDateTime.now();
        return challengeRepository.findActiveChallenges(maintenant);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Challenge> getChallengesByType(TypeChallenge typeChallenge) {
        return challengeRepository.findByTypeChallenge(typeChallenge);
    }

    // ==================== PARTICIPATION AUX CHALLENGES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    @Transactional
    public Participation participerChallenge(Long eleveId, Long challengeId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));
        
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", challengeId));
        
        // Vérifier si le challenge est encore actif
        LocalDateTime maintenant = LocalDateTime.now();
        if (maintenant.isBefore(challenge.getDateDebut()) || maintenant.isAfter(challenge.getDateFin())) {
            throw new RuntimeException("Ce challenge n'est pas actuellement disponible");
        }
        
        // Vérifier si l'élève n'a pas déjà participé
        if (participationRepository.existsByEleveIdAndChallengeId(eleveId, challengeId)) {
            throw new RuntimeException("Vous participez déjà à ce challenge");
        }
        
        Participation participation = new Participation();
        participation.setEleve(eleve);
        participation.setChallenge(challenge);
        participation.setDateParticipation(LocalDateTime.now());
        participation.setaParticiper(true);
        
        return participationRepository.save(participation);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public List<Participation> getChallengesParticipes(Long eleveId) {
        return participationRepository.findByEleveId(eleveId);
    }

    @PreAuthorize("hasRole('ELEVE')")
    public Participation getParticipationChallenge(Long eleveId, Long challengeId) {
        return participationRepository.findByEleveIdAndChallengeId(eleveId, challengeId)
                .orElse(null);
    }

    // ==================== GESTION DES PARTICIPATIONS ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Participation validerParticipation(Long participationId, String commentaire) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation", participationId));
        // pas de statut/commentaire/dateValidation, on peut augmenter le score/rang si besoin
        
        return participationRepository.save(participation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Participation disqualifierParticipation(Long participationId, String raison) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation", participationId));
        // pas de champs statut/commentaire/dateValidation
        
        return participationRepository.save(participation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Participation> getParticipationsChallenge(Long challengeId) {
        return participationRepository.findByChallengeId(challengeId);
    }

    // ==================== STATISTIQUES CHALLENGES ====================
    
    public Object getStatistiquesChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", challengeId));
        
        List<Participation> participations = participationRepository.findByChallengeId(challengeId);
        
        return new Object() {
            public final Long challengeId = challenge.getId();
            public final String titre = challenge.getTitre();
            public final Integer nombreParticipations = participations.size();
            public final Integer nombreValidations = (int) participations.stream()
                    .filter(p -> "VALIDE".equals(p.getStatut()))
                    .count();
            public final Integer nombreDisqualifications = (int) participations.stream()
                    .filter(p -> "DISQUALIFIE".equals(p.getStatut()))
                    .count();
            public final Double tauxParticipation = participations.isEmpty() ? 0.0 :
                    (double) participations.size() / eleveRepository.count() * 100;
        };
    }

    // ==================== CLASSEMENT CHALLENGES ====================
    
    public List<Object> getLeaderboardChallenge(Long challengeId) {
        List<Participation> participationsValidees = participationRepository.findByChallengeId(challengeId);
        
        return participationsValidees.stream()
                .sorted((p1, p2) -> p1.getDateParticipation().compareTo(p2.getDateParticipation()))
                .limit(10)
                .map(participation -> new Object() {
                    public final Long eleveId = participation.getEleve().getId();
                    public final String nom = participation.getEleve().getNom();
                    public final String prenom = participation.getEleve().getPrenom();
                    public final LocalDateTime dateParticipation = participation.getDateParticipation();
                    public final Integer points = participation.getEleve().getPointAccumule();
                })
                .collect(java.util.stream.Collectors.toList());
    }

    // ==================== CHALLENGES EXPIRÉS ET ARCHIVES ====================
    
    public List<Challenge> getChallengesExpires() {
        LocalDateTime maintenant = LocalDateTime.now();
        return challengeRepository.findByDateFinBefore(maintenant);
    }

    public List<Challenge> getChallengesAVenir() {
        LocalDateTime maintenant = LocalDateTime.now();
        return challengeRepository.findByDateDebutAfter(maintenant);
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    public List<Challenge> searchChallengesByTitre(String titre) {
        return challengeRepository.findByTitreContainingIgnoreCase(titre);
    }

    public List<Challenge> getChallengesByRewardMode(String rewardMode) {
        return challengeRepository.findByRewardMode(rewardMode);
    }
}
