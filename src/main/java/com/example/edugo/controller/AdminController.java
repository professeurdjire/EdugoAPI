package com.example.edugo.controller;

import com.example.edugo.entity.Principales.*;
import com.example.edugo.entity.User;
import com.example.edugo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administration", description = "Endpoints pour la gestion administrative complète de la plateforme (Réservé aux Administrateurs)")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    // ==================== UTILISATEURS ====================
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> request) {
        // Extract user data from request
        String nom = (String) request.get("nom");
        String prenom = (String) request.get("prenom");
        String email = (String) request.get("email");
        String motDePasse = (String) request.get("motDePasse");
        String role = (String) request.get("role");
        
        // For now, return a simple response
        // In a real scenario, you would create Eleve or Admin based on role
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "User creation logic should be implemented based on role");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        User user = adminService.getUserById(id);
        user.setNom((String) request.get("nom"));
        user.setPrenom((String) request.get("prenom"));
        user.setEmail((String) request.get("email"));
        
        if (request.containsKey("estActive")) {
            user.setEstActive((Boolean) request.get("estActive"));
        }
        
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== NIVEAUX ====================
    
    @GetMapping("/niveaux")
    public ResponseEntity<List<Niveau>> getAllNiveaux() {
        return ResponseEntity.ok(adminService.getAllNiveaux());
    }

    @GetMapping("/niveaux/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getNiveauById(id));
    }

    @PostMapping("/niveaux")
    public ResponseEntity<Niveau> createNiveau(@RequestBody Niveau niveau) {
        return ResponseEntity.ok(adminService.createNiveau(niveau));
    }

    @PutMapping("/niveaux/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable Long id, @RequestBody Niveau niveau) {
        return ResponseEntity.ok(adminService.updateNiveau(id, niveau));
    }

    @DeleteMapping("/niveaux/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        adminService.deleteNiveau(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CLASSES ====================
    
    @GetMapping("/classes")
    public ResponseEntity<List<Classe>> getAllClasses() {
        return ResponseEntity.ok(adminService.getAllClasses());
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<Classe> getClasseById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClasseById(id));
    }

    @PostMapping("/classes")
    public ResponseEntity<Classe> createClasse(@RequestBody Classe classe) {
        return ResponseEntity.ok(adminService.createClasse(classe));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<Classe> updateClasse(@PathVariable Long id, @RequestBody Classe classe) {
        return ResponseEntity.ok(adminService.updateClasse(id, classe));
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        adminService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== MATIERES ====================
    
    @GetMapping("/matieres")
    public ResponseEntity<List<Matiere>> getAllMatieres() {
        return ResponseEntity.ok(adminService.getAllMatieres());
    }

    @GetMapping("/matieres/{id}")
    public ResponseEntity<Matiere> getMatiereById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getMatiereById(id));
    }

    @PostMapping("/matieres")
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) {
        return ResponseEntity.ok(adminService.createMatiere(matiere));
    }

    @PutMapping("/matieres/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable Long id, @RequestBody Matiere matiere) {
        return ResponseEntity.ok(adminService.updateMatiere(id, matiere));
    }

    @DeleteMapping("/matieres/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        adminService.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LIVRES ====================
    
    @GetMapping("/livres")
    public ResponseEntity<List<Livre>> getAllLivres() {
        return ResponseEntity.ok(adminService.getAllLivres());
    }

    @GetMapping("/livres/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getLivreById(id));
    }

    @PostMapping("/livres")
    public ResponseEntity<Livre> createLivre(@RequestBody Livre livre) {
        return ResponseEntity.ok(adminService.createLivre(livre));
    }

    @PutMapping("/livres/{id}")
    public ResponseEntity<Livre> updateLivre(@PathVariable Long id, @RequestBody Livre livre) {
        return ResponseEntity.ok(adminService.updateLivre(id, livre));
    }

    @DeleteMapping("/livres/{id}")
    public ResponseEntity<Void> deleteLivre(@PathVariable Long id) {
        adminService.deleteLivre(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== EXERCICES ====================
    
    @GetMapping("/exercices")
    public ResponseEntity<List<Exercice>> getAllExercices() {
        return ResponseEntity.ok(adminService.getAllExercices());
    }

    @GetMapping("/exercices/{id}")
    public ResponseEntity<Exercice> getExerciceById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getExerciceById(id));
    }

    @PostMapping("/exercices")
    public ResponseEntity<Exercice> createExercice(@RequestBody Exercice exercice) {
        return ResponseEntity.ok(adminService.createExercice(exercice));
    }

    @PutMapping("/exercices/{id}")
    public ResponseEntity<Exercice> updateExercice(@PathVariable Long id, @RequestBody Exercice exercice) {
        return ResponseEntity.ok(adminService.updateExercice(id, exercice));
    }

    @DeleteMapping("/exercices/{id}")
    public ResponseEntity<Void> deleteExercice(@PathVariable Long id) {
        adminService.deleteExercice(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== DEFIS ====================
    
    @GetMapping("/defis")
    public ResponseEntity<List<Defi>> getAllDefis() {
        return ResponseEntity.ok(adminService.getAllDefis());
    }

    @GetMapping("/defis/{id}")
    public ResponseEntity<Defi> getDefiById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDefiById(id));
    }

    @PostMapping("/defis")
    public ResponseEntity<Defi> createDefi(@RequestBody Defi defi) {
        return ResponseEntity.ok(adminService.createDefi(defi));
    }

    @PutMapping("/defis/{id}")
    public ResponseEntity<Defi> updateDefi(@PathVariable Long id, @RequestBody Defi defi) {
        return ResponseEntity.ok(adminService.updateDefi(id, defi));
    }

    @DeleteMapping("/defis/{id}")
    public ResponseEntity<Void> deleteDefi(@PathVariable Long id) {
        adminService.deleteDefi(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CHALLENGES ====================
    
    @GetMapping("/challenges")
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        return ResponseEntity.ok(adminService.getAllChallenges());
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<Challenge> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getChallengeById(id));
    }

    @PostMapping("/challenges")
    public ResponseEntity<Challenge> createChallenge(@RequestBody Challenge challenge) {
        return ResponseEntity.ok(adminService.createChallenge(challenge));
    }

    @PutMapping("/challenges/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable Long id, @RequestBody Challenge challenge) {
        return ResponseEntity.ok(adminService.updateChallenge(id, challenge));
    }

    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        adminService.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== BADGES ====================
    
    @GetMapping("/badges")
    public ResponseEntity<List<Badge>> getAllBadges() {
        return ResponseEntity.ok(adminService.getAllBadges());
    }

    @GetMapping("/badges/{id}")
    public ResponseEntity<Badge> getBadgeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getBadgeById(id));
    }

    @PostMapping("/badges")
    public ResponseEntity<Badge> createBadge(@RequestBody Badge badge) {
        return ResponseEntity.ok(adminService.createBadge(badge));
    }

    @PutMapping("/badges/{id}")
    public ResponseEntity<Badge> updateBadge(@PathVariable Long id, @RequestBody Badge badge) {
        return ResponseEntity.ok(adminService.updateBadge(id, badge));
    }

    @DeleteMapping("/badges/{id}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id) {
        adminService.deleteBadge(id);
        return ResponseEntity.noContent().build();
    }
}

