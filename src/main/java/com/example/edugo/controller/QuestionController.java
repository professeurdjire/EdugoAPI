package com.example.edugo.controller;

import com.example.edugo.dto.QuestionRequest;
import com.example.edugo.dto.QuestionResponse;
import com.example.edugo.service.ServiceQuestion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Gestion unifiée des questions pour Quiz, Exercices, Challenges")
public class QuestionController {

    private final ServiceQuestion serviceQuestion;

    public QuestionController(ServiceQuestion serviceQuestion) {
        this.serviceQuestion = serviceQuestion;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer une question (QCU/QCM/VRAI_FAUX) pour un Quiz/Exercice/Challenge")
    public ResponseEntity<QuestionResponse> create(@RequestBody QuestionRequest request) {
        return ResponseEntity.ok(serviceQuestion.create(request));
    }

    @GetMapping("/by-quiz/{quizId}")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    @Operation(summary = "Lister les questions d'un quiz", description = "Accessible aux élèves et admins. Les réponses correctes sont masquées pour les élèves.")
    public ResponseEntity<List<QuestionResponse>> listByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(serviceQuestion.listByQuiz(quizId));
    }

    @GetMapping("/by-exercices/{exerciceId}")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    @Operation(summary = "Lister les questions d'un exercice", description = "Accessible aux élèves et admins. Les réponses correctes sont masquées pour les élèves.")
    public ResponseEntity<List<QuestionResponse>> listByExercice(@PathVariable Long exerciceId) {
        return ResponseEntity.ok(serviceQuestion.listByExercice(exerciceId));
    }

    @GetMapping("/by-challenges/{challengeId}")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    @Operation(summary = "Lister les questions d'un challenge", description = "Accessible aux élèves et admins. Les réponses correctes sont masquées pour les élèves.")
    public ResponseEntity<List<QuestionResponse>> listByChallenge(@PathVariable Long challengeId) {
        return ResponseEntity.ok(serviceQuestion.listByChallenge(challengeId));
    }

    @GetMapping("/by-defis/{defiId}")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    @Operation(summary = "Lister les questions d'un défi", description = "Accessible aux élèves et admins. Les réponses correctes sont masquées pour les élèves.")
    public ResponseEntity<List<QuestionResponse>> listByDefi(@PathVariable Long defiId) {
        return ResponseEntity.ok(serviceQuestion.listByDefi(defiId));
    }
}
