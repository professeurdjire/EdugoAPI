package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    
    Optional<Niveau> findByNom(String nom);
    
    boolean existsByNom(String nom);
    
    @Query("SELECT n FROM Niveau n ORDER BY n.nom ASC")
    List<Niveau> findAllByOrderByNomAsc();
    
    @Query("SELECT n FROM Niveau n WHERE LOWER(n.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Niveau> findByNomContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}

