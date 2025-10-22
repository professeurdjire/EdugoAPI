package com.example.edugo.repository;


import com.example.edugo.entity.Principales.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    // Trouver par nom
    List<Challenge> findByNomContainingIgnoreCase(String nom);

    // Trouver les challenges publiés
    List<Challenge> findByEstPublieTrue();

    // Trouver les challenges d'une classe
    List<Challenge> findByClasseIdClasse(Long classeId);

    // Trouver les challenges actifs (en cours)
    @Query("SELECT c FROM Challenge c WHERE c.dateDebut <= :now AND c.dateFin >= :now")
    List<Challenge> findActiveChallenges(@Param("now") LocalDateTime now);

    // Trouver les challenges à venir
    List<Challenge> findByDateDebutAfter(LocalDateTime date);

    // Trouver les challenges terminés
    List<Challenge> findByDateFinBefore(LocalDateTime date);

    // Trouver par type de challenge
    List<Challenge> findByTypeChallengeId(Long typeChallengeId);

    // Trouver par difficulté
    List<Challenge> findByTypeDifficulteId(Long typeDifficulteId);

    // Recherche avancée
    @Query("SELECT c FROM Challenge c WHERE " +
            "LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Challenge> searchByKeyword(@Param("searchTerm") String searchTerm);

    // Trouver un challenge avec tous les détails
    @Query("SELECT c FROM Challenge c " +
            "LEFT JOIN FETCH c.classe " +
            "LEFT JOIN FETCH c.typeChallenge " +
            "LEFT JOIN FETCH c.typeDifficulte " +
            "WHERE c.id = :challengeId")
    Optional<Challenge> findByIdWithDetails(@Param("challengeId") Long challengeId);
}
