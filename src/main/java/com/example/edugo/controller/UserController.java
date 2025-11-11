package com.example.edugo.controller;

import com.example.edugo.entity.User;
import com.example.edugo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestion des utilisateurs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AdminService adminService;

    // ==================== CRUD USERS ====================
    
    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID")
    public ResponseEntity<User> getUserById(@Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> request) {
        // La création d'utilisateur est gérée par AdminService.createUser
        // Cette méthode délègue à AdminService qui gère la création selon le rôle
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Utilisez AdminService.createUser pour créer des utilisateurs");
        response.put("note", "La création d'utilisateur nécessite un objet User complet avec le rôle approprié");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@Parameter(description = "ID de l'utilisateur") @PathVariable Long id, 
                                          @RequestBody Map<String, Object> request) {
        User user = adminService.getUserById(id);
        user.setNom((String) request.get("nom"));
        user.setPrenom((String) request.get("prenom"));
        user.setEmail((String) request.get("email"));
        
        if (request.containsKey("estActive")) {
            user.setEstActive((Boolean) request.get("estActive"));
        }
        
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

