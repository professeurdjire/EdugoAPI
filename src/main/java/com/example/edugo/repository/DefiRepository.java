package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Classe;
import com.example.edugo.entity.Principales.Defi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DefiRepository extends JpaRepository<Defi, Long> {
    
    List<Defi> findByClasse(Classe classe);
    
    @Query("SELECT d FROM Defi d WHERE d.classe.id = :classeId")
    List<Defi> findByClasseId(@Param("classeId") Long classeId);
    
    @Query("SELECT d FROM Defi d ORDER BY d.dateAjout DESC")
    List<Defi> findAllOrderByDateDesc();
    
    @Query("SELECT d FROM Defi d WHERE d.typeDefi = :typeDefi")
    List<Defi> findByTypeDefi(@Param("typeDefi") String typeDefi);
    
    @Query("SELECT d FROM Defi d WHERE " +
           "LOWER(d.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.ennonce) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Defi> searchByTitreOrEnonce(@Param("searchTerm") String searchTerm);
}

