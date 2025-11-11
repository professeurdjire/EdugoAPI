package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    
    Optional<Partenaire> findByNom(String nom);
    
    boolean existsByNom(String nom);
    
    List<Partenaire> findByActifTrue();
    
    @Query("SELECT p FROM Partenaire p ORDER BY p.nom ASC")
    List<Partenaire> findAllByOrderByNomAsc();
    
    @Query("SELECT p FROM Partenaire p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Partenaire> findByNomContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}