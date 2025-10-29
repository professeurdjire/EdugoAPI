package com.example.edugo.repository;

import com.example.edugo.entity.Principales.Quiz;
import com.example.edugo.entity.Principales.TypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TypeQuestionRepository extends JpaRepository<TypeQuestion, Long> {
}
