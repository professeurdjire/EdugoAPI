package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    
    Optional<Matiere> findByNom(String nom);
    
    boolean existsByNom(String nom);
    
    @Query("SELECT m FROM Matiere m ORDER BY m.nom ASC")
    List<Matiere> findAllOrderByNom();
    
    @Query("SELECT m FROM Matiere m WHERE LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Matiere> searchByNom(@Param("searchTerm") String searchTerm);

    List<Matiere> findByNomContainingIgnoreCase(String nom);
}

