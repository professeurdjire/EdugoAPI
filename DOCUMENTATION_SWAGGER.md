# Documentation Swagger/OpenAPI - API EDUGO

## 🌐 Accès à la Documentation

Une fois l'application démarrée, la documentation Swagger est accessible aux URLs suivantes :

- **Interface Swagger UI** : http://localhost:8080/swagger-ui.html
- **Documentation JSON** : http://localhost:8080/v3/api-docs
- **Documentation YAML** : http://localhost:8080/v3/api-docs.yaml

## 📋 Sections de l'API Documentée

### 1. **Authentification** 
Tous les endpoints pour gérer l'authentification des utilisateurs.

#### Endpoints :
- `POST /api/auth/register` - Inscription d'un nouvel élève
- `POST /api/auth/login` - Connexion (Admin ou Élève)
- `POST /api/auth/refresh` - Rafraîchir le token JWT
- `POST /api/auth/logout` - Déconnexion

**Note :** Ces endpoints sont **publics** (pas d'authentification requise).

### 2. **Administration** 
Endpoints réservés aux administrateurs pour la gestion complète de la plateforme.

#### Structures Scolaires :
- **Niveaux** : `GET/POST/PUT/DELETE /api/admin/niveaux`
- **Classes** : `GET/POST/PUT/DELETE /api/admin/classes`
- **Matières** : `GET/POST/PUT/DELETE /api/admin/matieres`

#### Ressources Éducatives :
- **Livres** : `GET/POST/PUT/DELETE /api/admin/livres`
- **Exercices** : `GET/POST/PUT/DELETE /api/admin/exercices`
- **Quiz** : `GET/POST/PUT/DELETE /api/admin/quiz`

#### Gamification :
- **Défis** : `GET/POST/PUT/DELETE /api/admin/defis`
- **Challenges** : `GET/POST/PUT/DELETE /api/admin/challenges`
- **Badges** : `GET/POST/PUT/DELETE /api/admin/badges`

#### Utilisateurs :
- **Utilisateurs** : `GET/POST/PUT/DELETE /api/admin/users`

**Note :** Tous ces endpoints nécessitent un **token JWT valide** avec le rôle **ADMIN**.

### 3. **Fonctionnalités Élèves**
Endpoints pour les fonctionnalités élèves.

#### Consultation :
- `GET /api/eleve/livres` - Liste tous les livres
- `GET /api/eleve/exercices` - Liste tous les exercices
- `GET /api/eleve/challenges` - Liste tous les challenges
- `GET /api/eleve/defis` - Liste tous les défis

#### Progression :
- `GET /api/eleve/progressions/eleve/{eleveId}` - Progressions de l'élève
- `POST /api/eleve/progressions` - Créer une progression
- `PUT /api/eleve/progressions/{id}` - Mettre à jour une progression

#### Notifications :
- `GET /api/eleve/notifications/eleve/{eleveId}` - Toutes les notifications
- `GET /api/eleve/notifications/eleve/{eleveId}/non-lues` - Notifications non lues
- `PUT /api/eleve/notifications/{id}/marquer-vu` - Marquer comme vu

#### Autres :
- `POST /api/eleve/suggestions` - Créer une suggestion
- `POST /api/eleve/objectifs` - Créer un objectif
- `POST /api/eleve/participations` - Créer une participation à un challenge

**Note :** Tous ces endpoints nécessitent un **token JWT valide** avec le rôle **ELEVE** ou **ADMIN**.

## 🔐 Authentification dans Swagger

### Pour utiliser l'authentification dans Swagger UI :

1. **Accéder à la page Swagger** : http://localhost:8080/swagger-ui.html

2. **Se connecter** via l'endpoint `/api/auth/login` :
   ```json
   {
     "email": "admin@edugo.com",
     "motDePasse": "admin123"
   }
   ```

3. **Copier le token** retourné dans la réponse

4. **Cliquer sur le bouton "Authorize"** en haut à droite de l'interface Swagger

5. **Coller le token** dans le champ "Value" (format : `Bearer VOTRE_TOKEN`)

6. **Cliquer sur "Authorize"** puis "Close"

7. **Vous pouvez maintenant tester** tous les endpoints protégés

## 📝 Exemples d'Utilisation

### Exemple 1 : Créer un niveau

**Endpoint :** `POST /api/admin/niveaux`

**Requête :**
```json
{
  "nom": "CP1"
}
```

**Réponse (200 OK) :**
```json
{
  "id": 1,
  "nom": "CP1",
  "classes": [],
  "livres": []
}
```

### Exemple 2 : Créer une classe

**Endpoint :** `POST /api/admin/classes`

**Requête :**
```json
{
  "nom": "6ème A",
  "niveau": {
    "id": 1,
    "nom": "6ème"
  }
}
```

### Exemple 3 : Créer un livre

**Endpoint :** `POST /api/admin/livres`

**Requête :**
```json
{
  "titre": "Manuel de Mathématiques",
  "auteur": "Jean Dupont",
  "editeur": "Éditions Maliennes",
  "description": "Manuel complet de mathématiques pour la 6ème",
  "totalPages": 250,
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

## 🎯 Fonctionnalités de Swagger UI

### 1. **Tester les Endpoints Directement**
- Cliquez sur un endpoint pour voir les détails
- Utilisez le bouton "Try it out" pour tester l'endpoint
- Remplissez les paramètres et cliquez sur "Execute"

### 2. **Voir les Modèles de Données**
- Tous les modèles de données sont documentés
- Exemples JSON inclus pour chaque modèle
- Schémas de validation affichés

### 3. **Filtres de Recherche**
- Recherchez par tag (Authentification, Administration, Élèves)
- Recherchez par nom d'endpoint
- Filtrez par méthode HTTP (GET, POST, PUT, DELETE)

### 4. **Authentification Pratique**
- Bouton "Authorize" pour entrer votre token
- Token validé automatiquement
- Support du Bearer Token JWT

## 🔄 Codes de Réponse HTTP

Tous les endpoints documentent les codes de réponse possibles :

- **200 OK** : Succès de l'opération
- **201 Created** : Ressource créée avec succès
- **204 No Content** : Succès sans contenu retourné
- **400 Bad Request** : Requête invalide (erreur de validation)
- **401 Unauthorized** : Non authentifié ou token invalide
- **403 Forbidden** : Non autorisé (rôle insuffisant)
- **404 Not Found** : Ressource non trouvée
- **409 Conflict** : Ressource déjà existante
- **500 Internal Server Error** : Erreur serveur

## 📊 Statistiques et Monitoring

Swagger affiche également :
- Nombre total d'endpoints
- Temps de réponse moyen
- Versions de l'API
- Informations de contact

## 🛠️ Configuration

La configuration Swagger est définie dans `SwaggerConfig.java` :

- **Titre** : "API EDUGO - Plateforme Éducative Malienne"
- **Version** : 1.0.0
- **Contact** : support@edugo.ml
- **License** : Apache 2.0
- **Sécurité** : Bearer JWT

## 🚀 Démarrage Rapide

1. **Démarrer l'application** :
   ```bash
   mvn spring-boot:run
   ```

2. **Accéder à Swagger UI** :
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Se connecter** avec un compte admin :
   ```
   email: admin@edugo.com
   mot de passe: admin123
   ```

4. **Tester les endpoints** directement depuis l'interface !

## 📞 Support

Pour toute question sur l'API :
- **Email** : support@edugo.ml
- **Website** : https://www.edugo.ml
- **Documentation** : http://localhost:8080/swagger-ui.html

---

**La documentation interactive est maintenant disponible ! 🎉**

