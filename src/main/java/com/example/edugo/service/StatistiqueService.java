package com.example.edugo.service;

import com.example.edugo.dto.StatistiquesClasseResponse;
import com.example.edugo.dto.StatistiquesNiveauResponse;
import com.example.edugo.dto.StatistiquesPlateformeResponse;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Niveau;
import com.example.edugo.entity.Statistique;
import com.example.edugo.entity.TypeStatistique;
import com.example.edugo.entity.User;
import com.example.edugo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatistiqueService {
    
    private final StatistiqueRepository statistiqueRepository;
    private final TypeStatistiqueRepository typeStatistiqueRepository;
    private final UserRepository userRepository;
    private final EleveRepository eleveRepository;
    private final LivreRepository livreRepository;
    private final ExerciceRepository exerciceRepository;
    private final QuizRepository quizRepository;
    private final DefiRepository defiRepository;
    private final ChallengeRepository challengeRepository;
    private final BadgeRepository badgeRepository;
    private final ProgressionRepository progressionRepository;
    private final FaireExerciceRepository faireExerciceRepository;
    private final ParticipationRepository participationRepository;
    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    
    @Transactional(readOnly = true)
    public StatistiquesPlateformeResponse getStatistiquesPlateforme() {
        StatistiquesPlateformeResponse stats = new StatistiquesPlateformeResponse();
        
        // Utilisateurs
        stats.setTotalUtilisateurs((long) userRepository.findAll().size());
        stats.setUtilisateursActifs(userRepository.countByEstActiveTrue());
        stats.setUtilisateursInactifs(userRepository.countByEstActiveFalse());
        stats.setTotalEleves((long) eleveRepository.findAll().size());
        stats.setTotalAdmins((long) userRepository.findByRole("ADMIN").size());
        
        // Contenu
        stats.setTotalLivres((long) livreRepository.findAll().size());
        // Livre doesn't have a 'disponible' property, so we can't use these methods
        // stats.setLivresDisponibles(livreRepository.countByDisponibleTrue());
        // stats.setLivresIndisponibles(livreRepository.countByDisponibleFalse());
        
        stats.setTotalExercices((long) exerciceRepository.findAll().size());
        stats.setExercicesActifs(exerciceRepository.countByActiveTrue());
        stats.setExercicesInactifs(exerciceRepository.countByActiveFalse());
        
        stats.setTotalQuiz((long) quizRepository.findAll().size());
        stats.setQuizActifs(quizRepository.countByStatutActif());
        stats.setQuizInactifs(quizRepository.countByStatutInactif());
        
        stats.setTotalDefis((long) defiRepository.findAll().size());
        // Defi doesn't have an 'active' property, so we can't use these methods
        // stats.setDefisActifs(defiRepository.countByActiveTrue());
        // stats.setDefisInactifs(defiRepository.countByActiveFalse());
        
        stats.setTotalChallenges((long) challengeRepository.findAll().size());
        // Challenge doesn't have an 'active' property, so we can't use these methods
        // stats.setChallengesActifs(challengeRepository.countByActiveTrue());
        // stats.setChallengesInactifs(challengeRepository.countByActiveFalse());
        
        // Activités
        stats.setTotalExercicesRealises((long) faireExerciceRepository.findAll().size());
        stats.setTotalQuizCompletes(faireExerciceRepository.countByNoteNotNull());
        stats.setTotalDefisReussis(participationRepository.countByStatut("REUSSI"));
        stats.setTotalChallengesReussis(participationRepository.countByStatut("REUSSI"));
        stats.setTotalPointsGagnes(eleveRepository.sumAllPoints());
        
        // Badges et récompenses
        stats.setTotalBadges((long) badgeRepository.findAll().size());
        stats.setTotalBadgesAttribues(0L); // À implémenter si nécessaire
        
        // Progression
        stats.setTotalProgressions((long) progressionRepository.findAll().size());
        stats.setTauxCompletionMoyen(progressionRepository.calculateAverageCompletionRate());
        
        // Dates
        stats.setDateDerniereMiseAJour(LocalDateTime.now());
        
        // Statistiques par niveau et classe
        stats.setStatistiquesParNiveau(getStatistiquesParNiveau());
        stats.setStatistiquesParClasse(getStatistiquesParClasse());
        
        return stats;
    }
    
    // Méthodes utilitaires pour les statistiques détaillées
    private List<StatistiquesNiveauResponse> getStatistiquesParNiveau() {
        List<Niveau> niveaux = niveauRepository.findAll();
        return niveaux.stream()
                .map(niveau -> {
                    List<Classe> classes = classeRepository.findByNiveauId(niveau.getId());
                    List<Eleve> eleves = eleveRepository.findByClasseNiveauId(niveau.getId());
                    List<Livre> livres = livreRepository.findByNiveauId(niveau.getId());
                    int pointsMoyens = eleves.isEmpty() ? 0 : eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
                    return new StatistiquesNiveauResponse(
                            niveau.getId(),
                            niveau.getNom(),
                            classes.size(),
                            eleves.size(),
                            livres.size(),
                            pointsMoyens
                    );
                })
                .collect(Collectors.toList());
    }
    
    private List<StatistiquesClasseResponse> getStatistiquesParClasse() {
        List<Classe> classes = classeRepository.findAll();
        return classes.stream()
                .map(classe -> {
                    List<Eleve> eleves = eleveRepository.findByClasseId(classe.getId());
                    List<Livre> livres = livreRepository.findByClasseId(classe.getId());
                    int pointsMoyens = eleves.isEmpty() ? 0 : eleves.stream().mapToInt(Eleve::getPointAccumule).sum() / eleves.size();
                    return new StatistiquesClasseResponse(
                            classe.getId(),
                            classe.getNom(),
                            classe.getNiveau() != null ? classe.getNiveau().getNom() : "N/A",
                            eleves.size(),
                            pointsMoyens
                    );
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void enregistrerStatistique(String indicateur, String periode, Integer valeur) {
        Statistique statistique = new Statistique();
        statistique.setIndicateur(indicateur);
        statistique.setPeriode(periode);
        statistique.setValeur(valeur);
        statistique.setDateCalcule(new java.util.Date());
        statistiqueRepository.save(statistique);
    }
    
    @Transactional
    public TypeStatistique creerTypeStatistique(String nom, String description, String unite, String typeCalcul) {
        TypeStatistique typeStat = new TypeStatistique();
        typeStat.setNom(nom);
        typeStat.setDescription(description);
        typeStat.setUnite(unite);
        typeStat.setTypeCalcul(typeCalcul);
        typeStat.setStatut("ACTIF");
        typeStat.setOrdreAffichage(0);
        return typeStatistiqueRepository.save(typeStat);
    }
}