package com.example.edugo.repository;

import com.example.edugo.entity.FichierLivre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichierLivreRepository extends JpaRepository<FichierLivre, Long> {
    @Query("SELECT f FROM FichierLivre f WHERE f.livre.id = :livreId")
    List<FichierLivre> findByLivreId(@Param("livreId") Long livreId);
}
