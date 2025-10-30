package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Defi;
import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.EleveDefi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EleveDefiRepository extends JpaRepository<EleveDefi, Long> {
    
    List<EleveDefi> findByEleve(Eleve eleve);
    
    List<EleveDefi> findByDefi(Defi defi);
    
    @Query("SELECT ed FROM EleveDefi ed WHERE ed.eleve.id = :eleveId")
    List<EleveDefi> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT ed FROM EleveDefi ed WHERE ed.defi.id = :defiId")
    List<EleveDefi> findByDefiId(@Param("defiId") Long defiId);
    
    @Query("SELECT ed FROM EleveDefi ed WHERE ed.eleve.id = :eleveId AND ed.defi.id = :defiId")
    Optional<EleveDefi> findByEleveIdAndDefiId(@Param("eleveId") Long eleveId, @Param("defiId") Long defiId);

    // Added for validation flows
    @Query("SELECT ed FROM EleveDefi ed WHERE ed.statut = :statut")
    List<EleveDefi> findByStatut(@Param("statut") String statut);

    @Query("SELECT COUNT(ed) FROM EleveDefi ed WHERE ed.eleve.id = :eleveId")
    Long countByEleveId(@Param("eleveId") Long eleveId);
}

