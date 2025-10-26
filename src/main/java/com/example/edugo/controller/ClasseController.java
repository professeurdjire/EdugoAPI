package com.example.edugo.controller;

import com.example.edugo.dto.ClasseRequest;
import com.example.edugo.dto.ClasseResponse;
import com.example.edugo.service.ServiceClasse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClasseController {

    private final ServiceClasse classeService;

    public ClasseController(ServiceClasse classeService) {
        this.classeService = classeService;
    }

    /**
     * Lister toutes les classes
     * Accessible à tout utilisateur authentifié
     */
    @GetMapping
    public ResponseEntity<List<ClasseResponse>> getAllClasses() {
        List<ClasseResponse> classes = classeService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    /**
     * Récupérer une classe spécifique par ID
     * Accessible à tout utilisateur authentifié
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClasseResponse> getClasseById(@PathVariable Long id) {
        ClasseResponse response = classeService.getClasseById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Création d'une nouvelle classe
     * Seulement pour les ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClasseResponse> createClasse(@RequestBody ClasseRequest request) {
        ClasseResponse response = classeService.createClasse(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Modification  d'une classe existante
     * Seulement pour les ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ClasseResponse> updateClasse(@PathVariable Long id, @RequestBody ClasseRequest request) {
        ClasseResponse response = classeService.updateClasse(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Suppression d'une classe
     * Seulement pour les ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }
}
