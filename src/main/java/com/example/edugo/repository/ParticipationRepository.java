package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Challenge;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    
    List<Participation> findByChallenge(Challenge challenge);
    
    List<Participation> findByEleve(Eleve eleve);
    
    @Query("SELECT p FROM Participation p WHERE p.challenge.id = :challengeId")
    List<Participation> findByChallengeId(@Param("challengeId") Long challengeId);
    
    @Query("SELECT p FROM Participation p WHERE p.eleve.id = :eleveId")
    List<Participation> findByEleveId(@Param("eleveId") Long eleveId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.eleve.id = :eleveId")
    Long countByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT p FROM Participation p WHERE p.challenge.id = :challengeId ORDER BY p.score DESC")
    List<Participation> findByChallengeIdOrderByScoreDesc(@Param("challengeId") Long challengeId);
    
    boolean existsByChallengeAndEleve(Challenge challenge, Eleve eleve);

    // Added for ELEVE flows
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Participation p WHERE p.eleve.id = :eleveId AND p.challenge.id = :challengeId")
    boolean existsByEleveIdAndChallengeId(@Param("eleveId") Long eleveId, @Param("challengeId") Long challengeId);

    @Query("SELECT p FROM Participation p WHERE p.eleve.id = :eleveId AND p.challenge.id = :challengeId")
    java.util.Optional<Participation> findByEleveIdAndChallengeId(@Param("eleveId") Long eleveId, @Param("challengeId") Long challengeId);

    @Query("SELECT p FROM Participation p WHERE p.challenge.id = :challengeId AND p.statut = :statut")
    List<Participation> findByChallengeIdAndStatut(@Param("challengeId") Long challengeId, @Param("statut") String statut);
    
    // Méthodes pour les statistiques
    Long countByStatut(String statut);
}

