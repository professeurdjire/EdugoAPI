package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    
    Optional<Badge> findByNom(String nom);
    
    boolean existsByNom(String nom);
    
    @Query("SELECT b FROM Badge b ORDER BY b.nom ASC")
    List<Badge> findAllOrderByNom();

    // Added for filters
    @Query("SELECT b FROM Badge b WHERE b.type = :type")
    List<Badge> findByType(@Param("type") com.example.edugo.entity.Principales.TypeBadge type);
    
    @Query("SELECT b FROM Badge b WHERE LOWER(b.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Badge> findByNomContainingIgnoreCase(@Param("nom") String nom);
}

