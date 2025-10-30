package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    Optional <Livre> findByTitre(String titre);

    // Added for ELEVE/domain flows
    @Query("SELECT l FROM Livre l WHERE l.classe.id = :classeId OR ( :niveauId IS NOT NULL AND l.niveau.id = :niveauId )")
    List<Livre> findByClasseIdOrNiveauId(@Param("classeId") Long classeId, @Param("niveauId") Long niveauId);

    @Query("SELECT l FROM Livre l WHERE l.matiere.id = :matiereId")
    List<Livre> findByMatiereId(@Param("matiereId") Long matiereId);

    @Query("SELECT l FROM Livre l WHERE l.niveau.id = :niveauId")
    List<Livre> findByNiveauId(@Param("niveauId") Long niveauId);

    @Query("SELECT l FROM Livre l WHERE l.classe.id = :classeId")
    List<Livre> findByClasseId(@Param("classeId") Long classeId);

    @Query(value = "SELECT * FROM livres ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Livre> findTop10ByOrderByIdDesc();

    List<Livre> findByTitreContainingIgnoreCase(String titre);

    List<Livre> findByAuteurContainingIgnoreCase(String auteur);
}
