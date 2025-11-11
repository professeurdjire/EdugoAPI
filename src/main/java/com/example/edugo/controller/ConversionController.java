package com.example.edugo.controller;

import com.example.edugo.dto.ConversionRequest;
import com.example.edugo.dto.ConversionResponse;
import com.example.edugo.dto.OptionsConversionResponse;
import com.example.edugo.service.ConversionService;
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
@RequestMapping("/api/conversions")
@RequiredArgsConstructor
@Tag(name = "Conversions", description = "Gestion de la conversion de points en forfaits data internet")
@SecurityRequirement(name = "bearerAuth")
public class ConversionController {

    private final ConversionService conversionService;

    // ==================== OPTIONS DE CONVERSION ====================
    
    @GetMapping("/options")
    @Operation(summary = "Récupérer toutes les options de conversion", 
               description = "Permet de récupérer toutes les options de conversion disponibles")
    public ResponseEntity<List<OptionsConversionResponse>> getAllOptions() {
        return ResponseEntity.ok(conversionService.getAllOptions());
    }

    @GetMapping("/options/actives")
    @Operation(summary = "Récupérer les options actives", 
               description = "Permet de récupérer uniquement les options de conversion actives")
    public ResponseEntity<List<OptionsConversionResponse>> getOptionsActives() {
        return ResponseEntity.ok(conversionService.getOptionsActives());
    }

    @GetMapping("/options/{id}")
    @Operation(summary = "Récupérer une option par ID", 
               description = "Permet de récupérer les détails d'une option de conversion")
    public ResponseEntity<OptionsConversionResponse> getOptionById(
            @Parameter(description = "ID de l'option") @PathVariable Long id) {
        return ResponseEntity.ok(conversionService.getOptionById(id));
    }

    // ==================== CONVERSION DE POINTS ====================
    
    @PostMapping("/convertir/{eleveId}")
    @Operation(summary = "Convertir des points en forfait data", 
               description = "Permet à un élève de convertir ses points en forfait data internet")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<ConversionResponse> convertirPoints(
            @Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
            @RequestBody ConversionRequest request) {
        return ResponseEntity.ok(conversionService.convertirPoints(eleveId, request));
    }

    // ==================== HISTORIQUE ====================
    
    @GetMapping("/historique/{eleveId}")
    @Operation(summary = "Récupérer l'historique des conversions", 
               description = "Permet de récupérer l'historique des conversions d'un élève")
    @PreAuthorize("hasAnyRole('ELEVE', 'ADMIN')")
    public ResponseEntity<List<ConversionResponse>> getHistoriqueConversions(
            @Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(conversionService.getHistoriqueConversions(eleveId));
    }
}

