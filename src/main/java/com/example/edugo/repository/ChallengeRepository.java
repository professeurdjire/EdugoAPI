package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Challenge;
import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Niveau;
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
    
    List<Challenge> findByClasse(Classe classe);
    
    List<Challenge> findByNiveau(Niveau niveau);
    
    @Query("SELECT c FROM Challenge c WHERE c.classe.id = :classeId")
    List<Challenge> findByClasseId(@Param("classeId") Long classeId);
    
    @Query("SELECT c FROM Challenge c WHERE c.niveau.id = :niveauId")
    List<Challenge> findByNiveauId(@Param("niveauId") Long niveauId);
    
    @Query("SELECT c FROM Challenge c ORDER BY c.dateDebut DESC")
    List<Challenge> findAllOrderByDateDesc();
    
    @Query("SELECT c FROM Challenge c WHERE :now BETWEEN c.dateDebut AND c.dateFin")
    List<Challenge> findActiveChallenges(@Param("now") LocalDateTime now);
    
    @Query("SELECT c FROM Challenge c WHERE " +
           "LOWER(c.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Challenge> searchByTitreOrDescription(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Challenge c WHERE LOWER(c.titre) LIKE LOWER(CONCAT('%', :titre, '%'))")
    List<Challenge> findByTitreContainingIgnoreCase(@Param("titre") String titre);

    @Query("SELECT c FROM Challenge c WHERE c.typeChallenge = :type")
    List<Challenge> findByTypeChallenge(@Param("type") TypeChallenge typeChallenge);

    @Query("SELECT c FROM Challenge c WHERE c.dateFin < :now")
    List<Challenge> findByDateFinBefore(@Param("now") LocalDateTime now);

    @Query("SELECT c FROM Challenge c WHERE c.dateDebut > :now")
    List<Challenge> findByDateDebutAfter(@Param("now") LocalDateTime now);

    @Query("SELECT c FROM Challenge c WHERE c.rewardMode = :rewardMode")
    List<Challenge> findByRewardMode(@Param("rewardMode") String rewardMode);
}
