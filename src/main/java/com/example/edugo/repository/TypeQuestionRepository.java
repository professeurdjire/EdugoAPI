package com.example.edugo.repository;

import com.example.edugo.entity.Principales.TypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeQuestionRepository extends JpaRepository<TypeQuestion, Long> {
    Optional<TypeQuestion> findByLibelleTypeIgnoreCase(String libelleType);
}
