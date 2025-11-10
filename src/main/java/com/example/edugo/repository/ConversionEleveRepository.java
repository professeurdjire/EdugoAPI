package com.example.edugo.repository;

import com.example.edugo.entity.Principales.ConversionEleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionEleveRepository extends JpaRepository<ConversionEleve, Long> {
    List<ConversionEleve> findByEleveId(Long eleveId);
    
    @Query("SELECT c FROM ConversionEleve c WHERE c.eleve.id = :eleveId ORDER BY c.dateConversion DESC")
    List<ConversionEleve> findHistoriqueByEleveId(@Param("eleveId") Long eleveId);
}

