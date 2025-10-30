package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Progression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressionRepository extends JpaRepository<Progression, Long> {
    
    List<Progression> findByEleve(Eleve eleve);
    
    List<Progression> findByLivre(Livre livre);
    
    @Query("SELECT p FROM Progression p WHERE p.eleve.id = :eleveId")
    List<Progression> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT p FROM Progression p WHERE p.livre.id = :livreId")
    List<Progression> findByLivreId(@Param("livreId") Long livreId);
    
    @Query("SELECT p FROM Progression p WHERE p.eleve.id = :eleveId AND p.livre.id = :livreId")
    Optional<Progression> findByEleveIdAndLivreId(@Param("eleveId") Long eleveId, @Param("livreId") Long livreId);

    @Query("SELECT COUNT(p) FROM Progression p WHERE p.eleve.id = :eleveId")
    Long countByEleveId(@Param("eleveId") Long eleveId);
}

