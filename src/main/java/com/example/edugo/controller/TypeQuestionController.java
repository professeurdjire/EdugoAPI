package com.example.edugo.controller;

import com.example.edugo.entity.Principales.TypeQuestion;
import com.example.edugo.repository.TypeQuestionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/type-questions")
@Tag(name = "TypeQuestions", description = "Types de question pour formulaires dynamiques")
public class TypeQuestionController {

    private final TypeQuestionRepository typeQuestionRepository;

    public TypeQuestionController(TypeQuestionRepository typeQuestionRepository) {
        this.typeQuestionRepository = typeQuestionRepository;
    }

    @GetMapping
    @Operation(summary = "Lister les types de questions")
    public ResponseEntity<List<TypeQuestionDto>> list() {
        List<TypeQuestionDto> res = typeQuestionRepository.findAll().stream()
                .map(t -> new TypeQuestionDto(t.getId(), t.getLibelleType()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    public static class TypeQuestionDto {
        private Long id;
        private String libelle;

        public TypeQuestionDto() {}
        public TypeQuestionDto(Long id, String libelle) {
            this.id = id;
            this.libelle = libelle;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }
    }
}
