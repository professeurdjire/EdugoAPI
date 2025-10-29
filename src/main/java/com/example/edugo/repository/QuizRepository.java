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

    // Trouver un quiz par son ID
    Optional<Quiz> findById(Long id);

    //  Trouver le quiz d’un livre spécifique
    Optional<Quiz> findByLivre_Id(Long livreId);

    // Recherche avancée
   // @Query("SELECT q FROM Quiz q WHERE " +
            //"LOWER(q.titre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            //"LOWER(q.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
   // List<Quiz> searchByKeyword(@Param("searchTerm") String searchTerm);

    // Trouver un quiz avec ses questions
    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questionsQuiz WHERE q.id = :quizId")
    Optional<Quiz> findByIdWithQuestions(@Param("quizId") Long quizId);

}
