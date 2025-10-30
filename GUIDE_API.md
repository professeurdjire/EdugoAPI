# Guide d'Utilisation de l'API EDUGO

## 🔑 Authentification

### 1. Connexion (Login)
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@edugo.com",
  "motDePasse": "admin123"
}
```

**Réponse:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "admin@edugo.com",
  "nom": "Admin",
  "prenom": "Principal",
  "role": "ADMIN",
  "id": 1
}
```

### 2. Rafraîchir le Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Inscription d'un Élève
```http
POST /api/auth/register
Content-Type: application/json

{
  "nom": "Doe",
  "prenom": "John",
  "email": "john.doe@example.com",
  "motDePasse": "password123",
  "classeId": 1,
  "dateNaissance": "2010-05-15"
}
```

## 🔒 Utilisation du Token

Tous les endpoints protégés nécessitent le token dans le header :

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 👨‍💼 Endpoints Admin

### Gestion des Niveaux

#### Lister tous les niveaux
```http
GET /api/admin/niveaux
Authorization: Bearer {token}
```

#### Créer un niveau
```http
POST /api/admin/niveaux
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "CP1"
}
```

#### Modifier un niveau
```http
PUT /api/admin/niveaux/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "CP2"
}
```

#### Supprimer un niveau
```http
DELETE /api/admin/niveaux/{id}
Authorization: Bearer {token}
```

### Gestion des Classes

#### Créer une classe
```http
POST /api/admin/classes
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "6ème A",
  "niveau": {
    "id": 1
  }
}
```

### Gestion des Matières

#### Créer une matière
```http
POST /api/admin/matieres
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Mathématiques"
}
```

### Gestion des Livres

#### Créer un livre
```http
POST /api/admin/livres
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Manuel de Mathématiques",
  "auteur": "Auteur Exemple",
  "editeur": "Éditeur Exemple",
  "totalPages": 250,
  "description": "Description du livre",
  "niveau": {
    "id": 1
  },
  "classe": {
    "id": 1
  },
  "matiere": {
    "id": 1
  }
}
```

### Gestion des Exercices

#### Créer un exercice
```http
POST /api/admin/exercices
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Exercice sur les fractions",
  "description": "Complétez les fractions suivantes",
  "niveauDifficulte": 3,
  "tempsAlloue": 30,
  "active": true,
  "matiere": {
    "id": 1
  },
  "niveauScolaire": {
    "id": 1
  }
}
```

### Gestion des Défis

#### Créer un défi
```http
POST /api/admin/defis
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Défi Mathématique",
  "ennonce": "Résolvez cette équation",
  "pointDefi": 100,
  "typeDefi": "QUOTIDIEN",
  "classe": {
    "id": 1
  }
}
```

### Gestion des Challenges

#### Créer un challenge
```http
POST /api/admin/challenges
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Challenge Mensuel",
  "description": "Challenge mensuel de mathématiques",
  "dateDebut": "2024-01-01T00:00:00",
  "dateFin": "2024-01-31T23:59:59",
  "rewardMode": "BADGE",
  "niveau": {
    "id": 1
  }
}
```

### Gestion des Badges

#### Créer un badge
```http
POST /api/admin/badges
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Expert Math",
  "description": "Badge pour les experts en mathématiques",
  "type": "GOLD",
  "icone": "icon_url"
}
```

## 👨‍🎓 Endpoints Élève

### Profil

#### Obtenir le profil
```http
GET /api/eleve/profil/{id}
Authorization: Bearer {token}
```

### Livres

#### Lister tous les livres
```http
GET /api/eleve/livres
Authorization: Bearer {token}
```

#### Détails d'un livre
```http
GET /api/eleve/livres/{id}
Authorization: Bearer {token}
```

### Exercices

#### Lister tous les exercices
```http
GET /api/eleve/exercices
Authorization: Bearer {token}
```

### Progressions

#### Lister les progressions d'un élève
```http
GET /api/eleve/progressions/eleve/{eleveId}
Authorization: Bearer {token}
```

#### Créer/Mettre à jour une progression
```http
PUT /api/eleve/progressions/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "pourcentageCompletion": 75,
  "pageActuelle": 180,
  "tempsLecture": 120
}
```

### Notifications

#### Obtenir les notifications
```http
GET /api/eleve/notifications/eleve/{eleveId}
Authorization: Bearer {token}
```

#### Marquer une notification comme vue
```http
PUT /api/eleve/notifications/{id}/marquer-vu
Authorization: Bearer {token}
```

### Suggestions

#### Créer une suggestion
```http
POST /api/eleve/suggestions
Authorization: Bearer {token}
Content-Type: application/json

{
  "contenu": "Je suggère d'ajouter plus d'exercices en algèbre",
  "eleve": {
    "id": 1
  }
}
```

### Objectifs

#### Créer un objectif
```http
POST /api/eleve/objectifs
Authorization: Bearer {token}
Content-Type: application/json

{
  "typeObjectif": "LIRE_5_LIVRES",
  "nbreLivre": 5,
  "progression": 0.0,
  "eleve": {
    "id": 1
  }
}
```

## 🔑 Codes d'État HTTP

- **200 OK** : Succès
- **201 Created** : Ressource créée
- **204 No Content** : Succès sans contenu
- **400 Bad Request** : Requête invalide
- **401 Unauthorized** : Non authentifié
- **403 Forbidden** : Non autorisé
- **404 Not Found** : Ressource non trouvée
- **409 Conflict** : Ressource déjà existante
- **500 Internal Server Error** : Erreur serveur

## ⚠️ Gestion des Erreurs

Toutes les erreurs renvoient un format JSON :

```json
{
  "message": "Message d'erreur en français",
  "status": "ERROR",
  "errors": {
    "field": "Message de validation"
  }
}
```

## 🛠️ Configuration

### Variables d'environnement (application.properties)
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/edugodatabase
spring.datasource.username=root
spring.datasource.password=

# JWT
app.jwt.secret=mySecretKeyForJWTTokenGenerationThatShouldBeLongEnough
app.jwt.expiration=86400000
app.jwt.refresh-expiration=604800000

# File Upload
app.file.upload-dir=./uploads
app.file.max-size=10485760
```

## 🚀 Démarrage Rapide

1. **Démarrer MySQL** et créer la base `edugodatabase`
2. **Lancer l'application** : `mvn spring-boot:run`
3. **Se connecter** avec admin@edugo.com / admin123
4. **Utiliser le token** dans toutes les requêtes

## 📝 Exemples avec cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@edugo.com","motDePasse":"admin123"}'

# Lister les niveaux
curl -X GET http://localhost:8080/api/admin/niveaux \
  -H "Authorization: Bearer YOUR_TOKEN"

# Créer un niveau
curl -X POST http://localhost:8080/api/admin/niveaux \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nom":"CP1"}'
```

---

**L'API est prête à être utilisée ! 🎉**

