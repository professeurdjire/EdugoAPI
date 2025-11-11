package com.example.edugo.controller;

import com.example.edugo.dto.LivreRequest;
import com.example.edugo.dto.LivreResponse;
import com.example.edugo.dto.LivreDetailResponse;
import com.example.edugo.entity.Principales.Livre;
import com.example.edugo.entity.Principales.Progression;
import com.example.edugo.service.ServiceLivre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.example.edugo.dto.LivrePopulaireResponse;
import com.example.edugo.dto.StatistiquesLivreResponse;
import com.example.edugo.dto.ProgressionUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/livres")
@RequiredArgsConstructor
@Tag(name = "Livres", description = "Gestion des livres et progression de lecture")
@SecurityRequirement(name = "bearerAuth")
public class LivreController {

    private final ServiceLivre serviceLivre;

    // ==================== CRUD LIVRES (ADMIN) ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les livres", description = "Permet de récupérer la liste de tous les livres")
    public ResponseEntity<List<LivreResponse>> getAllLivres() {
        return ResponseEntity.ok(serviceLivre.getAllLivres());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livre par ID", description = "Permet de récupérer les détails d'un livre")
    public ResponseEntity<LivreDetailResponse> getLivreById(@Parameter(description = "ID du livre") @PathVariable Long id) {
        return ResponseEntity.ok(serviceLivre.getLivreById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Créer un nouveau livre", description = "Permet à un administrateur de créer un nouveau livre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LivreResponse> createLivre(@RequestPart("livre") com.example.edugo.dto.LivreRequest livre,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(serviceLivre.createLivre(livre, image));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Mettre à jour un livre", description = "Permet à un administrateur de modifier un livre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LivreResponse> updateLivre(@Parameter(description = "ID du livre") @PathVariable Long id,
                                                     @RequestPart("livre") com.example.edugo.dto.LivreRequest livreDetails,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(serviceLivre.updateLivre(id, livreDetails, image));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre", description = "Permet à un administrateur de supprimer un livre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLivre(@Parameter(description = "ID du livre") @PathVariable Long id) {
        serviceLivre.deleteLivre(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LIVRES POUR ÉLÈVES ====================
    
    @GetMapping("/disponibles/{eleveId}")
    @Operation(summary = "Récupérer les livres disponibles pour un élève", description = "Permet de récupérer les livres adaptés au niveau d'un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<LivreResponse>> getLivresDisponibles(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceLivre.getLivresDisponibles(eleveId));
    }

    @GetMapping("/matiere/{matiereId}")
    @Operation(summary = "Récupérer les livres par matière", description = "Permet de récupérer les livres d'une matière spécifique")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<LivreResponse>> getLivresByMatiere(@Parameter(description = "ID de la matière") @PathVariable Long matiereId) {
        return ResponseEntity.ok(serviceLivre.getLivresByMatiere(matiereId));
    }

    @GetMapping("/niveau/{niveauId}")
    @Operation(summary = "Récupérer les livres par niveau", description = "Permet de récupérer les livres d'un niveau spécifique")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<LivreResponse>> getLivresByNiveau(@Parameter(description = "ID du niveau") @PathVariable Long niveauId) {
        return ResponseEntity.ok(serviceLivre.getLivresByNiveau(niveauId));
    }

    @GetMapping("/classe/{classeId}")
    @Operation(summary = "Récupérer les livres par classe", description = "Permet de récupérer les livres d'une classe spécifique")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<LivreResponse>> getLivresByClasse(@Parameter(description = "ID de la classe") @PathVariable Long classeId) {
        return ResponseEntity.ok(serviceLivre.getLivresByClasse(classeId));
    }

    // ==================== PROGRESSION DE LECTURE ====================
    
    @PostMapping("/progression/{eleveId}/{livreId}")
    @Operation(summary = "Mettre à jour la progression de lecture", description = "Permet de mettre à jour la page actuelle de lecture d'un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<com.example.edugo.dto.ProgressionResponse> updateProgressionLecture(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                               @Parameter(description = "ID du livre") @PathVariable Long livreId,
                                                               @RequestBody ProgressionUpdateRequest progressionData) {
        Integer pageActuelle = progressionData.getPageActuelle();
        return ResponseEntity.ok(serviceLivre.updateProgressionLecture(eleveId, livreId, pageActuelle));
    }

    @GetMapping("/progression/{eleveId}")
    @Operation(summary = "Récupérer la progression de lecture d'un élève", description = "Permet de récupérer l'historique de lecture d'un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<com.example.edugo.dto.ProgressionResponse>> getProgressionLecture(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceLivre.getProgressionLecture(eleveId));
    }

    @GetMapping("/progression/{eleveId}/{livreId}")
    @Operation(summary = "Récupérer la progression d'un livre spécifique", description = "Permet de récupérer la progression d'un élève pour un livre donné")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<com.example.edugo.dto.ProgressionResponse> getProgressionLivre(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId,
                                                          @Parameter(description = "ID du livre") @PathVariable Long livreId) {
        com.example.edugo.dto.ProgressionResponse progression = serviceLivre.getProgressionLivre(eleveId, livreId);
        return ResponseEntity.ok(progression);
    }

    // ==================== STATISTIQUES ET ANALYTICS ====================
    
    @GetMapping("/statistiques/{livreId}")
    @Operation(summary = "Récupérer les statistiques d'un livre", description = "Permet de récupérer les statistiques de lecture d'un livre")
    public ResponseEntity<StatistiquesLivreResponse> getStatistiquesLivre(@Parameter(description = "ID du livre") @PathVariable Long livreId) {
        return ResponseEntity.ok(serviceLivre.getStatistiquesLivre(livreId));
    }

    @GetMapping("/populaires")
    @Operation(summary = "Récupérer les livres populaires", description = "Permet de récupérer les livres les plus lus")
    public ResponseEntity<List<LivrePopulaireResponse>> getLivresPopulaires() {
        return ResponseEntity.ok(serviceLivre.getLivresPopulaires());
    }

    @GetMapping("/recommandes/{eleveId}")
    @Operation(summary = "Récupérer les livres recommandés", description = "Permet de récupérer les livres recommandés pour un élève")
    @PreAuthorize("hasRole('ELEVE')")
    public ResponseEntity<List<LivreResponse>> getLivresRecommandes(@Parameter(description = "ID de l'élève") @PathVariable Long eleveId) {
        return ResponseEntity.ok(serviceLivre.getLivresRecommandes(eleveId));
    }

    // ==================== RECHERCHE ET FILTRAGE ====================
    
    @GetMapping("/recherche/titre")
    @Operation(summary = "Rechercher des livres par titre", description = "Permet de rechercher des livres par titre")
    public ResponseEntity<List<LivreResponse>> searchLivresByTitre(@Parameter(description = "Titre à rechercher") @RequestParam String titre) {
        return ResponseEntity.ok(serviceLivre.searchLivresByTitre(titre));
    }

    @GetMapping("/recherche/auteur")
    @Operation(summary = "Rechercher des livres par auteur", description = "Permet de rechercher des livres par auteur")
    public ResponseEntity<List<LivreResponse>> searchLivresByAuteur(@Parameter(description = "Auteur à rechercher") @RequestParam String auteur) {
        return ResponseEntity.ok(serviceLivre.searchLivresByAuteur(auteur));
    }

    @GetMapping("/recents")
    @Operation(summary = "Récupérer les livres récents", description = "Permet de récupérer les livres les plus récemment ajoutés")
    public ResponseEntity<List<LivreResponse>> getLivresRecents() {
        return ResponseEntity.ok(serviceLivre.getLivresRecents());
    }
}

