package com.example.edugo.repository;

import com.example.edugo.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    @Query("SELECT s FROM ChatSession s WHERE s.eleve.id = :eleveId ORDER BY s.updatedAt DESC")
    List<ChatSession> findByEleveId(@Param("eleveId") Long eleveId);
}
