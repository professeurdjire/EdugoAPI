# 📚 Documentation Complète des Endpoints pour le Frontend Élève

## 🔐 Authentification

Tous les endpoints (sauf `/api/auth/login`, `/api/auth/register`) nécessitent un **token JWT** dans le header :
```
Authorization: Bearer <token>
```

---

## 1️⃣ AUTHENTIFICATION (`/api/auth`)

### 1.1 Inscription d'un élève
- **URL:** `POST /api/auth/register`
- **Authentification:** Non requise
- **Body (JSON):**
```json
{
  "email": "string",
  "motDePasse": "string",
  "nom": "string",
  "prenom": "string",
  "ville": "string (optionnel)",
  "telephone": "integer (optionnel)",
  "classeId": "Long (optionnel)",
  "niveauId": "Long (optionnel)",
  "photoProfil": "string (optionnel)"
}
```
- **Réponse (200):** `LoginResponse`
```json
{
  "token": "string",
  "refreshToken": "string",
  "role": "ELEVE",
  "id": "Long",
  "email": "string",
  "nom": "string",
  "prenom": "string"
}
```

### 1.2 Connexion
- **URL:** `POST /api/auth/login`
- **Authentification:** Non requise
- **Body (JSON):**
```json
{
  "email": "string",
  "motDePasse": "string"
}
```
- **Réponse (200):** `LoginResponse` (voir ci-dessus)

### 1.3 Rafraîchir le token
- **URL:** `POST /api/auth/refresh`
- **Authentification:** Non requise (mais refreshToken requis)
- **Body (JSON):**
```json
{
  "refreshToken": "string"
}
```
- **Réponse (200):** `LoginResponse`

### 1.4 Récupérer l'élève connecté
- **URL:** `GET /api/auth/me`
- **Authentification:** Requise
- **Réponse (200):** `EleveProfileResponse`
```json
{
  "id": "Long",
  "nom": "string",
  "prenom": "string",
  "email": "string",
  "photoProfil": "string",
  "telephone": "integer",
  "ville": "string",
  "classeId": "Long",
  "classeNom": "string",
  "niveauId": "Long",
  "niveauNom": "string",
  "pointAccumule": "integer",
  "role": "ELEVE"
}
```

---

## 2️⃣ PROFIL ÉLÈVE (`/api/eleve`)

### 2.1 Récupérer le profil
- **URL:** `GET /api/eleve/profil/{id}`
- **Réponse (200):** `Eleve` (entité complète)

### 2.2 Mettre à jour le profil
- **URL:** `PUT /api/eleve/profil/{id}`
- **Body (JSON):** `Eleve` (champs modifiables uniquement)
- **Réponse (200):** `Eleve`

### 2.3 Changer le mot de passe
- **URL:** `POST /api/eleve/profil/{id}/change-password`
- **Body (JSON):**
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```
- **Réponse (200):**
```json
{
  "message": "Mot de passe modifié avec succès"
}
```

---

## 3️⃣ POINTS ET BADGES (`/api/eleve`)

### 3.1 Récupérer les points
- **URL:** `GET /api/eleve/points/{id}`
- **Réponse (200):**
```json
{
  "points": "integer"
}
```

### 3.2 Récupérer les badges
- **URL:** `GET /api/eleve/badges/{id}`
- **Réponse (200):** `List<Badge>`

### 3.3 Récupérer les statistiques
- **URL:** `GET /api/eleve/statistiques/{id}`
- **Réponse (200):** `Object` (statistiques personnalisées)

---

## 4️⃣ LIVRES (`/api/eleve` ou `/api/livres`)

### 4.1 Livres disponibles pour un élève
- **URL:** `GET /api/eleve/livres/disponibles/{id}` ou `GET /api/livres/disponibles/{eleveId}`
- **Réponse (200):** `List<LivreResponse>`
```json
[
  {
    "id": "Long",
    "titre": "string",
    "isbn": "string",
    "auteur": "string",
    "imageCouverture": "string (URL)",
    "totalPages": "integer",
    "matiereId": "Long",
    "matiereNom": "string",
    "niveauId": "Long",
    "niveauNom": "string",
    "classeId": "Long",
    "classeNom": "string",
    "langueId": "Long",
    "langueNom": "string",
    "quizId": "Long (peut être null)"
  }
]
```

### 4.2 Détails d'un livre
- **URL:** `GET /api/eleve/livres/{id}` ou `GET /api/livres/{id}`
- **Réponse (200):** `LivreDetailResponse`
```json
{
  "id": "Long",
  "titre": "string",
  "isbn": "string",
  "auteur": "string",
  "imageCouverture": "string",
  "totalPages": "integer",
  "lectureAuto": "boolean",
  "interactif": "boolean",
  "anneePublication": "integer",
  "editeur": "string",
  "description": "string",
  "niveauId": "Long",
  "niveauNom": "string",
  "classeId": "Long",
  "classeNom": "string",
  "matiereId": "Long",
  "matiereNom": "string",
  "langueId": "Long",
  "langueNom": "string",
  "quizId": "Long",
  "progression": "Double (optionnel)",
  "statistiques": "Object (optionnel)"
}
```

### 4.3 Livres par matière
- **URL:** `GET /api/livres/matiere/{matiereId}`
- **Réponse (200):** `List<LivreResponse>`

### 4.4 Livres par niveau
- **URL:** `GET /api/livres/niveau/{niveauId}`
- **Réponse (200):** `List<LivreResponse>`

### 4.5 Livres par classe
- **URL:** `GET /api/livres/classe/{classeId}`
- **Réponse (200):** `List<LivreResponse>`

### 4.6 Rechercher par titre
- **URL:** `GET /api/livres/recherche/titre?titre={titre}`
- **Réponse (200):** `List<LivreResponse>`

### 4.7 Rechercher par auteur
- **URL:** `GET /api/livres/recherche/auteur?auteur={auteur}`
- **Réponse (200):** `List<LivreResponse>`

### 4.8 Livres populaires
- **URL:** `GET /api/livres/populaires`
- **Réponse (200):** `List<LivrePopulaireResponse>`

### 4.9 Livres recommandés
- **URL:** `GET /api/livres/recommandes/{eleveId}`
- **Réponse (200):** `List<LivreResponse>`

### 4.10 Livres récents
- **URL:** `GET /api/livres/recents`
- **Réponse (200):** `List<LivreResponse>`

### 4.11 Statistiques d'un livre
- **URL:** `GET /api/livres/statistiques/{livreId}`
- **Réponse (200):** `StatistiquesLivreResponse`

---

## 5️⃣ PROGRESSION DE LECTURE (`/api/eleve` ou `/api/livres`)

### 5.1 Mettre à jour la progression
- **URL:** `POST /api/eleve/progression/{eleveId}/{livreId}` ou `POST /api/livres/progression/{eleveId}/{livreId}`
- **Body (JSON):**
```json
{
  "pageActuelle": "integer"
}
```
- **Réponse (200):** `ProgressionResponse`
```json
{
  "id": "Long",
  "eleveId": "Long",
  "eleveNom": "string",
  "livreId": "Long",
  "livreTitre": "string",
  "pageActuelle": "integer",
  "pourcentageCompletion": "integer",
  "dateMiseAJour": "LocalDateTime"
}
```

### 5.2 Récupérer toute la progression
- **URL:** `GET /api/eleve/progression/{id}` ou `GET /api/livres/progression/{eleveId}`
- **Réponse (200):** `List<ProgressionResponse>`

### 5.3 Récupérer la progression d'un livre spécifique
- **URL:** `GET /api/livres/progression/{eleveId}/{livreId}`
- **Réponse (200):** `ProgressionResponse` ou `null`

---

## 6️⃣ FICHIERS DE LIVRES (`/api/livres`)

### 6.1 Lister les fichiers d'un livre
- **URL:** `GET /api/livres/{livreId}/fichiers`
- **Réponse (200):** `List<FichierLivreDto>`
```json
[
  {
    "id": "Long",
    "nom": "string",
    "type": "PDF | EPUB | ...",
    "taille": "Long (bytes)",
    "format": "string",
    "chemin": "string (chemin serveur)"
  }
]
```

### 6.2 Télécharger un fichier
- **URL:** `GET /api/livres/fichiers/{fichierId}/download`
- **Réponse (200):** Fichier binaire (PDF, EPUB, etc.)
- **Headers:** `Content-Disposition: attachment; filename="..."`

---

## 7️⃣ QUIZZES (`/api/eleves` ou `/api/quizzes`)

### 7.1 Quizzes disponibles pour un élève
- **URL:** `GET /api/eleves/{id}/quizzes`
- **Réponse (200):** `List<QuizResponse>`
```json
[
  {
    "id": "Long",
    "titre": "string",
    "description": "string",
    "auteur": "string",
    "titreLivre": "string",
    "livreId": "Long",
    "matiereId": "Long",
    "matiereNom": "string",
    "niveauId": "Long",
    "niveauNom": "string",
    "nombreQuestions": "integer"
  }
]
```

### 7.2 Détails d'un quiz
- **URL:** `GET /api/quizzes/{id}`
- **Réponse (200):** `QuizResponse`

### 7.3 Soumettre les réponses d'un quiz
- **URL:** `POST /api/quizzes/{quizId}/submit`
- **Body (JSON):**
```json
{
  "eleveId": "Long",
  "reponses": [
    {
      "questionId": "Long",
      "reponse": "string | string[] | boolean"
    }
  ]
}
```
- **Réponse (200):** `SubmitResultResponse`
```json
{
  "ownerId": "Long",
  "ownerType": "QUIZ",
  "eleveId": "Long",
  "score": "integer",
  "totalPoints": "integer",
  "details": [
    {
      "questionId": "Long",
      "points": "integer",
      "correct": "boolean"
    }
  ]
}
```

---

## 8️⃣ EXERCICES (`/api/eleve` ou `/api/exercices`)

### 8.1 Exercices disponibles pour un élève
- **URL:** `GET /api/eleve/exercices/disponibles/{id}` ou `GET /api/exercices/disponibles/{eleveId}`
- **Réponse (200):** `List<ExerciceResponse>`
```json
[
  {
    "id": "Long",
    "titre": "string",
    "active": "boolean",
    "niveauDifficulte": "integer",
    "tempsAlloue": "integer",
    "matiereId": "Long",
    "matiereNom": "string"
  }
]
```

### 8.2 Détails d'un exercice
- **URL:** `GET /api/eleve/exercices/{id}` ou `GET /api/exercices/{id}`
- **Réponse (200):** `ExerciceDetailResponse`

### 8.3 Exercices par matière
- **URL:** `GET /api/exercices/matiere/{matiereId}`
- **Réponse (200):** `List<ExerciceResponse>`

### 8.4 Exercices par difficulté
- **URL:** `GET /api/exercices/difficulte/{niveauDifficulte}`
- **Réponse (200):** `List<ExerciceResponse>`

### 8.5 Exercices d'un livre
- **URL:** `GET /api/exercices/livre/{livreId}`
- **Réponse (200):** `List<ExerciceResponse>`

### 8.6 Soumettre un exercice (texte libre)
- **URL:** `POST /api/eleve/exercices/soumettre/{eleveId}/{exerciceId}` ou `POST /api/exercices/soumettre/{eleveId}/{exerciceId}`
- **Body (JSON):**
```json
{
  "reponse": "string"
}
```
- **Réponse (200):** `FaireExerciceResponse`

### 8.7 Soumettre un exercice (QCU/QCM/VRAI_FAUX)
- **URL:** `POST /api/exercices/{exerciceId}/submit`
- **Body (JSON):** (même format que pour quiz)
- **Réponse (200):** `SubmitResultResponse`

### 8.8 Historique des exercices
- **URL:** `GET /api/eleve/exercices/historique/{id}` ou `GET /api/exercices/historique/{eleveId}`
- **Réponse (200):** `List<FaireExerciceResponse>`

### 8.9 Exercice réalisé (détails)
- **URL:** `GET /api/exercices/realise/{eleveId}/{exerciceId}`
- **Réponse (200):** `FaireExerciceResponse`

---

## 9️⃣ CHALLENGES (`/api/eleve` ou `/api/challenges`)

### 9.1 Challenges disponibles pour un élève
- **URL:** `GET /api/eleve/challenges/disponibles/{id}` ou `GET /api/challenges/disponibles/{eleveId}`
- **Réponse (200):** `List<ChallengeResponse>`
```json
[
  {
    "id": "Long",
    "titre": "string",
    "description": "string",
    "points": "integer",
    "theme": "string",
    "dateDebut": "LocalDateTime",
    "dateFin": "LocalDateTime"
  }
]
```

### 9.2 Détails d'un challenge
- **URL:** `GET /api/eleve/challenges/{id}` ou `GET /api/challenges/{id}`
- **Réponse (200):** `Challenge` (entité) ou `ChallengeResponse`

### 9.3 Participer à un challenge
- **URL:** `POST /api/eleve/challenges/participer/{eleveId}/{challengeId}` ou `POST /api/challenges/participer/{eleveId}/{challengeId}`
- **Réponse (200):** `Participation`

### 9.4 Soumettre les réponses d'un challenge
- **URL:** `POST /api/challenges/{challengeId}/submit`
- **Body (JSON):** (même format que pour quiz)
- **Réponse (200):** `SubmitResultResponse`

### 9.5 Challenges participés
- **URL:** `GET /api/eleve/challenges/participes/{id}` ou `GET /api/challenges/participes/{eleveId}`
- **Réponse (200):** `List<Participation>`

### 9.6 Classement d'un challenge
- **URL:** `GET /api/challenges/{challengeId}/leaderboard`
- **Réponse (200):** `List<ChallengeLeaderboardEntryResponse>`

---

## 🔟 DÉFIS (`/api/eleve` ou `/api/defis`)

### 10.1 Défis disponibles pour un élève
- **URL:** `GET /api/eleve/defis/disponibles/{id}` ou `GET /api/defis/disponibles/{eleveId}`
- **Réponse (200):** `List<DefiResponse>`

### 10.2 Détails d'un défi
- **URL:** `GET /api/eleve/defis/{id}` ou `GET /api/defis/{id}`
- **Réponse (200):** `DefiDetailResponse`

### 10.3 Participer à un défi
- **URL:** `POST /api/eleve/defis/participer/{eleveId}/{defiId}` ou `POST /api/defis/participer/{eleveId}/{defiId}`
- **Réponse (200):** `EleveDefiResponse`
```json
{
  "id": "Long",
  "eleveId": "Long",
  "nom": "string",
  "prenom": "string",
  "defiId": "Long",
  "defiTitre": "string",
  "dateEnvoie": "LocalDateTime",
  "statut": "string"
}
```

### 10.4 Défis participés
- **URL:** `GET /api/eleve/defis/participes/{id}` ou `GET /api/defis/participes/{eleveId}`
- **Réponse (200):** `List<EleveDefiResponse>`

---

## 1️⃣1️⃣ OBJECTIFS (`/api/objectifs`)

### 11.1 Créer un objectif
- **URL:** `POST /api/objectifs/eleve/{eleveId}`
- **Body (JSON):** `ObjectifRequest`
- **Réponse (200):** `ObjectifResponse`

### 11.2 Objectif en cours
- **URL:** `GET /api/objectifs/eleve/{eleveId}/en-cours`
- **Réponse (200):** `ObjectifResponse` ou `null`

### 11.3 Tous les objectifs d'un élève
- **URL:** `GET /api/objectifs/eleve/{eleveId}/tous`
- **Réponse (200):** `List<ObjectifResponse>`

### 11.4 Détails d'un objectif
- **URL:** `GET /api/objectifs/{id}/eleve/{eleveId}`
- **Réponse (200):** `ObjectifResponse`

### 11.5 Historique des objectifs
- **URL:** `GET /api/objectifs/eleve/{eleveId}/historique`
- **Réponse (200):** `List<ObjectifResponse>`

### 11.6 Supprimer un objectif
- **URL:** `DELETE /api/objectifs/{id}/eleve/{eleveId}`
- **Réponse (204):** No Content

---

## 1️⃣2️⃣ SUGGESTIONS (`/api/suggestions`)

### 12.1 Ajouter une suggestion
- **URL:** `POST /api/suggestions`
- **Body (JSON):** `SuggestionRequest`
```json
{
  "titre": "string",
  "description": "string",
  "categorie": "string (optionnel)"
}
```
- **Réponse (200):** `SuggestionResponse`

### 12.2 Mes suggestions
- **URL:** `GET /api/suggestions/mes-suggestions`
- **Réponse (200):** `List<SuggestionResponse>`

### 12.3 Détails d'une de mes suggestions
- **URL:** `GET /api/suggestions/mes-suggestions/{id}`
- **Réponse (200):** `SuggestionResponse`

---

## 1️⃣3️⃣ CONVERSION DE POINTS (`/api/conversions`)

### 13.1 Options de conversion disponibles
- **URL:** `GET /api/conversions/options` ou `GET /api/conversions/options/actives`
- **Réponse (200):** `List<OptionsConversionResponse>`

### 13.2 Détails d'une option
- **URL:** `GET /api/conversions/options/{id}`
- **Réponse (200):** `OptionsConversionResponse`

### 13.3 Convertir des points en forfait data
- **URL:** `POST /api/conversions/convertir/{eleveId}`
- **Body (JSON):**
```json
{
  "optionId": "Long",
  "montant": "integer (points à convertir)"
}
```
- **Réponse (200):** `ConversionResponse`

### 13.4 Historique des conversions
- **URL:** `GET /api/conversions/historique/{eleveId}`
- **Réponse (200):** `List<ConversionResponse>`

---

## 1️⃣4️⃣ IA ÉDUCATIVE (`/api/ia`)

### 14.1 Envoyer un message au chat IA
- **URL:** `POST /api/ia/chat`
- **Body (JSON):** `ChatRequest`
```json
{
  "eleveId": "Long",
  "message": "string",
  "sessionId": "Long (optionnel, pour continuer une session)"
}
```
- **Réponse (200):** `ChatResponse`

### 14.2 Lister les sessions de chat
- **URL:** `GET /api/ia/chat/sessions?eleveId={eleveId}`
- **Réponse (200):** `List<SessionResponse>`

### 14.3 Récupérer une session
- **URL:** `GET /api/ia/chat/sessions/{id}`
- **Réponse (200):** `ChatResponse`

### 14.4 Supprimer une session
- **URL:** `DELETE /api/ia/chat/sessions/{id}`
- **Réponse (204):** No Content

### 14.5 Générer une ressource IA
- **URL:** `POST /api/ia/ressources`
- **Body (JSON):** `RessourceIARequest`
- **Réponse (200):** `RessourceIAResponse`

### 14.6 Lister les ressources IA
- **URL:** `GET /api/ia/ressources?eleveId={eleveId}&livreId={livreId}&type={type}`
- **Réponse (200):** `List<RessourceIAResponse>`

### 14.7 Détails d'une ressource IA
- **URL:** `GET /api/ia/ressources/{id}`
- **Réponse (200):** `RessourceIAResponse`

---

## 1️⃣5️⃣ CAMARADES DE CLASSE (`/api/eleve`)

### 15.1 Liste des camarades
- **URL:** `GET /api/eleve/camarades/{id}`
- **Réponse (200):** `List<Eleve>`

---

## 1️⃣6️⃣ DONNÉES PUBLIQUES (Accessibles sans authentification spécifique)

### 16.1 Niveaux
- **URL:** `GET /api/niveaux` ou `GET /niveaux`
- **Réponse (200):** `List<Niveau>`

### 16.2 Détails d'un niveau
- **URL:** `GET /api/niveaux/{id}` ou `GET /niveaux/{id}`
- **Réponse (200):** `Niveau`

### 16.3 Classes
- **URL:** `GET /api/classes` ou `GET /classes`
- **Réponse (200):** `List<Classe>`

### 16.4 Détails d'une classe
- **URL:** `GET /api/classes/{id}` ou `GET /classes/{id}`
- **Réponse (200):** `Classe`

### 16.5 Classes d'un niveau
- **URL:** `GET /api/classes/niveau/{niveauId}`
- **Réponse (200):** `List<Classe>`

---

## 📋 RÉSUMÉ DES URLS IMPORTANTES

### Format des URLs
- **Base URL:** `http://localhost:8080/api` ou `http://votre-ip:8080/api`
- ⚠️ **IMPORTANT:** Ne pas mettre `/api` deux fois dans la base URL du frontend !

### Endpoints principaux par catégorie:

#### Authentification
- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/auth/me`

#### Profil
- `GET /api/eleve/profil/{id}`
- `PUT /api/eleve/profil/{id}`

#### Livres
- `GET /api/eleve/livres/disponibles/{id}`
- `GET /api/livres/{id}`
- `GET /api/eleves/{id}/quizzes` ⬅️ **NOUVEAU - Format pluriel**

#### Progression
- `POST /api/eleve/progression/{eleveId}/{livreId}`
- `GET /api/eleve/progression/{id}`

#### Exercices
- `GET /api/eleve/exercices/disponibles/{id}`
- `POST /api/exercices/{exerciceId}/submit`

#### Challenges
- `GET /api/eleve/challenges/disponibles/{id}`
- `POST /api/challenges/{challengeId}/submit`

#### Défis
- `GET /api/eleve/defis/disponibles/{id}`
- `POST /api/eleve/defis/participer/{eleveId}/{defiId}`

---

## 🔑 Champs importants dans les réponses

### LivreResponse / LivreDetailResponse
- ✅ `langueId` - ID de la langue (maintenant correctement retourné)
- ✅ `quizId` - ID du quiz lié (maintenant correctement retourné)
- ✅ Tous les IDs de relations (`matiereId`, `niveauId`, `classeId`)

### QuizResponse
- ✅ `livreId` - ID du livre associé (maintenant correctement retourné)
- ✅ Tous les IDs de relations

---

## ⚠️ NOTES IMPORTANTES

1. **Double `/api/api`:** Vérifier que la base URL du frontend ne contient pas déjà `/api`
2. **Token JWT:** Inclure dans le header `Authorization: Bearer <token>` pour tous les endpoints protégés
3. **IDs dans les réponses:** Tous les IDs (`langueId`, `quizId`, `livreId`, etc.) sont maintenant correctement renvoyés
4. **Format dates:** Les dates sont au format ISO 8601 (`LocalDateTime`)
5. **Images/Fichiers:** Les chemins d'images sont des URLs complètes ou des chemins relatifs à la base URL

---

## 📝 Codes de statut HTTP

- **200:** Succès
- **201:** Créé (pour POST)
- **204:** No Content (pour DELETE)
- **400:** Requête invalide
- **401:** Non authentifié
- **403:** Accès refusé
- **404:** Ressource non trouvée
- **500:** Erreur serveur

---

## 🔄 Flux typiques du frontend élève

### 1. Authentification
```
POST /api/auth/login → Obtenir token
GET /api/auth/me → Récupérer profil complet avec classeId, niveauId
```

### 2. Dashboard/Accueil
```
GET /api/eleve/profil/{id} → Profil
GET /api/eleve/points/{id} → Points
GET /api/eleve/badges/{id} → Badges
GET /api/objectifs/eleve/{id}/en-cours → Objectif en cours
GET /api/eleve/livres/disponibles/{id} → Livres disponibles
GET /api/eleves/{id}/quizzes → Quizzes disponibles
GET /api/eleve/exercices/disponibles/{id} → Exercices disponibles
GET /api/eleve/challenges/disponibles/{id} → Challenges disponibles
GET /api/eleve/defis/disponibles/{id} → Défis disponibles
```

### 3. Lecture d'un livre
```
GET /api/livres/{livreId} → Détails du livre
GET /api/livres/{livreId}/fichiers → Fichiers disponibles
GET /api/livres/fichiers/{fichierId}/download → Télécharger le fichier
POST /api/eleve/progression/{eleveId}/{livreId} → Mettre à jour progression
GET /api/livres/progression/{eleveId}/{livreId} → Vérifier progression
```

### 4. Réaliser un quiz
```
GET /api/quizzes/{quizId} → Détails du quiz
POST /api/quizzes/{quizId}/submit → Soumettre les réponses
```

### 5. Réaliser un exercice
```
GET /api/exercices/{exerciceId} → Détails de l'exercice
POST /api/exercices/{exerciceId}/submit → Soumettre (QCU/QCM)
POST /api/exercices/soumettre/{eleveId}/{exerciceId} → Soumettre (texte libre)
```

---

Cette documentation couvre tous les endpoints nécessaires pour le frontend élève ! 🎉

