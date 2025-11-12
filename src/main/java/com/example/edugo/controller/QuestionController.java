package com.example.edugo.controller;

import com.example.edugo.dto.QuestionRequest;
import com.example.edugo.dto.QuestionResponse;
import com.example.edugo.service.ServiceQuestion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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
}
