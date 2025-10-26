package com.example.edugo.controller;

import com.example.edugo.dto.NiveauRequest;
import com.example.edugo.dto.NiveauResponse;
import com.example.edugo.service.ServiceNiveau;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    private final ServiceNiveau niveauService;

    public NiveauController(ServiceNiveau niveauService) {
        this.niveauService = niveauService;
    }

    // Créer un nouveau niveau (seulement ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<NiveauResponse> createNiveau(@RequestBody NiveauRequest request) {
        NiveauResponse response = niveauService.createNiveau(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Récupérer tous les niveaux (accessible à tous)
    @GetMapping
    public ResponseEntity<List<NiveauResponse>> getAllNiveaux() {
        List<NiveauResponse> niveaux = niveauService.getAllNiveaux();
        return ResponseEntity.ok(niveaux);
    }

    // Récupérer un niveau par ID (accessible à tous)
    @GetMapping("/{id}")
    public ResponseEntity<NiveauResponse> getNiveauById(@PathVariable Long id) {
        NiveauResponse response = niveauService.getNiveauById( id);
        return ResponseEntity.ok(response);
    }

    // Modifier un niveau (seulement ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<NiveauResponse> updateNiveau(
            @PathVariable Long id,
            @RequestBody NiveauRequest request
    ) {
        NiveauResponse response = niveauService.updateNiveau(id, request);
        return ResponseEntity.ok(response);
    }

    // Supprimer un niveau (seulement ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        niveauService.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // Supprimer un niveau et renvoyer les infos supprimées (optionnel)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<NiveauResponse> removeNiveau(@PathVariable Long id) {
        NiveauResponse deleted = niveauService.removeNiveau(id);
        return ResponseEntity.ok(deleted);
    }
}
