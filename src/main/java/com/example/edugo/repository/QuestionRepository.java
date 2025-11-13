package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByExerciceId(Long exerciceId);

    List<Question> findByChallengeId(Long challengeId);

    // Trouver les questions d'un quiz
    List<Question> findByQuizId(Long quizId);

    // Trouver les questions d'un défi
    List<Question> findByDefiId(Long defiId);

    // Trouver toutes les questions d’un type spécifique
    List<Question> findByType_Id(Long id);

    // Trouver les questions avec un nombre minimum de points
    List<Question> findByPointsGreaterThanEqual(Integer minPoints);

    // Compter les questions par quiz
    @Query("SELECT COUNT(q) FROM Question q WHERE q.quiz.id = :quizId")
    Long countByQuizId(@Param("quizId") Long quizId);

    // Trouver une question avec ses réponses
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles WHERE q.id = :questionId")
    Optional<Question> findByIdWithReponses(@Param("questionId") Long questionId);

    // Trouver la bonne réponse d'une question
    @Query("SELECT rp FROM ReponsePossible rp WHERE rp.question.id = :questionId AND rp.estCorrecte = true")
    List<Object[]> findCorrectReponsesByQuestionId(@Param("questionId") Long questionId);
}
