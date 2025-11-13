package com.example.edugo.controller;

import com.example.edugo.dto.SubmitRequest;
import com.example.edugo.dto.SubmitResultResponse;
import com.example.edugo.service.ServiceEvaluation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Evaluation", description = "Soumission et évaluation des quiz")
public class EvaluationController {

    private final ServiceEvaluation serviceEvaluation;

    public EvaluationController(ServiceEvaluation serviceEvaluation) {
        this.serviceEvaluation = serviceEvaluation;
    }

    @PostMapping("/quizzes/{quizId}/submit")
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Soumettre les réponses d'un quiz (QCU/QCM/VRAI_FAUX)")
    public ResponseEntity<SubmitResultResponse> submitQuiz(@PathVariable Long quizId, @RequestBody SubmitRequest request) {
        return ResponseEntity.ok(serviceEvaluation.submitQuiz(quizId, request));
    }

    @PostMapping("/challenges/{challengeId}/submit")
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Soumettre les réponses d'un challenge (QCU/QCM/VRAI_FAUX)")
    public ResponseEntity<SubmitResultResponse> submitChallenge(@PathVariable Long challengeId, @RequestBody SubmitRequest request) {
        return ResponseEntity.ok(serviceEvaluation.submitChallenge(challengeId, request));
    }

    @PostMapping("/exercices/{exerciceId}/submit")
    @PreAuthorize("hasRole('ELEVE')")
    @Operation(summary = "Soumettre les réponses d'un exercice (QCU/QCM/VRAI_FAUX)")
    public ResponseEntity<SubmitResultResponse> submitExercice(@PathVariable Long exerciceId, @RequestBody SubmitRequest request) {
        return ResponseEntity.ok(serviceEvaluation.submitExercice(exerciceId, request));
    }
}
