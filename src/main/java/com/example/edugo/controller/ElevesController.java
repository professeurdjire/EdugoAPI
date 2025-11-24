package com.example.edugo.controller;

import com.example.edugo.dto.QuizResponse;
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
@RequestMapping("/api/eleves")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
@Tag(name = "Élèves", description = "Endpoints pour les fonctionnalités élèves (format pluriel)")
@SecurityRequirement(name = "bearerAuth")
public class ElevesController {

    private final ServiceQuiz serviceQuiz;

    @GetMapping("/{id}/quizzes")
    @Operation(summary = "Récupérer les quizzes disponibles pour un élève", description = "Permet de récupérer les quizzes adaptés au niveau/classe d'un élève")
    public ResponseEntity<List<QuizResponse>> getQuizzesDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long id) {
        return ResponseEntity.ok(serviceQuiz.getQuizzesDisponibles(id));
    }
}

