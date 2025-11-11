package com.example.edugo.repository;

import com.example.edugo.entity.TypeStatistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeStatistiqueRepository extends JpaRepository<TypeStatistique, Long> {
    
    Optional<TypeStatistique> findByNom(String nom);
    
    List<TypeStatistique> findByStatut(String statut);
    
    @Query("SELECT t FROM TypeStatistique t WHERE t.statut = 'ACTIF' ORDER BY t.ordreAffichage ASC")
    List<TypeStatistique> findActiveByOrder();
    
    boolean existsByNom(String nom);
}