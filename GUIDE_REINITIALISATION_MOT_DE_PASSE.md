# 🔐 Guide : Réinitialisation de Mot de Passe

## 📋 Vue d'Ensemble

Le système de réinitialisation de mot de passe permet aux **élèves** et aux **administrateurs** de réinitialiser leur mot de passe en utilisant leur adresse email. Le processus est sécurisé et utilise des tokens temporaires.

---

## 🔄 Flux de Réinitialisation

### 1. **Demande de Réinitialisation**
L'utilisateur demande la réinitialisation en fournissant son email.

### 2. **Envoi d'Email**
Un email contenant un lien de réinitialisation est envoyé à l'utilisateur.

### 3. **Vérification du Token** (Optionnel)
Le frontend peut vérifier si le token est valide avant d'afficher le formulaire de réinitialisation.

### 4. **Réinitialisation**
L'utilisateur saisit son nouveau mot de passe et le confirme.

### 5. **Confirmation**
Un email de confirmation est envoyé à l'utilisateur.

---

## 📡 Endpoints API

### 1. **Demander la Réinitialisation**

**Endpoint** : `POST /api/auth/forgot-password`

**Description** : Envoie un email avec un lien de réinitialisation à l'utilisateur.

**Accès** : Public (pas d'authentification requise)

**Body** :
```json
{
  "email": "user@example.com"
}
```

**Réponse (200 OK)** :
```json
{
  "message": "Si un compte existe avec cet email, un lien de réinitialisation a été envoyé",
  "success": true,
  "email": "user@example.com"
}
```

**Note de Sécurité** : Le message de réponse est générique pour ne pas révéler si l'email existe ou non dans la base de données.

---

### 2. **Vérifier un Token** (Optionnel)

**Endpoint** : `POST /api/auth/reset-password/verify`

**Description** : Vérifie si un token de réinitialisation est valide.

**Accès** : Public (pas d'authentification requise)

**Body** :
```json
{
  "token": "abc123def456..."
}
```

**Réponse (200 OK)** :
```json
{
  "message": "Token valide",
  "success": true,
  "email": "user@example.com"
}
```

**Réponse (404 Not Found)** :
```json
{
  "message": "Token invalide ou expiré",
  "status": "NOT_FOUND"
}
```

---

### 3. **Réinitialiser le Mot de Passe**

**Endpoint** : `POST /api/auth/reset-password`

**Description** : Réinitialise le mot de passe de l'utilisateur avec un token valide.

**Accès** : Public (pas d'authentification requise)

**Body** :
```json
{
  "token": "abc123def456...",
  "nouveauMotDePasse": "nouveauMotDePasse123",
  "confirmationMotDePasse": "nouveauMotDePasse123"
}
```

**Réponse (200 OK)** :
```json
{
  "message": "Mot de passe réinitialisé avec succès",
  "success": true,
  "email": "user@example.com"
}
```

**Erreurs possibles** :
- **400 Bad Request** : Les mots de passe ne correspondent pas
- **404 Not Found** : Token invalide ou expiré

---

## 🔧 Configuration

### Variables d'Environnement

Les paramètres suivants peuvent être configurés dans `application.properties` :

```properties
# Durée de validité du token (en heures)
app.password-reset.token-expiration-hours=24

# URL de base du frontend (pour les liens dans les emails)
app.password-reset.base-url=http://localhost:8080
```

### Personnalisation de l'URL de Réinitialisation

Dans `EmailService.java`, l'URL de réinitialisation est générée comme suit :

```java
String resetUrl = "https://www.edugo.ml/reset-password?token=" + token;
```

**À adapter** selon votre frontend :
- Pour Angular : `https://www.edugo.ml/reset-password?token=...`
- Pour Flutter : L'URL peut être ouverte dans le navigateur ou gérée par l'app

---

## 📧 Emails Envoyés

### 1. Email de Réinitialisation

**Sujet** : "Réinitialisation de votre mot de passe - EDUGO 🔐"

**Contenu** :
- Message de bienvenue personnalisé
- Bouton/lien de réinitialisation
- Avertissements de sécurité
- Durée de validité du token (24 heures)

### 2. Email de Confirmation

**Sujet** : "Mot de passe modifié avec succès - EDUGO ✅"

**Contenu** :
- Confirmation de la modification
- Avertissements de sécurité
- Lien de connexion

---

## 🔒 Sécurité

### Mesures de Sécurité Implémentées

1. **Tokens Sécurisés** : Génération de tokens aléatoires avec `SecureRandom` et encodage Base64
2. **Expiration** : Tokens valides pendant 24 heures (configurable)
3. **Usage Unique** : Chaque token ne peut être utilisé qu'une seule fois
4. **Invalidation** : Tous les anciens tokens sont invalidés lors de la création d'un nouveau
5. **Protection contre l'énumération** : Message générique même si l'email n'existe pas
6. **Chiffrement des Mots de Passe** : Utilisation de BCrypt pour le hachage

### Bonnes Pratiques

- ✅ Ne jamais exposer les tokens dans les logs
- ✅ Utiliser HTTPS en production
- ✅ Limiter le nombre de tentatives de réinitialisation par IP
- ✅ Nettoyer régulièrement les tokens expirés (job planifié)

---

## 🧪 Exemples d'Utilisation

### Exemple 1 : Demande de Réinitialisation (cURL)

```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "eleve@example.com"
  }'
```

### Exemple 2 : Vérification du Token (cURL)

```bash
curl -X POST http://localhost:8080/api/auth/reset-password/verify \
  -H "Content-Type: application/json" \
  -d '{
    "token": "abc123def456..."
  }'
```

### Exemple 3 : Réinitialisation (cURL)

```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "token": "abc123def456...",
    "nouveauMotDePasse": "nouveauMotDePasse123",
    "confirmationMotDePasse": "nouveauMotDePasse123"
  }'
```

### Exemple 4 : Flutter/Dart

```dart
// 1. Demander la réinitialisation
Future<void> requestPasswordReset(String email) async {
  final response = await dio.post(
    '/api/auth/forgot-password',
    data: {'email': email},
  );
  print(response.data['message']);
}

// 2. Vérifier le token
Future<bool> verifyToken(String token) async {
  try {
    final response = await dio.post(
      '/api/auth/reset-password/verify',
      data: {'token': token},
    );
    return response.data['success'] == true;
  } catch (e) {
    return false;
  }
}

// 3. Réinitialiser le mot de passe
Future<void> resetPassword(String token, String newPassword, String confirmPassword) async {
  final response = await dio.post(
    '/api/auth/reset-password',
    data: {
      'token': token,
      'nouveauMotDePasse': newPassword,
      'confirmationMotDePasse': confirmPassword,
    },
  );
  print(response.data['message']);
}
```

### Exemple 5 : Angular/TypeScript

```typescript
// 1. Demander la réinitialisation
requestPasswordReset(email: string): Observable<PasswordResetResponse> {
  return this.http.post<PasswordResetResponse>(
    '/api/auth/forgot-password',
    { email }
  );
}

// 2. Vérifier le token
verifyToken(token: string): Observable<PasswordResetResponse> {
  return this.http.post<PasswordResetResponse>(
    '/api/auth/reset-password/verify',
    { token }
  );
}

// 3. Réinitialiser le mot de passe
resetPassword(token: string, newPassword: string, confirmPassword: string): Observable<PasswordResetResponse> {
  return this.http.post<PasswordResetResponse>(
    '/api/auth/reset-password',
    { token, nouveauMotDePasse: newPassword, confirmationMotDePasse: confirmPassword }
  );
}
```

---

## 🗄️ Structure de la Base de Données

### Table `password_reset_tokens`

```sql
CREATE TABLE password_reset_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    expiration_date DATETIME NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    INDEX idx_token (token),
    INDEX idx_email (email),
    INDEX idx_expiration (expiration_date)
);
```

---

## 🔄 Nettoyage des Tokens Expirés

Un job planifié peut être configuré pour nettoyer automatiquement les tokens expirés :

```java
@Scheduled(cron = "0 0 2 * * ?") // Tous les jours à 2h du matin
public void cleanupExpiredTokens() {
    passwordResetService.cleanupExpiredTokens();
}
```

---

## ✅ Fonctionnalités

- ✅ Réinitialisation pour **élèves** et **administrateurs**
- ✅ Tokens sécurisés avec expiration
- ✅ Emails HTML personnalisés
- ✅ Validation des données
- ✅ Protection contre l'énumération d'emails
- ✅ Invalidation automatique des anciens tokens
- ✅ Email de confirmation après réinitialisation
- ✅ Documentation Swagger complète

---

## 🐛 Dépannage

### Problème : L'email n'est pas reçu

**Solutions** :
1. Vérifier la configuration SMTP dans `application.properties`
2. Vérifier les logs de l'application
3. Vérifier le dossier spam
4. Vérifier que l'email existe dans la base de données

### Problème : Token invalide ou expiré

**Solutions** :
1. Vérifier que le token n'a pas été utilisé
2. Vérifier que le token n'a pas expiré (24h par défaut)
3. Demander un nouveau token

### Problème : Les mots de passe ne correspondent pas

**Solution** : Vérifier que `nouveauMotDePasse` et `confirmationMotDePasse` sont identiques

---

## 📝 Notes Importantes

1. **URL du Frontend** : L'URL dans l'email doit être adaptée selon votre frontend (Angular, Flutter, etc.)
2. **HTTPS en Production** : Assurez-vous d'utiliser HTTPS en production
3. **Rate Limiting** : Considérez l'ajout d'un rate limiting pour éviter les abus
4. **Nettoyage** : Configurez un job pour nettoyer les tokens expirés régulièrement

---

## 🔗 Liens Utiles

- [Documentation Swagger](http://localhost:8080/api/swagger-ui.html)
- [Architecture Backend](ARCHITECTURE_BACKEND.md)
- [Guide d'Intégration Flutter](GUIDE_INTEGRATION_FLUTTER_CHALLENGES.md)

---

**Date de création** : 2024  
**Version** : 1.0.0

