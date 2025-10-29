package com.example.edugo.repository;


import com.example.edugo.entity.Principales.Challenge;
import com.example.edugo.entity.Principales.TypeChallenge;
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
    List<Challenge> findByTitreContainingIgnoreCase(String titre);

    // Trouver les challenges actifs (en cours)
    @Query("SELECT c FROM Challenge c WHERE c.dateDebut <= :now AND c.dateFin >= :now")
    List<Challenge> findActiveChallenges(@Param("now") LocalDateTime now);

    // Trouver les challenges terminés
    List<Challenge> findByDateFinBefore(LocalDateTime date);

    // Trouver par type de challenge
    List<Challenge> findByTypeChallenge(TypeChallenge typeChallenge);
    // Recherche avancée
    @Query("SELECT c FROM Challenge c WHERE " +
            "LOWER(c.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Challenge> searchByKeyword(@Param("searchTerm") String searchTerm);

    /*Trouver un challenge avec tous les détails
    @Query("SELECT c FROM Challenge c " +
            "LEFT JOIN FETCH c.classe " +
            "LEFT JOIN FETCH c.typeChallenge " +
            "WHERE c.id = :challengeId")
    Optional<Challenge> findByIdWithDetails(@Param("challengeId") Long challengeId);*/
}
