# 🔔 Améliorations du Système de Notifications

## Problème identifié

L'erreur `No static resource api/notifications` indiquait qu'il manquait un contrôleur pour gérer les endpoints de notifications. Le frontend essayait d'accéder à `/api/notifications?eleveId=7&unreadOnly=true` mais cet endpoint n'existait pas.

## Solution implémentée

### 1. Création du Service `NotificationService`

**Fichier** : `src/main/java/com/example/edugo/service/NotificationService.java`

**Fonctionnalités** :
- ✅ Récupération des notifications d'un élève (toutes ou non lues uniquement)
- ✅ Comptage des notifications non lues
- ✅ Marquage d'une notification comme lue
- ✅ Marquage de toutes les notifications comme lues

**Méthodes principales** :
- `getNotificationsByEleveId(Long eleveId, Boolean unreadOnly)` - Récupère les notifications avec filtre optionnel
- `getUnreadCount(Long eleveId)` - Retourne le nombre de notifications non lues
- `markAsRead(Long notificationId)` - Marque une notification comme lue
- `markAllAsRead(Long eleveId)` - Marque toutes les notifications d'un élève comme lues

### 2. Création du Contrôleur `NotificationController`

**Fichier** : `src/main/java/com/example/edugo/controller/NotificationController.java`

**Endpoints créés** :

#### GET `/api/notifications`
- **Description** : Récupère les notifications d'un élève
- **Paramètres** :
  - `eleveId` (requis) : ID de l'élève
  - `unreadOnly` (optionnel) : Si `true`, retourne uniquement les notifications non lues
- **Autorisation** : ELEVE ou ADMIN
- **Exemple** : `GET /api/notifications?eleveId=7&unreadOnly=true`

#### GET `/api/notifications/unread-count`
- **Description** : Retourne le nombre de notifications non lues
- **Paramètres** :
  - `eleveId` (requis) : ID de l'élève
- **Autorisation** : ELEVE ou ADMIN
- **Exemple** : `GET /api/notifications/unread-count?eleveId=7`
- **Réponse** : `{"count": 5}`

#### PUT `/api/notifications/{id}/marquer-vu`
- **Description** : Marque une notification spécifique comme lue
- **Paramètres** :
  - `id` (path) : ID de la notification
- **Autorisation** : ELEVE ou ADMIN
- **Exemple** : `PUT /api/notifications/123/marquer-vu`

#### PUT `/api/notifications/marquer-tout-vu`
- **Description** : Marque toutes les notifications non lues d'un élève comme lues
- **Paramètres** :
  - `eleveId` (requis) : ID de l'élève
- **Autorisation** : ELEVE ou ADMIN
- **Exemple** : `PUT /api/notifications/marquer-tout-vu?eleveId=7`
- **Réponse** : `{"message": "Toutes les notifications ont été marquées comme lues"}`

### 3. Configuration de sécurité

**Fichier** : `src/main/java/com/example/edugo/security/SecurityConfig.java`

**Règles ajoutées** :
```java
.requestMatchers(HttpMethod.GET, "/notifications/**", "/api/notifications/**").authenticated()
.requestMatchers(HttpMethod.PUT, "/notifications/**", "/api/notifications/**").hasAnyRole("ELEVE", "ADMIN")
```

Les endpoints de notifications nécessitent :
- **GET** : Authentification (tous les utilisateurs authentifiés)
- **PUT** : Rôle ELEVE ou ADMIN

## Améliorations apportées

### ✅ Compatibilité avec le frontend
- Le contrôleur répond maintenant aux requêtes du frontend
- Support du paramètre `unreadOnly` pour filtrer les notifications non lues
- Support du paramètre `eleveId` pour récupérer les notifications d'un élève spécifique

### ✅ Fonctionnalités complètes
- Récupération des notifications (toutes ou non lues)
- Comptage des notifications non lues
- Marquage individuel ou en masse comme lues

### ✅ Sécurité
- Tous les endpoints nécessitent une authentification
- Les opérations de modification nécessitent le rôle ELEVE ou ADMIN
- Validation des paramètres

### ✅ Documentation
- Annotations Swagger complètes
- Descriptions détaillées des endpoints
- Exemples d'utilisation

## Utilisation

### Récupérer toutes les notifications d'un élève
```http
GET /api/notifications?eleveId=7
Authorization: Bearer {token}
```

### Récupérer uniquement les notifications non lues
```http
GET /api/notifications?eleveId=7&unreadOnly=true
Authorization: Bearer {token}
```

### Récupérer le nombre de notifications non lues
```http
GET /api/notifications/unread-count?eleveId=7
Authorization: Bearer {token}
```

### Marquer une notification comme lue
```http
PUT /api/notifications/123/marquer-vu
Authorization: Bearer {token}
```

### Marquer toutes les notifications comme lues
```http
PUT /api/notifications/marquer-tout-vu?eleveId=7
Authorization: Bearer {token}
```

## Structure des données

### NotificationResponse
```json
{
  "id": 1,
  "titre": "Nouveau badge obtenu",
  "message": "Félicitations ! Vous avez obtenu le badge 'Lecteur assidu'",
  "dateEnvoi": "2024-11-23T20:00:00",
  "utilisateurId": 7,
  "lu": false
}
```

## Notes importantes

1. **Ordre des règles de sécurité** : Les règles spécifiques pour les notifications sont placées avant les règles générales pour garantir qu'elles sont évaluées en premier.

2. **Transaction** : Les opérations de modification (marquer comme lu) sont transactionnelles pour garantir la cohérence des données.

3. **Performance** : Les requêtes utilisent des méthodes optimisées du repository avec des requêtes SQL personnalisées.

---

**Date de correction** : Novembre 2024
**Fichiers créés** :
- `src/main/java/com/example/edugo/service/NotificationService.java`
- `src/main/java/com/example/edugo/controller/NotificationController.java`

**Fichiers modifiés** :
- `src/main/java/com/example/edugo/security/SecurityConfig.java`

