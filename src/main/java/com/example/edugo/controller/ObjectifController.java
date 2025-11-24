package com.example.edugo.controller;

import com.example.edugo.dto.ObjectifRequest;
import com.example.edugo.dto.ObjectifResponse;
import com.example.edugo.service.ServiceObjectif;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objectifs")
@RequiredArgsConstructor
public class ObjectifController {

    private final ServiceObjectif serviceObjectif;

    @PostMapping("/eleve/{eleveId}")
    public ResponseEntity<ObjectifResponse> createObjectif(
            @RequestBody ObjectifRequest request,
            @PathVariable Long eleveId) {

        ObjectifResponse response = serviceObjectif.createObjectifDto(request, eleveId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eleve/{eleveId}/en-cours")
    public ResponseEntity<ObjectifResponse> getObjectifEnCours(@PathVariable Long eleveId) {
        ObjectifResponse objectif = serviceObjectif.getObjectifEnCoursDto(eleveId);
        if (objectif == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(objectif);
    }

    @GetMapping("/eleve/{eleveId}/tous")
    public ResponseEntity<List<ObjectifResponse>> getObjectifsByEleve(@PathVariable Long eleveId) {
        List<ObjectifResponse> objectifs = serviceObjectif.getObjectifsByEleveDto(eleveId);
        return ResponseEntity.ok(objectifs);
    }

    @GetMapping("/{id}/eleve/{eleveId}")
    public ResponseEntity<ObjectifResponse> getObjectifById(
            @PathVariable Long id,
            @PathVariable Long eleveId) {

        ObjectifResponse objectif = serviceObjectif.getObjectifByIdDto(id, eleveId);
        return ResponseEntity.ok(objectif);
    }

    @GetMapping("/eleve/{eleveId}/historique")
    public ResponseEntity<List<ObjectifResponse>> getHistoriqueObjectifs(@PathVariable Long eleveId) {
        List<ObjectifResponse> objectifs = serviceObjectif.getHistoriqueObjectifsDto(eleveId);
        return ResponseEntity.ok(objectifs);
    }

    @DeleteMapping("/{id}/eleve/{eleveId}")
    public ResponseEntity<Void> deleteObjectif(
            @PathVariable Long id,
            @PathVariable Long eleveId) {

        serviceObjectif.deleteObjectif(id, eleveId);
        return ResponseEntity.noContent().build();
    }
}