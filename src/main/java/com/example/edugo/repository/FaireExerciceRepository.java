package com.example.edugo.repository;

import com.example.edugo.entity.Principales.FaireExercice;
import com.example.edugo.entity.Principales.StatutExercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaireExerciceRepository extends JpaRepository<FaireExercice, Long> {

    @Query("SELECT f FROM FaireExercice f WHERE f.eleve.id = :eleveId")
    List<FaireExercice> findByEleveId(@Param("eleveId") Long eleveId);

    @Query("SELECT f FROM FaireExercice f WHERE f.eleve.id = :eleveId AND f.exercice.id = :exerciceId")
    java.util.Optional<FaireExercice> findByEleveIdAndExerciceId(@Param("eleveId") Long eleveId, @Param("exerciceId") Long exerciceId);

    @Query("SELECT f FROM FaireExercice f WHERE f.exercice.id = :exerciceId")
    List<FaireExercice> findByExerciceId(@Param("exerciceId") Long exerciceId);

    @Query("SELECT f FROM FaireExercice f WHERE f.statut = :statut")
    List<FaireExercice> findByStatut(@Param("statut") StatutExercice statut);

    @Query("SELECT COUNT(f) FROM FaireExercice f WHERE f.eleve.id = :eleveId")
    Long countByEleveId(@Param("eleveId") Long eleveId);
    
    // Méthodes pour les statistiques
    Long countByNoteNotNull();
}