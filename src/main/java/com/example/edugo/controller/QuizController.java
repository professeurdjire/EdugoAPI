package com.example.edugo.controller;

import com.example.edugo.dto.QuizResponse;
import com.example.edugo.entity.Principales.Quiz;
import com.example.edugo.entity.StatutQuiz;
import com.example.edugo.service.ServiceQuiz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@Tag(name = "Quizzes", description = "Gestion des quizzes")
@SecurityRequirement(name = "bearerAuth")
public class QuizController {

    private final ServiceQuiz serviceQuiz;

    // ==================== CRUD QUIZZES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les quizzes")
    public ResponseEntity<List<QuizResponse>> getAllQuizzes() {
        return ResponseEntity.ok(serviceQuiz.getAllQuizzesDto());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un quiz par ID")
    public ResponseEntity<QuizResponse> getQuizById(@Parameter(description = "ID du quiz") @PathVariable Long id) {
        return ResponseEntity.ok(serviceQuiz.getQuizByIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Créer un quiz")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(serviceQuiz.createQuiz(quiz));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un quiz")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizResponse> updateQuiz(@Parameter(description = "ID du quiz") @PathVariable Long id,
                                                    @RequestBody Quiz quizDetails) {
        return ResponseEntity.ok(serviceQuiz.updateQuiz(id, quizDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un quiz")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuiz(@Parameter(description = "ID du quiz") @PathVariable Long id) {
        serviceQuiz.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les quizzes par statut")
    public ResponseEntity<List<QuizResponse>> getQuizzesByStatut(@Parameter(description = "Statut du quiz") @PathVariable String statut) {
        StatutQuiz statutQuiz = StatutQuiz.valueOf(statut.toUpperCase());
        return ResponseEntity.ok(serviceQuiz.getQuizzesByStatut(statutQuiz));
    }
}

