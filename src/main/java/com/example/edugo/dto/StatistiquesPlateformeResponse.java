package com.example.edugo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StatistiquesPlateformeResponse {
    // Utilisateurs
    private Long totalUtilisateurs;
    private Long utilisateursActifs;
    private Long utilisateursInactifs;
    private Long totalEleves;
    private Long totalAdmins;
    
    // Contenu
    private Long totalLivres;
    private Long livresDisponibles;
    private Long livresIndisponibles;
    private Long totalExercices;
    private Long exercicesActifs;
    private Long exercicesInactifs;
    private Long totalQuiz;
    private Long quizActifs;
    private Long quizInactifs;
    private Long totalDefis;
    private Long defisActifs;
    private Long defisInactifs;
    private Long totalChallenges;
    private Long challengesActifs;
    private Long challengesInactifs;
    
    // Activités
    private Long totalExercicesRealises;
    private Long totalQuizCompletes;
    private Long totalDefisReussis;
    private Long totalChallengesReussis;
    private Long totalPointsGagnes;
    
    // Badges et récompenses
    private Long totalBadges;
    private Long totalBadgesAttribues;
    
    // Progression
    private Double tauxCompletionMoyen;
    private Long totalProgressions;
    
    // Dates
    private LocalDateTime dateDerniereMiseAJour;
    private LocalDateTime dateDebutPeriode;
    private LocalDateTime dateFinPeriode;
    
    // Statistiques par niveau
    private List<StatistiquesNiveauResponse> statistiquesParNiveau;
    
    // Statistiques par classe
    private List<StatistiquesClasseResponse> statistiquesParClasse;
    
    // Statistiques par matière
    private List<StatistiquesMatiereResponse> statistiquesParMatiere;
    
    public StatistiquesPlateformeResponse() {}
    
    // Getters et Setters
    public Long getTotalUtilisateurs() { return totalUtilisateurs; }
    public void setTotalUtilisateurs(Long totalUtilisateurs) { this.totalUtilisateurs = totalUtilisateurs; }
    
    public Long getUtilisateursActifs() { return utilisateursActifs; }
    public void setUtilisateursActifs(Long utilisateursActifs) { this.utilisateursActifs = utilisateursActifs; }
    
    public Long getUtilisateursInactifs() { return utilisateursInactifs; }
    public void setUtilisateursInactifs(Long utilisateursInactifs) { this.utilisateursInactifs = utilisateursInactifs; }
    
    public Long getTotalEleves() { return totalEleves; }
    public void setTotalEleves(Long totalEleves) { this.totalEleves = totalEleves; }
    
    public Long getTotalAdmins() { return totalAdmins; }
    public void setTotalAdmins(Long totalAdmins) { this.totalAdmins = totalAdmins; }
    
    public Long getTotalLivres() { return totalLivres; }
    public void setTotalLivres(Long totalLivres) { this.totalLivres = totalLivres; }
    
    public Long getLivresDisponibles() { return livresDisponibles; }
    public void setLivresDisponibles(Long livresDisponibles) { this.livresDisponibles = livresDisponibles; }
    
    public Long getLivresIndisponibles() { return livresIndisponibles; }
    public void setLivresIndisponibles(Long livresIndisponibles) { this.livresIndisponibles = livresIndisponibles; }
    
    public Long getTotalExercices() { return totalExercices; }
    public void setTotalExercices(Long totalExercices) { this.totalExercices = totalExercices; }
    
    public Long getExercicesActifs() { return exercicesActifs; }
    public void setExercicesActifs(Long exercicesActifs) { this.exercicesActifs = exercicesActifs; }
    
    public Long getExercicesInactifs() { return exercicesInactifs; }
    public void setExercicesInactifs(Long exercicesInactifs) { this.exercicesInactifs = exercicesInactifs; }
    
    public Long getTotalQuiz() { return totalQuiz; }
    public void setTotalQuiz(Long totalQuiz) { this.totalQuiz = totalQuiz; }
    
    public Long getQuizActifs() { return quizActifs; }
    public void setQuizActifs(Long quizActifs) { this.quizActifs = quizActifs; }
    
    public Long getQuizInactifs() { return quizInactifs; }
    public void setQuizInactifs(Long quizInactifs) { this.quizInactifs = quizInactifs; }
    
    public Long getTotalDefis() { return totalDefis; }
    public void setTotalDefis(Long totalDefis) { this.totalDefis = totalDefis; }
    
    public Long getDefisActifs() { return defisActifs; }
    public void setDefisActifs(Long defisActifs) { this.defisActifs = defisActifs; }
    
    public Long getDefisInactifs() { return defisInactifs; }
    public void setDefisInactifs(Long defisInactifs) { this.defisInactifs = defisInactifs; }
    
    public Long getTotalChallenges() { return totalChallenges; }
    public void setTotalChallenges(Long totalChallenges) { this.totalChallenges = totalChallenges; }
    
    public Long getChallengesActifs() { return challengesActifs; }
    public void setChallengesActifs(Long challengesActifs) { this.challengesActifs = challengesActifs; }
    
    public Long getChallengesInactifs() { return challengesInactifs; }
    public void setChallengesInactifs(Long challengesInactifs) { this.challengesInactifs = challengesInactifs; }
    
    public Long getTotalExercicesRealises() { return totalExercicesRealises; }
    public void setTotalExercicesRealises(Long totalExercicesRealises) { this.totalExercicesRealises = totalExercicesRealises; }
    
    public Long getTotalQuizCompletes() { return totalQuizCompletes; }
    public void setTotalQuizCompletes(Long totalQuizCompletes) { this.totalQuizCompletes = totalQuizCompletes; }
    
    public Long getTotalDefisReussis() { return totalDefisReussis; }
    public void setTotalDefisReussis(Long totalDefisReussis) { this.totalDefisReussis = totalDefisReussis; }
    
    public Long getTotalChallengesReussis() { return totalChallengesReussis; }
    public void setTotalChallengesReussis(Long totalChallengesReussis) { this.totalChallengesReussis = totalChallengesReussis; }
    
    public Long getTotalPointsGagnes() { return totalPointsGagnes; }
    public void setTotalPointsGagnes(Long totalPointsGagnes) { this.totalPointsGagnes = totalPointsGagnes; }
    
    public Long getTotalBadges() { return totalBadges; }
    public void setTotalBadges(Long totalBadges) { this.totalBadges = totalBadges; }
    
    public Long getTotalBadgesAttribues() { return totalBadgesAttribues; }
    public void setTotalBadgesAttribues(Long totalBadgesAttribues) { this.totalBadgesAttribues = totalBadgesAttribues; }
    
    public Double getTauxCompletionMoyen() { return tauxCompletionMoyen; }
    public void setTauxCompletionMoyen(Double tauxCompletionMoyen) { this.tauxCompletionMoyen = tauxCompletionMoyen; }
    
    public Long getTotalProgressions() { return totalProgressions; }
    public void setTotalProgressions(Long totalProgressions) { this.totalProgressions = totalProgressions; }
    
    public LocalDateTime getDateDerniereMiseAJour() { return dateDerniereMiseAJour; }
    public void setDateDerniereMiseAJour(LocalDateTime dateDerniereMiseAJour) { this.dateDerniereMiseAJour = dateDerniereMiseAJour; }
    
    public LocalDateTime getDateDebutPeriode() { return dateDebutPeriode; }
    public void setDateDebutPeriode(LocalDateTime dateDebutPeriode) { this.dateDebutPeriode = dateDebutPeriode; }
    
    public LocalDateTime getDateFinPeriode() { return dateFinPeriode; }
    public void setDateFinPeriode(LocalDateTime dateFinPeriode) { this.dateFinPeriode = dateFinPeriode; }
    
    public List<StatistiquesNiveauResponse> getStatistiquesParNiveau() { return statistiquesParNiveau; }
    public void setStatistiquesParNiveau(List<StatistiquesNiveauResponse> statistiquesParNiveau) { this.statistiquesParNiveau = statistiquesParNiveau; }
    
    public List<StatistiquesClasseResponse> getStatistiquesParClasse() { return statistiquesParClasse; }
    public void setStatistiquesParClasse(List<StatistiquesClasseResponse> statistiquesParClasse) { this.statistiquesParClasse = statistiquesParClasse; }
    
    public List<StatistiquesMatiereResponse> getStatistiquesParMatiere() { return statistiquesParMatiere; }
    public void setStatistiquesParMatiere(List<StatistiquesMatiereResponse> statistiquesParMatiere) { this.statistiquesParMatiere = statistiquesParMatiere; }
}