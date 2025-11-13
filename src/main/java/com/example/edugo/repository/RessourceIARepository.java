package com.example.edugo.repository;

import com.example.edugo.entity.RessourceIA;
import com.example.edugo.entity.RessourceIA.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RessourceIARepository extends JpaRepository<RessourceIA, Long> {
    @Query("SELECT r FROM RessourceIA r WHERE (:eleveId IS NULL OR r.eleve.id = :eleveId) AND (:livreId IS NULL OR r.livre.id = :livreId) AND (:type IS NULL OR r.type = :type) ORDER BY r.createdAt DESC")
    List<RessourceIA> search(@Param("eleveId") Long eleveId,
                             @Param("livreId") Long livreId,
                             @Param("type") Type type);
}
