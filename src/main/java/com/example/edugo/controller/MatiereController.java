package com.example.edugo.controller;

import com.example.edugo.dto.MatiereRequest;
import com.example.edugo.dto.MatiereResponse;
import com.example.edugo.service.ServiceMatiere;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/matieres")
@RequiredArgsConstructor
public class MatiereController {

    private final ServiceMatiere serviceMatiere;

    // -------------------- CREATE --------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MatiereResponse> createMatiere(@Valid @RequestBody MatiereRequest request) {
        MatiereResponse response = serviceMatiere.createMatiere(request);
        return ResponseEntity.ok(response);
    }

    // -------------------- READ (All) --------------------
    @GetMapping
    public ResponseEntity<List<MatiereResponse>> getAllMatieres() {
        List<MatiereResponse> matieres = serviceMatiere.getAllMatieres();
        return ResponseEntity.ok(matieres);
    }

    // -------------------- READ (By ID) --------------------
    @GetMapping("/{id}")
    public ResponseEntity<MatiereResponse> getMatiereById(@PathVariable Long id) {
        MatiereResponse response = serviceMatiere.getMatiereById(id);
        return ResponseEntity.ok(response);
    }

    // -------------------- UPDATE --------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MatiereResponse> updateMatiere(
            @PathVariable Long id,
            @Valid @RequestBody MatiereRequest request
    ) {
        MatiereResponse response = serviceMatiere.updateMatiere(id, request);
        return ResponseEntity.ok(response);
    }

    // -------------------- DELETE --------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        serviceMatiere.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }
}
