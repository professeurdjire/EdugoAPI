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

    // Added for ELEVE/domain flows - avec relations chargées
    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE l.classe.id = :classeId OR ( :niveauId IS NOT NULL AND l.niveau.id = :niveauId )")
    List<Livre> findByClasseIdOrNiveauId(@Param("classeId") Long classeId, @Param("niveauId") Long niveauId);

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE l.matiere.id = :matiereId")
    List<Livre> findByMatiereId(@Param("matiereId") Long matiereId);

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE l.niveau.id = :niveauId")
    List<Livre> findByNiveauId(@Param("niveauId") Long niveauId);

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE l.classe.id = :classeId")
    List<Livre> findByClasseId(@Param("classeId") Long classeId);

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz ORDER BY l.id DESC")
    List<Livre> findTop10ByOrderByIdDesc();

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE LOWER(l.titre) LIKE LOWER(CONCAT('%', :titre, '%'))")
    List<Livre> findByTitreContainingIgnoreCase(@Param("titre") String titre);

    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE LOWER(l.auteur) LIKE LOWER(CONCAT('%', :auteur, '%'))")
    List<Livre> findByAuteurContainingIgnoreCase(@Param("auteur") String auteur);

    Optional<Livre> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);
    
    // Charger un livre avec toutes ses relations nécessaires pour les DTOs
    @Query("SELECT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz WHERE l.id = :id")
    Optional<Livre> findByIdWithRelations(@Param("id") Long id);
    
    // Charger tous les livres avec leurs relations
    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.niveau LEFT JOIN FETCH l.classe LEFT JOIN FETCH l.matiere LEFT JOIN FETCH l.langue LEFT JOIN FETCH l.quiz")
    List<Livre> findAllWithRelations();
}
