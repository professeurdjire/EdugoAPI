package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    // Trouver toutes les suggestions d'un élève, triées par date décroissante
    List<Suggestion> findByEleveIdOrderByDateEnvoieDesc(Long eleveId);

    // Trouver une suggestion spécifique d'un élève
    Optional<Suggestion> findByIdAndEleveId(Long id, Long eleveId);

    // Trouver toutes les suggestions, triées par date décroissante
    List<Suggestion> findAllByOrderByDateEnvoieDesc();

    // Statistiques - VERSION CORRIGÉE
    @Query("SELECT COUNT(s) FROM Suggestion s WHERE YEAR(s.dateEnvoie) = YEAR(CURRENT_DATE) AND MONTH(s.dateEnvoie) = MONTH(CURRENT_DATE)")
    long countSuggestionsCeMois();

    @Query(value = "SELECT COUNT(*) FROM suggestion WHERE date_envoie >= CURRENT_DATE - 7", nativeQuery = true)
    long countSuggestionsCetteSemaine();

    @Query(value = "SELECT COUNT(*) FROM suggestion WHERE CAST(date_envoie AS DATE) = CURRENT_DATE", nativeQuery = true)
    long countSuggestionsAujourdhui();
}