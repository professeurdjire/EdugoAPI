package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Trouver par titre
    List<Quiz> findByTitreContainingIgnoreCase(String titre);

    // Trouver les quiz publiés
    List<Quiz> findByEstPublieTrue();

    // Trouver les quiz d'une classe
    List<Quiz> findByClasseIdClasse(Long classeId);

    // Trouver les quiz publiés d'une classe
    List<Quiz> findByClasseIdClasseAndEstPublieTrue(Long classeId);

    // Recherche avancée
    @Query("SELECT q FROM Quiz q WHERE " +
            "LOWER(q.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(q.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Quiz> searchByKeyword(@Param("searchTerm") String searchTerm);

    // Compter les quiz par classe
    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.classe.idClasse = :classeId")
    Long countByClasseId(@Param("classeId") Long classeId);

    // Trouver un quiz avec ses questions
    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questionsQuiz WHERE q.id = :quizId")
    Optional<Quiz> findByIdWithQuestions(@Param("quizId") Long quizId);

    // Trouver les quiz par durée maximale
    List<Quiz> findByDureeLessThanEqual(Integer dureeMax);
}
