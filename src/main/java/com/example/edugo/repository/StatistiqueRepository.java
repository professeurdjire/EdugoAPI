package com.example.edugo.repository;

import com.example.edugo.entity.Statistique;
import com.example.edugo.entity.TypeStatistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {
    
    List<Statistique> findByPeriode(String periode);
    
    List<Statistique> findByIndicateur(String indicateur);
    
    @Query("SELECT s FROM Statistique s WHERE s.indicateur = :indicateur AND s.periode = :periode ORDER BY s.dateCalcule DESC")
    List<Statistique> findByIndicateurAndPeriodeOrderByDateCalculeDesc(@Param("indicateur") String indicateur, @Param("periode") String periode);
    
    @Query("SELECT s FROM Statistique s WHERE s.indicateur = :indicateur AND s.dateCalcule >= :startDate AND s.dateCalcule <= :endDate ORDER BY s.dateCalcule ASC")
    List<Statistique> findByIndicateurAndDateRange(@Param("indicateur") String indicateur, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT s FROM Statistique s WHERE s.periode = :periode AND s.dateCalcule = (SELECT MAX(s2.dateCalcule) FROM Statistique s2 WHERE s2.indicateur = s.indicateur AND s2.periode = :periode)")
    List<Statistique> findLatestByPeriode(@Param("periode") String periode);
    
    Optional<Statistique> findByIndicateurAndPeriodeAndDateCalcule(String indicateur, String periode, Date dateCalcule);
    
    @Query("SELECT COUNT(s) FROM Statistique s WHERE s.indicateur = :indicateur")
    Long countByIndicateur(@Param("indicateur") String indicateur);
}