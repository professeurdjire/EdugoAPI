package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Exercice;
import com.example.edugo.entity.Principales.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
    
    List<Exercice> findByTitreContainingIgnoreCase(String titre);
    
    List<Exercice> findByMatiere(Matiere matiere);
    
    @Query("SELECT e FROM Exercice e WHERE e.matiere.id = :matiereId")
    List<Exercice> findByMatiereId(@Param("matiereId") Long matiereId);
    
    @Query("SELECT e FROM Exercice e ORDER BY e.dateCreation DESC")
    List<Exercice> findAllOrderByDateDesc();
    
    @Query("SELECT e FROM Exercice e WHERE " +
           "LOWER(e.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Exercice> searchByTitreOrDescription(@Param("searchTerm") String searchTerm);

    // Added for ELEVE flows
    @Query("SELECT e FROM Exercice e WHERE e.active = true")
    List<Exercice> findByActiveTrue();

    List<Exercice> findByTitreContainingIgnoreCaseAndActiveTrue(String titre);

    @Query("SELECT e FROM Exercice e WHERE e.matiere.id = :matiereId AND e.active = true")
    List<Exercice> findByMatiereIdAndActiveTrue(@Param("matiereId") Long matiereId);

    @Query("SELECT e FROM Exercice e WHERE e.niveauDifficulte = :niveau AND e.active = true")
    List<Exercice> findByNiveauDifficulteAndActiveTrue(@Param("niveau") Integer niveau);

    @Query("SELECT e FROM Exercice e WHERE e.livre.id = :livreId AND e.active = true")
    List<Exercice> findByLivreIdAndActiveTrue(@Param("livreId") Long livreId);
}

