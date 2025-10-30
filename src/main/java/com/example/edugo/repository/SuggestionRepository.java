package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Eleve;
import com.example.edugo.entity.Principales.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    
    List<Suggestion> findByEleve(Eleve eleve);
    
    @Query("SELECT s FROM Suggestion s WHERE s.eleve.id = :eleveId")
    List<Suggestion> findByEleveId(@Param("eleveId") Long eleveId);
    
    @Query("SELECT s FROM Suggestion s ORDER BY s.dateEnvoie DESC")
    List<Suggestion> findAllOrderByDateDesc();
}

