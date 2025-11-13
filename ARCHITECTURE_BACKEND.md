# Architecture Backend - EDUGO

## 1. Technologies

### Framework et Langage
- **Framework** : Spring Boot 3.5.7
- **Langage** : Java 21
- **Build Tool** : Apache Maven

### Base de Données
- **SGBD** : MySQL 8.0
- **ORM** : JPA/Hibernate
- **Pool de Connexion** : HikariCP

### Sécurité
- **Authentification** : JWT (JSON Web Token)
- **Autorisation** : Spring Security (RBAC)
- **Cryptographie** : BCrypt, JJWT

### Documentation API
- **Swagger/OpenAPI** : SpringDoc OpenAPI

### Tests
- **Framework** : JUnit 5, Mockito
- **Coverage** : Jacoco

### Monitoring
- **Actuators** : Spring Boot Actuator

## 2. Structure du Projet

```
src/main/java/com/example/edugo/
├── config/               # Configuration de sécurité, Swagger, initialisation
├── controller/          # Contrôleurs REST par module
├── dto/                 # Objets de transfert de données
├── entity/              # Entités JPA
├── exception/           # Gestion d'exceptions personnalisées
├── repository/          # Interfaces de persistance Spring Data JPA
├── security/            # Filtres JWT, utilitaires d'authentification
└── service/             # Logique métier

src/main/resources/
├── application.properties        # Configuration principale
├── application-dev.properties   # Configuration développement
└── static/                      # Ressources statiques
```

## 3. Architecture Logique

### Modèle MVC
- **Modèle** : Entités JPA + DTOs
- **Vue** : API REST (JSON)
- **Contrôleur** : @RestController

### Couches d'Architecture
1. **Présentation** : REST Controllers
2. **Application** : Services
3. **Domaine** : Entités, Logique métier
4. **Persistance** : Repositories, JPA

## 4. Modules Fonctionnels

### Utilisateurs
- Gestion Admins, Élèves
- Authentification JWT
- Rôles et Permissions

### Ressources Pédagogiques
- Livres numériques
- Exercices interactifs
- Quiz et Évaluations

### Gamification
- Défis et Challenges
- Badges et Récompenses
- Classements

### Analytics
- Statistiques d'utilisation
- Suivi de progression
- Rapports

## 5. Configuration Serveur

### Accès API
- **Port** : Dynamique (par défaut 8080)
- **Context Path** : `/api`
- **URL complète** : `http://localhost:<port>/api/...`

### Points d'Entrée Principaux
- **Authentification** : `/api/auth/**`
- **Administration** : `/api/admin/**`
- **Élève** : `/api/eleve/**`
- **Public** : `/api/public/**`

### Documentation
- **Swagger UI** : `/api/swagger-ui.html`
- **OpenAPI Docs** : `/api/v3/api-docs`

## 6. Sécurité

### Schéma d'Authentification
1. **Inscription** : `/api/auth/register`
2. **Connexion** : `/api/auth/login` → JWT + Refresh Token
3. **Accès Protégé** : Header `Authorization: Bearer <token>`

### Rôles
- **ADMIN** : Gestion complète
- **ELEVE** : Accès aux ressources pédagogiques

## 7. Gestion des Fichiers

### Types Supportés
- **Livres** : PDF, EPUB
- **Images** : JPG, PNG
- **Audio/Video** : MP3, MP4

### Stockage
- **Répertoire** : `./uploads/`
- **Organisation** : Sous-dossiers par type

## 8. Performance

### Configuration
- **Batch Processing** : JDBC batch
- **Caching** : Spring Cache (Simple)
- **Connection Pool** : HikariCP

### Optimisations
- **Lazy Loading** : Fetch strategies
- **Pagination** : Pageable queries
- **Indexing** : Database indexes

---
# 📚 Architecture du Backend EDUGO - Guide Complet

## 🎯 Vue d'Ensemble

Le backend EDUGO est une **API REST Spring Boot** qui gère une plateforme éducative avec système de gamification. Il utilise **JWT** pour l'authentification et **MySQL** comme base de données.

---

## 🏗️ Structure du Projet

```
src/main/java/com/example/edugo/
├── config/              # Configurations (JWT Filter, Swagger, etc.)
├── controller/          # Controllers REST (endpoints API)
├── dto/                 # Data Transfer Objects (objets de transfert)
├── entity/              # Entités JPA (modèles de base de données)
│   └── Principales/     # Entités principales du domaine
├── exception/           # Gestion des erreurs
├── repository/          # Repositories JPA (accès base de données)
├── security/            # Configuration sécurité (JWT, Spring Security)
└── service/             # Services métier (logique business)
```

---

## 🔄 Flux de Données

```
Frontend (Angular)
    ↓ HTTP Request (avec JWT token)
Controller (REST endpoints)
    ↓ Appelle
Service (logique métier)
    ↓ Utilise
Repository (accès base de données)
    ↓ Retourne
Entity (objet JPA)
    ↓ Convertit en
DTO (objet de transfert)
    ↓ Retourne au
Frontend (JSON)
```

---

## 📦 Les 3 Couches Principales

### 1. **Controller** (Couche Présentation)
- **Rôle** : Reçoit les requêtes HTTP, valide les données, appelle les services
- **Exemple** : `AdminController`, `AuthController`, `LivreController`
- **Responsabilités** :
  - Définir les endpoints (`@GetMapping`, `@PostMapping`, etc.)
  - Valider les permissions (`@PreAuthorize`)
  - Retourner les réponses HTTP

### 2. **Service** (Couche Métier)
- **Rôle** : Contient la logique métier, transforme les entités en DTOs
- **Exemple** : `AdminService`, `AuthService`, `ServiceLivre`
- **Responsabilités** :
  - Logique métier (création, modification, suppression)
  - Conversion Entity ↔ DTO
  - Validation des règles métier

### 3. **Repository** (Couche Données)
- **Rôle** : Accès à la base de données via JPA
- **Exemple** : `LivreRepository`, `UserRepository`, `QuizRepository`
- **Responsabilités** :
  - Requêtes SQL automatiques (via JPA)
  - Requêtes personnalisées (`@Query`)
  - Opérations CRUD

---

## 🔐 Système d'Authentification

### Comment ça marche ?

1. **Inscription/Login** → `AuthController` → `AuthService`
2. **Génération JWT** → `JwtUtil` crée un token avec le rôle de l'utilisateur
3. **Stockage** → Le frontend stocke le token
4. **Requêtes suivantes** → Le frontend envoie `Authorization: Bearer <token>`
5. **Validation** → `JwtAuthenticationFilter` valide le token à chaque requête
6. **Autorisation** → `SecurityConfig` vérifie les rôles (ADMIN, ELEVE)

### Rôles
- **ADMIN** : Accès complet à tous les endpoints
- **ELEVE** : Accès limité aux fonctionnalités élèves

---

## 📊 Entités Principales

### Utilisateurs
- `User` (abstraite) : Classe de base
  - `Admin` : Administrateur
  - `Eleve` : Élève avec points accumulés

### Contenu Éducatif
- `Livre` : Livres avec quiz associé
- `Exercice` : Exercices avec questions
- `Quiz` : Quiz liés aux livres
- `Question` : Questions pour quiz/exercices/challenges

### Gamification
- `Defi` : Défis pour les élèves
- `Challenge` : Challenges avec dates
- `Badge` : Badges/récompenses
- `Progression` : Progression de lecture

### Structures
- `Niveau` : Niveaux scolaires (CP1, CP2, etc.)
- `Classe` : Classes (6ème A, 5ème B, etc.)
- `Matiere` : Matières (Maths, Français, etc.)

### Conversions
- `OptionsConversion` : Options de conversion (forfaits data)
- `ConversionEleve` : Historique des conversions d'élèves

---

## 🛣️ Endpoints par Catégorie

### 🔓 Authentification (Public)
- `POST /api/auth/login` - Connexion
- `POST /api/auth/register` - Inscription
- `POST /api/auth/refresh` - Rafraîchir token

### 👨‍💼 Administration (ADMIN uniquement)
- `/api/admin/users` - Gestion utilisateurs
- `/api/admin/niveaux` - Gestion niveaux
- `/api/admin/classes` - Gestion classes
- `/api/admin/matieres` - Gestion matières
- `/api/admin/livres` - Gestion livres
- `/api/admin/exercices` - Gestion exercices
- `/api/admin/defis` - Gestion défis
- `/api/admin/challenges` - Gestion challenges
- `/api/admin/badges` - Gestion badges
- `/api/admin/quizzes` - Gestion quizzes

### 📚 Données Générales (Authentifié)
- `/api/classes` - Liste classes (GET)
- `/api/matieres` - Liste matières (GET)
- `/api/niveaux` - Liste niveaux (GET)
- `/api/users` - Liste utilisateurs (GET)
- `/api/livres` - Liste livres (GET)
- `/api/exercices` - Liste exercices (GET)
- `/api/defis` - Liste défis (GET)
- `/api/challenges` - Liste challenges (GET)
- `/api/badges` - Liste badges (GET)
- `/api/quizzes` - Liste quizzes (GET)

### 🎓 Fonctionnalités Élèves (ELEVE + ADMIN)
- `/api/eleve/**` - Toutes les fonctionnalités élèves

---

## 🔄 Problème des Boucles Infinies

### Pourquoi ça arrive ?
Quand une entité `Livre` contient une relation vers `Quiz`, et que `Quiz` contient une relation vers `Livre`, JSON essaie de sérialiser les deux → **boucle infinie**.

### Solution
Utiliser des **DTOs** (Data Transfer Objects) au lieu de retourner directement les entités :
- `LivreResponse` au lieu de `Livre`
- `QuizResponse` au lieu de `Quiz`
- `ClasseResponse` au lieu de `Classe`

Les DTOs ne contiennent que les données nécessaires, sans relations circulaires.

---

## 📧 Système d'Email

### Configuration
- Utilise `spring-boot-starter-mail`
- Configuration dans `application.properties`

### Utilisation
- Envoi d'email lors de l'inscription
- Notifications aux élèves
- Confirmations de conversion

---

## 💰 Système de Conversion de Points

### Fonctionnement
1. Les élèves gagnent des **points** en faisant des exercices, défis, etc.
2. Les points peuvent être convertis en **forfaits data internet**
3. `OptionsConversion` : Définit les forfaits disponibles (ex: 100 points = 500 Mo)
4. `ConversionEleve` : Enregistre chaque conversion

### Endpoints
- `GET /api/conversions/options` - Liste des options de conversion
- `POST /api/conversions/convertir` - Convertir des points en forfait
- `GET /api/conversions/historique/{eleveId}` - Historique des conversions

---

## 🎯 Bonnes Pratiques

1. **Toujours utiliser des DTOs** pour les réponses API
2. **Ne jamais retourner des entités directement** (risque de boucles infinies)
3. **Valider les permissions** avec `@PreAuthorize`
4. **Gérer les erreurs** avec `GlobalExceptionHandler`
5. **Utiliser des transactions** (`@Transactional`) pour les opérations complexes

---

## 🔧 Configuration Importante

### Port et Context Path
- **Port** : Dynamique (par défaut 8080)
- **Context Path** : `/api`
- **URL complète** : `http://localhost:<port>/api/...`

### Base de Données
- **MySQL** : `edugodatabase`
- **Hibernate** : Auto-update du schéma

### JWT
- **Secret** : Configuré dans `application.properties`
- **Expiration** : 24 heures
- **Refresh Token** : 7 jours

---

## 📝 Prochaines Étapes

1. ✅ Corriger les boucles infinies dans AdminController
2. ✅ Compléter le service Quiz
3. ✅ Créer le service de conversion de points
4. ✅ Ajouter l'envoi d'email lors de l'inscription
5. ✅ Simplifier les endpoints admin

