package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {
    
    Optional<Eleve> findByEmail(String email);
    
    List<Eleve> findByClasse(Classe classe);
    
    @Query("SELECT e FROM Eleve e ORDER BY e.nom ASC, e.prenom ASC")
    List<Eleve> findAllOrderByNom();
    
    @Query("SELECT e FROM Eleve e WHERE e.classe.id = :classeId")
    List<Eleve> findByClasseId(@Param("classeId") Long classeId);
    
    @Query("SELECT e FROM Eleve e WHERE " +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Eleve> searchByNomOrPrenom(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT e FROM Eleve e ORDER BY e.pointAccumule DESC")
    List<Eleve> findAllOrderByPoints();

    @Query("SELECT e FROM Eleve e WHERE e.classe.niveau.id = :niveauId")
    List<Eleve> findByClasseNiveauId(@Param("niveauId") Long niveauId);
    
    // Méthodes pour les statistiques
    @Query("SELECT SUM(e.pointAccumule) FROM Eleve e")
    Long sumAllPoints();
}

