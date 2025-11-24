package com.example.edugo.service;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.*;
import com.example.edugo.dto.ChallengeRequest;
import com.example.edugo.dto.ChallengeResponse;
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
    private final ClasseRepository classeRepository;
    private final NiveauRepository niveauRepository;
    private final QuestionRepository questionRepository;
    private final OneSignalService oneSignalService;
    private final AdminNotificationService adminNotificationService;

    // ==================== CRUD CHALLENGES ====================
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ChallengeResponse updateChallenge(Long id, ChallengeRequest dto) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", id));
        challenge.setTitre(dto.getTitre());
        challenge.setDescription(dto.getDescription());
        challenge.setDateDebut(dto.getDateDebut());
        challenge.setDateFin(dto.getDateFin());
        challenge.setPoints(dto.getPoints());
        challenge.setRewardMode(dto.getTheme());
        if (dto.getTypeChallenge() != null) {
            challenge.setTypeChallenge(TypeChallenge.valueOf(dto.getTypeChallenge()));
        }
        if (dto.getClasseId() != null) {
            Classe classe = classeRepository.findById(dto.getClasseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", dto.getClasseId()));
            challenge.setClasse(classe);
        }
        if (dto.getNiveauId() != null) {
            Niveau niveau = niveauRepository.findById(dto.getNiveauId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", dto.getNiveauId()));
            challenge.setNiveau(niveau);
        }
        Challenge saved = challengeRepository.save(challenge);
        return toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ChallengeResponse createChallenge(ChallengeRequest dto) {
        Challenge challenge = new Challenge();
        challenge.setTitre(dto.getTitre());
        challenge.setDescription(dto.getDescription());
        challenge.setDateDebut(dto.getDateDebut());
        challenge.setDateFin(dto.getDateFin());
        challenge.setPoints(dto.getPoints());
        // dto.theme not directly mapped in entity; using rewardMode
        challenge.setRewardMode(dto.getTheme());
        if (dto.getTypeChallenge() != null) {
            challenge.setTypeChallenge(TypeChallenge.valueOf(dto.getTypeChallenge()));
        }
        if (dto.getClasseId() != null) {
            Classe classe = classeRepository.findById(dto.getClasseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classe", dto.getClasseId()));
            challenge.setClasse(classe);
        }
        if (dto.getNiveauId() != null) {
            Niveau niveau = niveauRepository.findById(dto.getNiveauId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", dto.getNiveauId()));
            challenge.setNiveau(niveau);
        }
        
        // Sauvegarder le challenge
        Challenge saved = challengeRepository.save(challenge);
        
        // Notifier les élèves qu'un nouveau challenge est disponible
        envoyerNotificationNouveauChallenge(saved);
        
        return toResponse(saved);
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

    public ChallengeResponse toResponse(Challenge challenge) {
        return toResponse(challenge, null);
    }
    
    public ChallengeResponse toResponse(Challenge challenge, java.util.Map<Long, Integer> questionCountsMap) {
        ChallengeResponse res = new ChallengeResponse();
        res.setId(challenge.getId());
        res.setTitre(challenge.getTitre());
        res.setDescription(challenge.getDescription());
        res.setPoints(challenge.getPoints());
        res.setTheme(challenge.getRewardMode());
        res.setDateDebut(challenge.getDateDebut());
        res.setDateFin(challenge.getDateFin());
        // Compter les questions associées au challenge (utiliser le map si fourni, sinon requête directe)
        if (questionCountsMap != null && questionCountsMap.containsKey(challenge.getId())) {
            res.setNombreQuestions(questionCountsMap.get(challenge.getId()));
        } else {
            Long count = questionRepository.countByChallengeId(challenge.getId());
            res.setNombreQuestions(count != null ? count.intValue() : 0);
        }
        return res;
    }

    // ==================== CHALLENGES POUR ÉLÈVES ====================
    
    @PreAuthorize("hasRole('ELEVE')")
    public List<ChallengeResponse> getChallengesDisponibles(Long eleveId) {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new ResourceNotFoundException("Élève", eleveId));

        Classe classeEleve = eleve.getClasse();
        Niveau niveauEleve = eleve.getNiveau();
        LocalDateTime maintenant = LocalDateTime.now();

        List<Challenge> challengesActifs = challengeRepository.findActiveChallenges(maintenant);

        // Filtrer les challenges
        List<Challenge> challengesFiltres = challengesActifs.stream()
                .filter(challenge -> {
                    if (challenge.getTypeChallenge() == TypeChallenge.INTERCLASSE) {
                        return challenge.getClasse() != null
                                && classeEleve != null
                                && challenge.getClasse().getId().equals(classeEleve.getId());
                    }
                    if (challenge.getTypeChallenge() == TypeChallenge.INTERNIVEAU) {
                        return challenge.getNiveau() != null
                                && niveauEleve != null
                                && challenge.getNiveau().getId().equals(niveauEleve.getId());
                    }
                    // autres types de challenge: visibles pour tous par défaut
                    return true;
                })
                .filter(challenge -> !participationRepository.existsByEleveIdAndChallengeId(eleveId, challenge.getId()))
                .toList();
        
        // Optimisation : compter les questions pour tous les challenges en une seule requête
        java.util.Map<Long, Integer> questionCountsMap = new java.util.HashMap<>();
        if (!challengesFiltres.isEmpty()) {
            List<Long> challengeIds = challengesFiltres.stream().map(Challenge::getId).toList();
            List<Object[]> counts = questionRepository.countByChallengeIds(challengeIds);
            for (Object[] count : counts) {
                Long challengeId = (Long) count[0];
                Long countValue = (Long) count[1];
                questionCountsMap.put(challengeId, countValue.intValue());
            }
        }
        
        // Mapper les challenges avec les comptes pré-calculés
        final java.util.Map<Long, Integer> finalCountsMap = questionCountsMap;
        return challengesFiltres.stream()
                .map(challenge -> toResponse(challenge, finalCountsMap))
                .toList();
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
        
        // Créer la participation avec les valeurs initiales
        Participation participation = new Participation();
        participation.setEleve(eleve);
        participation.setChallenge(challenge);
        participation.setDateParticipation(LocalDateTime.now());
        participation.setStatut("EN_COURS"); // Statut initial : en cours
        participation.setaParticiper(true);
        participation.setScore(0); // Score initial : 0
        participation.setTempsPasse(0); // Temps passé initial : 0 secondes
        participation.setRang(null); // Pas encore de classement
        
        Participation saved = participationRepository.save(participation);
        
        // Notifier les administrateurs de la nouvelle participation (OneSignal + Email)
        try {
            String titre = "🎯 Nouvelle participation à un challenge";
            String message = String.format("L'élève %s %s vient de participer au challenge : %s", 
                eleve.getPrenom(), eleve.getNom(), challenge.getTitre());
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("type", "PARTICIPATION_CHALLENGE");
            data.put("challengeId", challenge.getId());
            data.put("eleveId", eleve.getId());
            data.put("participationId", saved.getId());
            data.put("eleveNom", eleve.getNom());
            data.put("elevePrenom", eleve.getPrenom());
            data.put("challengeTitre", challenge.getTitre());
            
            adminNotificationService.notifyAdmins(titre, message, data);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas la participation
            System.err.println("Erreur lors de la notification aux administrateurs: " + e.getMessage());
        }
        
        return saved;
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
    
    @PreAuthorize("hasRole('ELEVE')")
    public com.example.edugo.dto.ParticipationDetailResponse getParticipationDetail(Long eleveId, Long challengeId) {
        Participation participation = participationRepository.findByEleveIdAndChallengeId(eleveId, challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation", challengeId));
        
        Challenge challenge = participation.getChallenge();
        Eleve eleve = participation.getEleve();
        
        // Calculer le total de points du challenge (somme des points des questions)
        List<Question> questions = questionRepository.findByChallengeId(challengeId);
        Integer totalPoints = questions.stream()
                .map(q -> q.getPoints() != null ? q.getPoints() : 0)
                .mapToInt(Integer::intValue)
                .sum();
        
        // Si aucune question, utiliser challenge.getPoints() comme total
        if (totalPoints == 0) {
            totalPoints = challenge.getPoints() != null ? challenge.getPoints() : 0;
        }
        
        // Calculer le pourcentage de réussite
        Double pourcentageReussite = null;
        if (participation.getScore() != null && totalPoints > 0) {
            pourcentageReussite = (double) participation.getScore() / totalPoints * 100;
        }
        
        // Calculer les points gagnés (estimation basée sur le score et le rang)
        Integer pointsGagnes = 0;
        if (participation.getScore() != null && totalPoints > 0) {
            double pourcentage = pourcentageReussite;
            if (pourcentage >= 90) {
                pointsGagnes = challenge.getPoints() != null ? challenge.getPoints() : 0;
            } else if (pourcentage >= 80) {
                pointsGagnes = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 8 / 10;
            } else if (pourcentage >= 70) {
                pointsGagnes = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 6 / 10;
            } else if (pourcentage >= 50) {
                pointsGagnes = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 4 / 10;
            }
            
            // Bonus de classement
            Integer rang = participation.getRang();
            if (rang != null && rang <= 3) {
                if (rang == 1) {
                    pointsGagnes += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 2;
                } else if (rang == 2) {
                    pointsGagnes += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 3;
                } else if (rang == 3) {
                    pointsGagnes += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 4;
                }
            }
        }
        
        com.example.edugo.dto.ParticipationDetailResponse dto = new com.example.edugo.dto.ParticipationDetailResponse();
        dto.setId(participation.getId());
        dto.setEleveId(eleve.getId());
        dto.setEleveNom(eleve.getNom());
        dto.setElevePrenom(eleve.getPrenom());
        dto.setChallengeId(challenge.getId());
        dto.setChallengeTitre(challenge.getTitre());
        dto.setScore(participation.getScore());
        dto.setTotalPoints(totalPoints);
        dto.setRang(participation.getRang());
        dto.setTempsPasse(participation.getTempsPasse());
        dto.setStatut(participation.getStatut());
        dto.setDateParticipation(participation.getDateParticipation());
        dto.setBadgeId(participation.getBadge() != null ? participation.getBadge().getId() : null);
        dto.setBadgeNom(participation.getBadge() != null ? participation.getBadge().getNom() : null);
        dto.setBadgeIcone(participation.getBadge() != null ? participation.getBadge().getIcone() : null);
        dto.setPourcentageReussite(pourcentageReussite);
        dto.setPointsGagnes(pointsGagnes);
        
        return dto;
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
    
    public com.example.edugo.dto.StatistiquesChallengeResponse getStatistiquesChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ResourceNotFoundException("Challenge", challengeId));

    List<Participation> participations = participationRepository.findByChallengeId(challengeId);
    Integer nombreParticipations = participations.size();
    Integer nombreValidations = (int) participations.stream()
        .filter(p -> "VALIDE".equals(p.getStatut()))
        .count();
    Integer nombreDisqualifications = (int) participations.stream()
        .filter(p -> "DISQUALIFIE".equals(p.getStatut()))
        .count();
    Double tauxParticipation = participations.isEmpty() ? 0.0 :
        (double) participations.size() / eleveRepository.count() * 100;
    return new com.example.edugo.dto.StatistiquesChallengeResponse(
        challenge.getId(),
        challenge.getTitre(),
        nombreParticipations,
        nombreValidations,
        nombreDisqualifications,
        tauxParticipation
    );
    }

    // ==================== CLASSEMENT CHALLENGES ====================
    
    public List<com.example.edugo.dto.ChallengeLeaderboardEntryResponse> getLeaderboardChallenge(Long challengeId) {
        // Récupérer les participations pour ce challenge, triées par score décroissant
        List<Participation> participations = participationRepository.findByChallengeIdOrderByScoreDesc(challengeId);

        // Construire le leaderboard en se basant sur le score spécifique au challenge
        return participations.stream()
                .limit(10)
                .map(participation -> new com.example.edugo.dto.ChallengeLeaderboardEntryResponse(
                        participation.getEleve().getId(),
                        participation.getEleve().getNom(),
                        participation.getEleve().getPrenom(),
                        participation.getDateParticipation(),
                        participation.getScore() != null ? participation.getScore() : 0
                ))
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

    /**
     * Envoie une notification push aux élèves lorsqu'un nouveau challenge est créé
     * et notifie également les administrateurs
     */
    private void envoyerNotificationNouveauChallenge(Challenge challenge) {
        try {
            String titre = "🎯 Nouveau Challenge disponible !";
            String message = challenge.getTitre() + "\n" + 
                            (challenge.getDescription() != null && !challenge.getDescription().isEmpty() 
                                ? challenge.getDescription().substring(0, Math.min(100, challenge.getDescription().length()))
                                : "Participez dès maintenant !");

            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("type", "NOUVEAU_CHALLENGE");
            data.put("challengeId", challenge.getId());
            data.put("titre", challenge.getTitre());

            // Envoyer à tous les élèves
            oneSignalService.sendNotificationToRole("ELEVE", titre, message, data);
            
            // Notifier les administrateurs (email + OneSignal)
            String adminTitre = "✅ Nouveau Challenge créé";
            String adminMessage = "Un nouveau challenge a été créé : " + challenge.getTitre();
            java.util.Map<String, Object> adminData = new java.util.HashMap<>();
            adminData.put("type", "NOUVEAU_CHALLENGE_CREE");
            adminData.put("challengeId", challenge.getId());
            adminData.put("titre", challenge.getTitre());
            adminData.put("points", challenge.getPoints());
            
            adminNotificationService.notifyAdmins(adminTitre, adminMessage, adminData);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas la création du challenge
            System.err.println("Erreur lors de l'envoi de notification nouveau challenge: " + e.getMessage());
        }
    }
}
