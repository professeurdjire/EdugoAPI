package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Trouver par nom
    Optional<Classe> findByNom(String nom);

    // Added for domain services
    @Query("SELECT c FROM Classe c WHERE c.niveau.id = :niveauId")
    List<Classe> findByNiveauId(@Param("niveauId") Long niveauId);

    @Query("SELECT c FROM Classe c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Classe> findByNomContainingIgnoreCase(@Param("nom") String nom);

}
