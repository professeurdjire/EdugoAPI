# 📧 Notifications Administrateurs : OneSignal + Email

## ✅ Configuration Complète

Tout est **déjà mis au point** côté backend pour OneSignal, et maintenant aussi pour l'envoi d'emails aux administrateurs !

---

## 🔧 Ce qui est déjà implémenté

### 1. Services Créés

#### ✅ `OneSignalService`
- Envoie des notifications push via OneSignal
- Supporte les notifications aux utilisateurs spécifiques
- Supporte les notifications par rôle (ELEVE, ADMIN)
- **Status** : ✅ Opérationnel

#### ✅ `EmailService`
- Envoie des emails simples et HTML
- Utilisé pour les emails de bienvenue
- **Status** : ✅ Opérationnel

#### ✅ `AdminNotificationService` (NOUVEAU)
- Combine OneSignal + Email pour les administrateurs
- Envoie automatiquement une notification push ET un email
- **Status** : ✅ Opérationnel

---

## 📨 Notifications Automatiques pour les Admins

Les administrateurs reçoivent automatiquement des notifications (OneSignal + Email) pour les événements suivants :

### 1. ✅ Nouvel élève inscrit

**Quand** : Lorsqu'un nouvel élève s'inscrit sur la plateforme

**Notification OneSignal** :
- Titre : "👤 Nouvel élève inscrit"
- Message : "Un nouvel élève vient de s'inscrire : [Prénom] [Nom]"
- Données : `eleveId`, `nom`, `prenom`, `email`

**Email** :
- Sujet : "👤 Nouvel élève inscrit"
- Contenu HTML avec détails de l'élève
- Lien vers la plateforme admin

**Implémenté dans** : `AuthService.register()`

---

### 2. ✅ Nouveau challenge créé

**Quand** : Lorsqu'un admin crée un nouveau challenge

**Notification OneSignal** :
- Titre : "✅ Nouveau Challenge créé"
- Message : "Un nouveau challenge a été créé : [Titre]"
- Données : `challengeId`, `titre`, `points`

**Email** :
- Sujet : "✅ Nouveau Challenge créé"
- Contenu HTML avec détails du challenge
- Lien vers la plateforme admin

**Implémenté dans** : `ServiceChallenge.createChallenge()`

---

### 3. ✅ Nouvelle participation à un challenge

**Quand** : Lorsqu'un élève participe à un challenge

**Notification OneSignal** :
- Titre : "🎯 Nouvelle participation"
- Message : "Un élève vient de participer au challenge : [Titre]"
- Données : `challengeId`, `eleveId`, `titre`

**Email** :
- Sujet : "🎯 Nouvelle participation"
- Contenu HTML avec détails de la participation
- Lien vers la plateforme admin

**À implémenter** : `ServiceChallenge.participerChallenge()`

---

## 🔧 Configuration Backend

### 1. Configuration OneSignal

Dans `application.properties` :

```properties
# ===============================
# ONESIGNAL CONFIGURATION
# ===============================
onesignal.app.id=${ONESIGNAL_APP_ID:your-onesignal-app-id}
onesignal.rest.api.key=${ONESIGNAL_REST_API_KEY:your-onesignal-rest-api-key}
onesignal.enabled=${ONESIGNAL_ENABLED:true}
```

### 2. Configuration Email

Dans `application.properties` :

```properties
# ===============================
# EMAIL CONFIGURATION
# ===============================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=professeurhamidoudjire@gmail.com
spring.mail.password=tvjp bcrz reff beak
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

**Note** : Les emails sont déjà configurés dans votre `application.properties`.

---

## 📧 Exemple d'Email Reçu par les Admins

Quand un événement se produit, les administrateurs reçoivent un email HTML comme celui-ci :

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🔔 Notification EDUGO
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✅ Nouveau Challenge créé

Un nouveau challenge a été créé : Challenge de Lecture Interclasse

Détails :
- Challenge ID: 5
- Titre: Challenge de Lecture Interclasse
- Points: 100

[Accéder à l'administration] (bouton)

© 2024 EDUGO - Plateforme éducative pour le Mali
Email: support@edugo.ml | Site: https://www.edugo.ml
```

---

## 🔄 Flux Complet

### Exemple : Nouvel élève inscrit

1. **Élève s'inscrit** via `POST /api/auth/register`
2. **Backend** :
   - ✅ Crée l'élève dans la base de données
   - ✅ Envoie un email de bienvenue à l'élève
   - ✅ **Appelle `AdminNotificationService.notifyAdmins()`**
3. **AdminNotificationService** :
   - ✅ Envoie notification OneSignal à tous les admins
   - ✅ Envoie email à tous les admins actifs
4. **Résultat** :
   - ✅ Les admins reçoivent une notification push (si app ouverte)
   - ✅ Les admins reçoivent un email dans leur boîte mail

---

## ✅ Checklist d'Implémentation

### Backend - OneSignal
- [x] Service OneSignal créé
- [x] Configuration OneSignal dans `application.properties`
- [x] Endpoints pour enregistrer les devices
- [x] Envoi de notifications push

### Backend - Email Admin
- [x] Service Email créé
- [x] Configuration Email dans `application.properties`
- [x] Service AdminNotificationService créé (combine OneSignal + Email)
- [x] Notifications pour nouveau challenge créé
- [x] Notifications pour nouvel élève inscrit
- [ ] Notifications pour nouvelle participation (optionnel)
- [ ] Notifications pour nouveau livre ajouté (optionnel)
- [ ] Notifications pour nouveau quiz créé (optionnel)

### Frontend Angular (Admin)
- [ ] Intégration OneSignal (voir `NOTIFICATIONS_FLUTTER_ANGULAR.md`)
- [ ] Affichage des notifications dans l'interface
- [ ] Gestion des clics sur les notifications

---

## 🔍 Vérification

### Comment vérifier que ça fonctionne ?

1. **Vérifier OneSignal** :
   - Se connecter en tant qu'admin sur Angular
   - S'inscrire pour OneSignal (voir `NOTIFICATIONS_FLUTTER_ANGULAR.md`)
   - Créer un nouveau challenge
   - Vérifier que la notification push arrive

2. **Vérifier Email** :
   - Vérifier l'email des admins dans la base de données
   - Créer un nouveau challenge ou inscrire un nouvel élève
   - Vérifier la boîte mail des admins

3. **Vérifier les logs** :
   - Regarder les logs du backend Spring Boot
   - Chercher les messages : "Email de notification envoyé à l'admin" ou "Notifications envoyées à X administrateur(s)"

---

## 📊 Résumé

| Fonctionnalité | Status Backend | Status Frontend |
|----------------|----------------|-----------------|
| OneSignal Push (Élèves) | ✅ Opérationnel | ⚠️ À configurer (Flutter) |
| OneSignal Push (Admins) | ✅ Opérationnel | ⚠️ À configurer (Angular) |
| Email Notifications (Admins) | ✅ Opérationnel | ✅ Automatique |
| Notifications automatiques | ✅ Opérationnel | - |

---

## 🎯 Prochaines Étapes

### Pour Angular (Admin)

Consultez `NOTIFICATIONS_FLUTTER_ANGULAR.md` section "Notifications Angular (Admin)" pour :
- Installer OneSignal dans Angular
- Configurer les notifications push
- Afficher les notifications dans l'interface

### Pour Ajouter Plus de Notifications

Si vous voulez ajouter des notifications pour d'autres événements :

1. Dans le service concerné (ex: `ServiceLivre`, `ServiceQuiz`)
2. Injecter `AdminNotificationService`
3. Appeler `adminNotificationService.notifyAdmins()` après l'événement

**Exemple** :
```java
// Dans ServiceLivre.createLivre()
Livre saved = livreRepository.save(livre);

// Notifier les admins
String titre = "📚 Nouveau livre ajouté";
String message = "Un nouveau livre a été ajouté : " + saved.getTitre();
Map<String, Object> data = new HashMap<>();
data.put("type", "NOUVEAU_LIVRE_AJOUTE");
data.put("livreId", saved.getId());
data.put("titre", saved.getTitre());

adminNotificationService.notifyAdmins(titre, message, data);
```

---

**Tout est prêt côté backend ! Il ne reste plus qu'à configurer OneSignal dans Angular pour recevoir les notifications push.** 🚀

