package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Progression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressionRepository extends JpaRepository<Progression, Long> {
    
    List<Progression> findByEleve(Eleve eleve);
    
    List<Progression> findByLivre(Livre livre);
    
    @Query("SELECT p FROM Progression p WHERE p.eleve.id = :eleveId")
    List<Progression> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT p FROM Progression p WHERE p.livre.id = :livreId")
    List<Progression> findByLivreId(@Param("livreId") Long livreId);
    
    @Query("SELECT p FROM Progression p WHERE p.eleve.id = :eleveId AND p.livre.id = :livreId")
    Optional<Progression> findByEleveIdAndLivreId(@Param("eleveId") Long eleveId, @Param("livreId") Long livreId);

    @Query("SELECT COUNT(p) FROM Progression p WHERE p.eleve.id = :eleveId")
    Long countByEleveId(@Param("eleveId") Long eleveId);
    
    // Méthodes pour les statistiques
    @Query("SELECT AVG(p.pourcentageCompletion) FROM Progression p")
    Double calculateAverageCompletionRate();

// Methode concernant mon objectif
    @Query("SELECT p FROM Progression p WHERE p.eleve = :eleve AND p.pourcentageCompletion = :pourcentageCompletion AND p.dateDerniereLecture BETWEEN :startDate AND :endDate")
    List<Progression> findByEleveAndPourcentageCompletionAndDateDerniereLectureBetween(
            @Param("eleve") Eleve eleve,
            @Param("pourcentageCompletion") Integer pourcentageCompletion,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}

