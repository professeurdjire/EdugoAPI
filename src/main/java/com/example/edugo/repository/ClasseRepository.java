package com.example.edugo.repository;

import com.example.edugo.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Trouver par nom
    Optional<Classe> findByNomClasse(String nomClasse);

    // Trouver les classes publiées
    List<Classe> findByEstPublieTrue();

    // Trouver les classes vérifiées
    List<Classe> findByEstVerifieTrue();

    // Trouver les classes populaires
    List<Classe> findByEstPopulaireTrue();

    // Trouver par niveau
    List<Classe> findByNiveau(String niveau);

    // Recherche par mot-clé
    @Query("SELECT c FROM Classe c WHERE " +
            "LOWER(c.nomClasse) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.motClef) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Classe> searchByKeyword(@Param("searchTerm") String searchTerm);

    // Trouver les classes d'un auteur
    List<Classe> findByAuteurId(Long auteurId);

    // Trouver les classes validées par un admin
    List<Classe> findByValidateurId(Long validateurId);

    // Compter les classes par statut
    @Query("SELECT COUNT(c) FROM Classe c WHERE c.estPublie = :publie AND c.estVerifie = :verifie")
    Long countByStatus(@Param("publie") Boolean publie, @Param("verifie") Boolean verifie);

    // Trouver les classes avec pagination et tri
    @Query("SELECT c FROM Classe c ORDER BY c.dateCreation DESC")
    List<Classe> findAllOrderByDateCreationDesc();

    // Trouver les classes avec leurs membres
    @Query("SELECT c FROM Classe c LEFT JOIN FETCH c.membres WHERE c.idClasse = :classeId")
    Optional<Classe> findByIdWithMembres(@Param("classeId") Long classeId);

    // Trouver les classes avec leurs quiz
    @Query("SELECT c FROM Classe c LEFT JOIN FETCH c.quizzes WHERE c.idClasse = :classeId")
    Optional<Classe> findByIdWithQuizzes(@Param("classeId") Long classeId);

    // Vérifier si un utilisateur est membre d'une classe
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Classe c JOIN c.membres m WHERE c.idClasse = :classeId AND m.id = :userId")
    Boolean isUserMemberOfClasse(@Param("classeId") Long classeId, @Param("userId") Long userId);
}
