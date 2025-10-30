package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Objectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Long> {
    
    List<Objectif> findByEleve(Eleve eleve);
    
    @Query("SELECT o FROM Objectif o WHERE o.eleve.id = :eleveId")
    List<Objectif> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT o FROM Objectif o WHERE o.eleve.id = :eleveId ORDER BY o.dateEnvoie DESC")
    List<Objectif> findByEleveIdOrderByDateDesc(@Param("eleveId") Long eleveId);
}

