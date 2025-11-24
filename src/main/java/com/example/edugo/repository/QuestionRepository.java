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

    // Trouver les questions d'un exercice avec leurs réponses
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles LEFT JOIN FETCH q.type WHERE q.exercice.id = :exerciceId")
    List<Question> findByExerciceId(@Param("exerciceId") Long exerciceId);

    // Trouver les questions d'un challenge avec leurs réponses
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles LEFT JOIN FETCH q.type WHERE q.challenge.id = :challengeId")
    List<Question> findByChallengeId(@Param("challengeId") Long challengeId);

    // Trouver les questions d'un quiz avec leurs réponses
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles LEFT JOIN FETCH q.type WHERE q.quiz.id = :quizId")
    List<Question> findByQuizId(@Param("quizId") Long quizId);

    // Trouver les questions d'un défi avec leurs réponses
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles LEFT JOIN FETCH q.type WHERE q.defi.id = :defiId")
    List<Question> findByDefiId(@Param("defiId") Long defiId);

    // Trouver toutes les questions d’un type spécifique
    List<Question> findByType_Id(Long id);

    // Trouver les questions avec un nombre minimum de points
    List<Question> findByPointsGreaterThanEqual(Integer minPoints);

    // Compter les questions par quiz
    @Query("SELECT COUNT(q) FROM Question q WHERE q.quiz.id = :quizId")
    Long countByQuizId(@Param("quizId") Long quizId);
    
    // Compter les questions par challenge (optimisé)
    @Query("SELECT COUNT(q) FROM Question q WHERE q.challenge.id = :challengeId")
    Long countByChallengeId(@Param("challengeId") Long challengeId);
    
    // Compter les questions par défi (optimisé)
    @Query("SELECT COUNT(q) FROM Question q WHERE q.defi.id = :defiId")
    Long countByDefiId(@Param("defiId") Long defiId);
    
    // Compter les questions par exercice (optimisé)
    @Query("SELECT COUNT(q) FROM Question q WHERE q.exercice.id = :exerciceId")
    Long countByExerciceId(@Param("exerciceId") Long exerciceId);
    
    // Compter les questions pour plusieurs challenges en une seule requête (batch)
    @Query("SELECT q.challenge.id, COUNT(q) FROM Question q WHERE q.challenge.id IN :challengeIds GROUP BY q.challenge.id")
    List<Object[]> countByChallengeIds(@Param("challengeIds") List<Long> challengeIds);
    
    // Compter les questions pour plusieurs défis en une seule requête (batch)
    @Query("SELECT q.defi.id, COUNT(q) FROM Question q WHERE q.defi.id IN :defiIds GROUP BY q.defi.id")
    List<Object[]> countByDefiIds(@Param("defiIds") List<Long> defiIds);

    // Trouver une question avec ses réponses
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.reponsesPossibles WHERE q.id = :questionId")
    Optional<Question> findByIdWithReponses(@Param("questionId") Long questionId);

    // Trouver la bonne réponse d'une question
    @Query("SELECT rp FROM ReponsePossible rp WHERE rp.question.id = :questionId AND rp.estCorrecte = true")
    List<Object[]> findCorrectReponsesByQuestionId(@Param("questionId") Long questionId);
}
