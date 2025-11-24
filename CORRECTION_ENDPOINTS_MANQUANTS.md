# Correction des Endpoints Manquants

## ✅ Endpoints Ajoutés

### 1. `GET /api/admin/notifications`
- **Problème** : Le frontend essayait d'accéder à `/api/admin/notifications` qui n'existait pas
- **Solution** : Ajout de l'endpoint dans `AdminController`
- **Méthode** : `getAllNotifications()` dans `AdminService`
- **Retourne** : Liste de toutes les notifications

**Code ajouté :**
```java
@GetMapping("/notifications")
@Operation(summary = "Obtenir toutes les notifications")
public ResponseEntity<List<Notification>> getAllNotifications() {
    return ResponseEntity.ok(adminService.getAllNotifications());
}
```

### 2. `POST /api/eleve/{id}/objectifs`
- **Problème** : Le frontend essayait d'accéder à `/api/eleve/7/objectifs` qui n'existait pas
- **Solution** : Ajout de l'endpoint dans `EleveController`
- **Méthode** : Délègue à `ServiceObjectif.createObjectifDto()`
- **Retourne** : `ObjectifResponse`

**Code ajouté :**
```java
@PostMapping("/{id}/objectifs")
@Operation(summary = "Créer un objectif pour un élève", description = "Permet à un élève de créer un nouvel objectif")
@PreAuthorize("hasRole('ELEVE')")
public ResponseEntity<ObjectifResponse> createObjectif(
    @Parameter(description = "ID de l'élève") @PathVariable Long id,
    @RequestBody ObjectifRequest request) {
    ObjectifResponse response = serviceObjectif.createObjectifDto(request, id);
    return ResponseEntity.ok(response);
}
```

## 📋 Endpoints Existants (Alternatifs)

### Objectifs
L'endpoint suivant existe déjà dans `ObjectifController` :
- `POST /api/objectifs/eleve/{eleveId}` - Créer un objectif

Les deux endpoints sont maintenant disponibles :
- ✅ `POST /api/eleve/{id}/objectifs` (NOUVEAU - cohérent avec les autres endpoints élève)
- ✅ `POST /api/objectifs/eleve/{eleveId}` (EXISTANT)

## 🔐 Sécurité

Les endpoints ajoutés respectent la configuration de sécurité :
- `/api/admin/notifications` : Requiert le rôle `ADMIN` (via `@PreAuthorize("hasRole('ADMIN')")` sur `AdminController`)
- `/api/eleve/{id}/objectifs` : Requiert le rôle `ELEVE` (via `@PreAuthorize("hasRole('ELEVE')")` sur la méthode)

## ✅ Résultat

Tous les endpoints manquants ont été ajoutés et sont maintenant fonctionnels. Les erreurs `NoResourceFoundException` pour ces endpoints ne devraient plus se produire.

