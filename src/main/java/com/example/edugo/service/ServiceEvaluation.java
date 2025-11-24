package com.example.edugo.service;

import com.example.edugo.dto.SubmitRequest;
import com.example.edugo.dto.SubmitResultResponse;
import com.example.edugo.dto.SubmitResultDetail;
import com.example.edugo.entity.Principales.Question;
import com.example.edugo.entity.Principales.ReponsePossible;
import com.example.edugo.entity.Principales.Participation;
import com.example.edugo.entity.Principales.Challenge;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Badge;
import com.example.edugo.entity.Principales.Defi;
import com.example.edugo.entity.Principales.EleveDefi;
import com.example.edugo.exception.ResourceNotFoundException;
import com.example.edugo.repository.QuestionRepository;
import com.example.edugo.repository.QuizRepository;
import com.example.edugo.repository.ParticipationRepository;
import com.example.edugo.repository.ChallengeRepository;
import com.example.edugo.repository.EleveRepository;
import com.example.edugo.repository.BadgeRepository;
import com.example.edugo.repository.DefiRepository;
import com.example.edugo.repository.EleveDefiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceEvaluation {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ParticipationRepository participationRepository;
    private final ChallengeRepository challengeRepository;
    private final EleveRepository eleveRepository;
    private final BadgeRepository badgeRepository;
    private final OneSignalService oneSignalService;
    private final DefiRepository defiRepository;
    private final EleveDefiRepository eleveDefiRepository;

    public ServiceEvaluation(QuizRepository quizRepository,
                             QuestionRepository questionRepository,
                             ParticipationRepository participationRepository,
                             ChallengeRepository challengeRepository,
                             EleveRepository eleveRepository,
                             BadgeRepository badgeRepository,
                             OneSignalService oneSignalService,
                             DefiRepository defiRepository,
                             EleveDefiRepository eleveDefiRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.participationRepository = participationRepository;
        this.challengeRepository = challengeRepository;
        this.eleveRepository = eleveRepository;
        this.badgeRepository = badgeRepository;
        this.oneSignalService = oneSignalService;
        this.defiRepository = defiRepository;
        this.eleveDefiRepository = eleveDefiRepository;
    }

    public SubmitResultResponse submitQuiz(Long quizId, SubmitRequest req) {
        // ensure quiz exists
        quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", quizId));

        // load questions of the quiz
        List<Question> questions = questionRepository.findByQuizId(quizId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        int score = 0;
        List<SubmitResultDetail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                // load with options
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultDetail d = new SubmitResultDetail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(quizId);
        res.setOwnerType("QUIZ");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }

    @Transactional
    public SubmitResultResponse submitChallenge(Long challengeId, SubmitRequest req) {
        // Vérifier que le challenge existe
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", challengeId));
        
        // Vérifier que l'élève existe
        Eleve eleve = eleveRepository.findById(req.getEleveId())
                .orElseThrow(() -> new ResourceNotFoundException("Élève", req.getEleveId()));
        
        // Vérifier que l'élève a participé au challenge
        Participation participation = participationRepository.findByEleveIdAndChallengeId(req.getEleveId(), challengeId)
                .orElseThrow(() -> new RuntimeException("Vous devez d'abord participer à ce challenge"));
        
        // Vérifier que le challenge est encore actif
        java.time.LocalDateTime maintenant = java.time.LocalDateTime.now();
        if (maintenant.isBefore(challenge.getDateDebut()) || maintenant.isAfter(challenge.getDateFin())) {
            throw new RuntimeException("Ce challenge n'est plus disponible");
        }
        
        // Charger les questions du challenge
        List<Question> questions = questionRepository.findByChallengeId(challengeId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        // Mapper les réponses par question
        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        // Calculer le score
        int score = 0;
        List<SubmitResultDetail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultDetail d = new SubmitResultDetail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        // Mettre à jour la participation avec le score
        participation.setScore(score);
        participation.setStatut("TERMINE");
        participation.setaParticiper(true);
        
        // Mettre à jour le temps passé si fourni dans la requête
        if (req.getTempsPasse() != null && req.getTempsPasse() > 0) {
            participation.setTempsPasse(req.getTempsPasse());
        }
        
        participationRepository.save(participation);
        
        // Calculer et mettre à jour le classement (rang)
        updateRanking(challengeId);
        
        // Attribuer des badges si mérité
        boolean badgeObtenu = attribuerBadgesChallenge(participation, challenge, score, totalPoints);
        
        // Ajouter les points à l'élève selon le score et le classement
        int pointsGagnes = ajouterPointsChallenge(eleve, challenge, participation, score, totalPoints);

        // Récupérer le rang mis à jour
        Integer rang = participation.getRang();
        
        // Envoyer une notification push à l'élève
        envoyerNotificationChallengeTermine(eleve, challenge, score, totalPoints, rang, badgeObtenu, pointsGagnes);

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(challengeId);
        res.setOwnerType("CHALLENGE");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }
    
    @Transactional
    private void updateRanking(Long challengeId) {
        // Récupérer toutes les participations avec score, triées par score décroissant
        List<Participation> participations = participationRepository.findByChallengeIdOrderByScoreDesc(challengeId);
        
        // Filtrer seulement les participations terminées avec un score > 0
        List<Participation> participationsValides = participations.stream()
                .filter(p -> p.getScore() != null && p.getScore() > 0 && "TERMINE".equals(p.getStatut()))
                .collect(Collectors.toList());
        
        // Attribuer un rang à chaque participation valide
        int rang = 1;
        Integer dernierScore = null;
        
        for (Participation p : participationsValides) {
            Integer scoreActuel = p.getScore();
            
            // Si le score est différent du précédent, on met à jour le rang
            if (dernierScore == null || !dernierScore.equals(scoreActuel)) {
                p.setRang(rang);
                rang++;
            } else {
                // Même score = même rang (ex-aequo) - ne pas incrémenter le rang
                p.setRang(rang - 1);
            }
            
            dernierScore = scoreActuel;
            participationRepository.save(p);
        }
        
        // Pour les participations non terminées ou avec score 0, mettre le rang à null
        for (Participation p : participations) {
            if (!participationsValides.contains(p)) {
                p.setRang(null);
                participationRepository.save(p);
            }
        }
    }
    
    @Transactional
    private boolean attribuerBadgesChallenge(Participation participation, Challenge challenge, int score, int totalPoints) {
        // Calculer le pourcentage de réussite
        double pourcentage = totalPoints > 0 ? (double) score / totalPoints * 100 : 0;
        
        // Récupérer les badges associés au challenge
        List<Badge> badgesChallenge = challenge.getRewards() != null ? challenge.getRewards() : new ArrayList<>();
        
        // Attribuer des badges selon le score/pourcentage
        for (Badge badge : badgesChallenge) {
            // Logique d'attribution basée sur le score ou le pourcentage
            // Exemple : si le pourcentage est >= 80%, attribuer un badge "Excellent"
            // Cette logique peut être personnalisée selon vos besoins
            if (pourcentage >= 80 && participation.getBadge() == null) {
                participation.setBadge(badge);
                participationRepository.save(participation);
                return true; // Badge obtenu
            }
        }
        return false; // Pas de badge obtenu
    }
    
    @Transactional
    private int ajouterPointsChallenge(Eleve eleve, Challenge challenge, Participation participation, int score, int totalPoints) {
        // Calculer les points à ajouter selon le score et le classement
        int pointsAAjouter = 0;
        
        // Base : points selon le pourcentage de réussite
        double pourcentage = totalPoints > 0 ? (double) score / totalPoints * 100 : 0;
        
        if (pourcentage >= 90) {
            pointsAAjouter = challenge.getPoints() != null ? challenge.getPoints() : 0;
        } else if (pourcentage >= 80) {
            pointsAAjouter = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 8 / 10;
        } else if (pourcentage >= 70) {
            pointsAAjouter = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 6 / 10;
        } else if (pourcentage >= 50) {
            pointsAAjouter = (challenge.getPoints() != null ? challenge.getPoints() : 0) * 4 / 10;
        }
        
        // Bonus de classement : points supplémentaires selon le rang
        Integer rang = participation.getRang();
        if (rang != null && rang <= 3) {
            // Top 3 : bonus de points
            if (rang == 1) {
                pointsAAjouter += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 2; // +50% pour le 1er
            } else if (rang == 2) {
                pointsAAjouter += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 3; // +33% pour le 2ème
            } else if (rang == 3) {
                pointsAAjouter += (challenge.getPoints() != null ? challenge.getPoints() : 0) / 4; // +25% pour le 3ème
            }
        }
        
        // Ajouter les points à l'élève
        if (pointsAAjouter > 0) {
            eleve.setPointAccumule(eleve.getPointAccumule() + pointsAAjouter);
            eleveRepository.save(eleve);
        }
        
        return pointsAAjouter;
    }

    /**
     * Envoie une notification push à l'élève après avoir terminé un challenge
     */
    private void envoyerNotificationChallengeTermine(Eleve eleve, Challenge challenge, int score, int totalPoints, 
                                                      Integer rang, boolean badgeObtenu, int pointsGagnes) {
        try {
            double pourcentage = totalPoints > 0 ? (double) score / totalPoints * 100 : 0;
            String titre = "🎉 Challenge terminé !";
            StringBuilder message = new StringBuilder();
            
            message.append("Vous avez obtenu ").append(score).append("/").append(totalPoints);
            message.append(" (").append(String.format("%.1f", pourcentage)).append("%)");
            
            if (rang != null && rang <= 3) {
                message.append("\n🏆 Rang ").append(rang);
            }
            
            if (badgeObtenu) {
                message.append("\n🏅 Badge obtenu !");
            }
            
            if (pointsGagnes > 0) {
                message.append("\n⭐ ").append(pointsGagnes).append(" points gagnés !");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("type", "CHALLENGE_TERMINE");
            data.put("challengeId", challenge.getId());
            data.put("score", score);
            data.put("totalPoints", totalPoints);
            data.put("rang", rang);
            data.put("badgeObtenu", badgeObtenu);
            data.put("pointsGagnes", pointsGagnes);

            oneSignalService.sendNotificationToUser(eleve.getId(), "ELEVE", titre, message.toString(), data);
        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas le processus
            System.err.println("Erreur lors de l'envoi de notification challenge terminé: " + e.getMessage());
        }
    }

    public SubmitResultResponse submitExercice(Long exerciceId, SubmitRequest req) {
        List<Question> questions = questionRepository.findByExerciceId(exerciceId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        int score = 0;
        List<SubmitResultDetail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultDetail d = new SubmitResultDetail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(exerciceId);
        res.setOwnerType("EXERCICE");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }

    @Transactional
    public SubmitResultResponse submitDefi(Long defiId, SubmitRequest req) {
        // Vérifier que le défi existe
        Defi defi = defiRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Défi", defiId));
        
        // Vérifier que l'élève existe
        Eleve eleve = eleveRepository.findById(req.getEleveId())
                .orElseThrow(() -> new ResourceNotFoundException("Élève", req.getEleveId()));
        
        // Vérifier que l'élève a participé au défi
        EleveDefi eleveDefi = eleveDefiRepository.findByEleveIdAndDefiId(req.getEleveId(), defiId)
                .orElseThrow(() -> new RuntimeException("Vous devez d'abord participer à ce défi"));
        
        // Charger les questions du défi
        List<Question> questions = questionRepository.findByDefiId(defiId);
        int totalPoints = questions.stream().map(q -> Optional.ofNullable(q.getPoints()).orElse(0)).mapToInt(Integer::intValue).sum();

        // Mapper les réponses par question
        Map<Long, SubmitRequest.SubmitAnswer> answerMap = (req.getReponses() == null ? List.<SubmitRequest.SubmitAnswer>of() : req.getReponses())
                .stream().collect(Collectors.toMap(SubmitRequest.SubmitAnswer::getQuestionId, a -> a, (a,b)->a));

        // Calculer le score
        int score = 0;
        List<SubmitResultDetail> details = new ArrayList<>();

        for (Question q : questions) {
            SubmitRequest.SubmitAnswer ans = answerMap.get(q.getId());
            boolean correct = false;
            if (ans != null && ans.getReponseIds() != null) {
                Question withOptions = questionRepository.findByIdWithReponses(q.getId()).orElse(q);
                Set<Long> selected = new HashSet<>(ans.getReponseIds());
                Set<Long> correctIds = withOptions.getReponsesPossibles() == null ? Set.of() : withOptions.getReponsesPossibles().stream()
                        .filter(ReponsePossible::isEstCorrecte)
                        .map(ReponsePossible::getId)
                        .collect(Collectors.toSet());
                String type = withOptions.getType() != null ? withOptions.getType().getLibelleType() : "";
                String T = type == null ? "" : type.toUpperCase();
                if ("QCU".equals(T) || "VRAI_FAUX".equals(T)) {
                    correct = selected.size() == 1 && correctIds.size() == 1 && selected.iterator().next().equals(correctIds.iterator().next());
                } else if ("QCM".equals(T)) {
                    correct = selected.equals(correctIds);
                }
            }
            int pts = Optional.ofNullable(q.getPoints()).orElse(0);
            if (correct) score += pts;
            SubmitResultDetail d = new SubmitResultDetail();
            d.setQuestionId(q.getId());
            d.setPoints(pts);
            d.setCorrect(correct);
            details.add(d);
        }

        // Mettre à jour la participation avec le statut
        eleveDefi.setStatut("TERMINE");
        eleveDefiRepository.save(eleveDefi);
        
        // Ajouter les points à l'élève selon le score
        if (defi.getPointDefi() != null && score > 0) {
            double pourcentage = totalPoints > 0 ? (double) score / totalPoints * 100 : 0;
            int pointsAAjouter = 0;
            
            if (pourcentage >= 90) {
                pointsAAjouter = defi.getPointDefi();
            } else if (pourcentage >= 80) {
                pointsAAjouter = defi.getPointDefi() * 8 / 10;
            } else if (pourcentage >= 70) {
                pointsAAjouter = defi.getPointDefi() * 6 / 10;
            } else if (pourcentage >= 50) {
                pointsAAjouter = defi.getPointDefi() * 4 / 10;
            }
            
            if (pointsAAjouter > 0) {
                eleve.setPointAccumule(eleve.getPointAccumule() + pointsAAjouter);
                eleveRepository.save(eleve);
            }
        }

        SubmitResultResponse res = new SubmitResultResponse();
        res.setOwnerId(defiId);
        res.setOwnerType("DEFI");
        res.setEleveId(req.getEleveId());
        res.setScore(score);
        res.setTotalPoints(totalPoints);
        res.setDetails(details);
        return res;
    }
}
