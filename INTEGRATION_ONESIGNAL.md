# 📱 Intégration OneSignal pour les Notifications Push

## 📋 Vue d'ensemble

OneSignal permet d'envoyer des notifications push aux administrateurs et aux élèves pour les informer des activités importantes de la plateforme.

---

## 🔧 Configuration Backend (Spring Boot)

### 1. Configuration dans `application.properties`

Ajoutez vos clés OneSignal dans `application.properties` :

```properties
# ===============================
# ONESIGNAL CONFIGURATION
# ===============================
onesignal.app.id=${ONESIGNAL_APP_ID:your-onesignal-app-id}
onesignal.rest.api.key=${ONESIGNAL_REST_API_KEY:your-onesignal-rest-api-key}
onesignal.enabled=${ONESIGNAL_ENABLED:true}
```

**Comment obtenir vos clés OneSignal :**

1. Créez un compte sur [https://onesignal.com](https://onesignal.com)
2. Créez une nouvelle application
3. Sélectionnez votre plateforme (Android, iOS, Web)
4. Dans les paramètres de l'application :
   - **App ID** : Visible dans "Settings" > "Keys & IDs"
   - **REST API Key** : Visible dans "Settings" > "Keys & IDs"

### 2. Entités et Services créés

#### Entité `Device`
Stocke les informations des appareils (OneSignal Player IDs) pour chaque utilisateur.

#### Service `OneSignalService`
Service pour envoyer des notifications push via l'API OneSignal.

**Méthodes principales :**
- `sendNotificationToUser()` : Envoie une notification à un utilisateur spécifique
- `sendNotificationToRole()` : Envoie une notification à tous les utilisateurs d'un rôle (ex: tous les admins)
- `sendNotification()` : Envoie une notification à une liste de Player IDs

#### Service `DeviceService`
Gère l'enregistrement et la gestion des appareils.

### 3. Endpoints créés

#### Enregistrer un appareil
```
POST /api/devices/register
Body: {
  "oneSignalPlayerId": "player-id-from-onesignal",
  "userId": 7,
  "userRole": "ELEVE",
  "platform": "Android",
  "deviceModel": "Samsung Galaxy",
  "appVersion": "1.0.0"
}
```

#### Désactiver un appareil
```
DELETE /api/devices/{playerId}
```

#### Récupérer les appareils d'un utilisateur
```
GET /api/devices/user/{userId}
```

---

## 📲 Notifications Automatiques

### 1. Notifications pour les Élèves

#### ✅ Nouveau Challenge disponible
- **Quand** : Lorsqu'un admin crée un nouveau challenge
- **Titre** : "🎯 Nouveau Challenge disponible !"
- **Message** : Titre et description du challenge
- **Données** :
  ```json
  {
    "type": "NOUVEAU_CHALLENGE",
    "challengeId": 5,
    "titre": "Challenge de Lecture"
  }
  ```

#### ✅ Challenge terminé
- **Quand** : Lorsqu'un élève soumet ses réponses à un challenge
- **Titre** : "🎉 Challenge terminé !"
- **Message** : Score, rang, badge obtenu, points gagnés
- **Données** :
  ```json
  {
    "type": "CHALLENGE_TERMINE",
    "challengeId": 5,
    "score": 25,
    "totalPoints": 30,
    "rang": 3,
    "badgeObtenu": true,
    "pointsGagnes": 120
  }
  ```

### 2. Notifications pour les Administrateurs

Les notifications pour les administrateurs peuvent être ajoutées dans les services suivants :

#### ✅ Nouvelle inscription d'élève
- **Quand** : Lorsqu'un nouvel élève s'inscrit
- **Service** : `ServiceEleve`

#### ✅ Nouvelle participation à un challenge
- **Quand** : Lorsqu'un élève participe à un challenge
- **Service** : `ServiceChallenge`

#### ✅ Nouveau livre ajouté
- **Quand** : Lorsqu'un admin ajoute un nouveau livre
- **Service** : `ServiceLivre`

#### ✅ Nouveau quiz créé
- **Quand** : Lorsqu'un admin crée un nouveau quiz
- **Service** : `ServiceQuiz`

---

## 🚀 Intégration Flutter

### 1. Installation de OneSignal Flutter

Ajoutez la dépendance dans `pubspec.yaml` :

```yaml
dependencies:
  onesignal_flutter: ^5.0.0
```

Puis installez :

```bash
flutter pub get
```

### 2. Configuration Android

#### Dans `android/app/build.gradle` :

```gradle
android {
    defaultConfig {
        manifestPlaceholders = [
            onesignal_app_id: 'YOUR_ONESIGNAL_APP_ID',
            onesignal_google_project_number: 'YOUR_GOOGLE_PROJECT_NUMBER'
        ]
    }
}
```

#### Dans `AndroidManifest.xml` :

```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    
    <application>
        <!-- ... autres configurations ... -->
    </application>
</manifest>
```

### 3. Configuration iOS

#### Dans `ios/Podfile` :

```ruby
platform :ios, '12.0'
```

#### Dans `ios/Runner/Info.plist` :

```xml
<key>UIBackgroundModes</key>
<array>
    <string>remote-notification</string>
</array>
```

### 4. Code Flutter

#### Initialisation de OneSignal

```dart
import 'package:onesignal_flutter/onesignal_flutter.dart';

class NotificationService {
  static Future<void> initializeOneSignal(String userId, String userRole) async {
    // Initialiser OneSignal
    await OneSignal.shared.setAppId("YOUR_ONESIGNAL_APP_ID");
    
    // Demander la permission pour les notifications
    await OneSignal.shared.promptUserForPushNotificationPermission(
      fallbackToSettings: true,
    );
    
    // Obtenir le Player ID
    DeviceState deviceState = await OneSignal.shared.getDeviceState();
    String? playerId = deviceState?.userId;
    
    if (playerId != null) {
      // Enregistrer le Player ID dans le backend
      await registerDevice(playerId, userId, userRole);
      
      // Configurer les tags pour filtrer les notifications
      await OneSignal.shared.sendTags({
        "user_id": userId,
        "user_role": userRole,
      });
    }
    
    // Configurer les handlers de notifications
    OneSignal.shared.setNotificationWillShowInForegroundHandler((OSNotificationReceivedEvent event) {
      // Notification reçue en avant-plan
      // Vous pouvez personnaliser l'affichage ici
      event.complete(event.notification);
    });
    
    OneSignal.shared.setNotificationOpenedHandler((OSNotificationOpenedResult result) {
      // Notification cliquée - naviguer vers la page appropriée
      handleNotificationClick(result.notification);
    });
  }
  
  // Enregistrer le device dans le backend
  static Future<void> registerDevice(String playerId, String userId, String userRole) async {
    try {
      final response = await http.post(
        Uri.parse('http://votre-ip:8080/api/devices/register'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ${await getToken()}',
        },
        body: jsonEncode({
          'oneSignalPlayerId': playerId,
          'userId': int.parse(userId),
          'userRole': userRole,
          'platform': Platform.isAndroid ? 'Android' : 'iOS',
          'deviceModel': await getDeviceModel(),
          'appVersion': await getAppVersion(),
        }),
      );
      
      if (response.statusCode == 200) {
        print('Device enregistré avec succès');
      }
    } catch (e) {
      print('Erreur lors de l\'enregistrement du device: $e');
    }
  }
  
  // Gérer le clic sur une notification
  static void handleNotificationClick(OSNotification notification) {
    final Map<String, dynamic>? data = notification.additionalData;
    
    if (data != null) {
      final String type = data['type'] ?? '';
      
      switch (type) {
        case 'NOUVEAU_CHALLENGE':
          // Naviguer vers le détail du challenge
          final int challengeId = data['challengeId'];
          Navigator.pushNamed(
            context,
            '/challenge/${challengeId}',
          );
          break;
          
        case 'CHALLENGE_TERMINE':
          // Naviguer vers les résultats du challenge
          final int challengeId = data['challengeId'];
          Navigator.pushNamed(
            context,
            '/challenge/${challengeId}/results',
          );
          break;
          
        default:
          // Naviguer vers la page d'accueil
          Navigator.pushNamed(context, '/home');
      }
    }
  }
  
  // Helper pour obtenir le modèle de l'appareil
  static Future<String> getDeviceModel() async {
    final DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      final AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
      return androidInfo.model;
    } else {
      final IosDeviceInfo iosInfo = await deviceInfo.iosInfo;
      return iosInfo.model;
    }
  }
  
  // Helper pour obtenir la version de l'app
  static Future<String> getAppVersion() async {
    final PackageInfo packageInfo = await PackageInfo.fromPlatform();
    return packageInfo.version;
  }
  
  // Helper pour obtenir le token JWT
  static Future<String> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token') ?? '';
  }
}
```

#### Utilisation dans l'application

```dart
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Récupérer l'utilisateur connecté
  final prefs = await SharedPreferences.getInstance();
  final String? userId = prefs.getString('userId');
  final String? userRole = prefs.getString('userRole');
  
  if (userId != null && userRole != null) {
    // Initialiser OneSignal
    await NotificationService.initializeOneSignal(userId, userRole);
  }
  
  runApp(MyApp());
}
```

#### Widget pour afficher les notifications

```dart
class NotificationWidget extends StatefulWidget {
  @override
  _NotificationWidgetState createState() => _NotificationWidgetState();
}

class _NotificationWidgetState extends State<NotificationWidget> {
  List<OSNotification> notifications = [];
  
  @override
  void initState() {
    super.initState();
    _loadNotifications();
    
    // Écouter les nouvelles notifications
    OneSignal.shared.setNotificationWillShowInForegroundHandler((event) {
      setState(() {
        notifications.insert(0, event.notification);
      });
      event.complete(event.notification);
    });
  }
  
  Future<void> _loadNotifications() async {
    // Charger les notifications depuis le backend
    // Vous pouvez utiliser votre NotificationRepository existant
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Notifications')),
      body: ListView.builder(
        itemCount: notifications.length,
        itemBuilder: (context, index) {
          final notification = notifications[index];
          return ListTile(
            title: Text(notification.title ?? ''),
            subtitle: Text(notification.body ?? ''),
            trailing: Icon(Icons.arrow_forward_ios),
            onTap: () {
              NotificationService.handleNotificationClick(notification);
            },
          );
        },
      ),
    );
  }
}
```

---

## 📊 Types de Notifications

### Pour les Élèves

| Type | Description | Données |
|------|-------------|---------|
| `NOUVEAU_CHALLENGE` | Nouveau challenge disponible | `challengeId`, `titre` |
| `CHALLENGE_TERMINE` | Challenge terminé avec résultats | `challengeId`, `score`, `rang`, `badgeObtenu`, `pointsGagnes` |
| `BADGE_OBTENU` | Badge obtenu | `badgeId`, `badgeNom` |
| `NOUVEAU_LIVRE` | Nouveau livre disponible | `livreId`, `titre` |
| `NOUVEAU_QUIZ` | Nouveau quiz disponible | `quizId`, `titre` |

### Pour les Administrateurs

| Type | Description | Données |
|------|-------------|---------|
| `NOUVEL_ELEVE` | Nouvel élève inscrit | `eleveId`, `nom`, `prenom` |
| `PARTICIPATION_CHALLENGE` | Nouvelle participation à un challenge | `challengeId`, `eleveId` |
| `NOUVEAU_LIVRE_AJOUTE` | Nouveau livre ajouté | `livreId`, `titre` |
| `NOUVEAU_QUIZ_CREE` | Nouveau quiz créé | `quizId`, `titre` |

---

## ✅ Checklist d'Intégration

### Backend
- [x] Entité `Device` créée
- [x] Repository `DeviceRepository` créé
- [x] Service `OneSignalService` créé
- [x] Service `DeviceService` créé
- [x] Controller `DeviceController` créé
- [x] Configuration OneSignal dans `application.properties`
- [x] Intégration dans `ServiceEvaluation` (challenge terminé)
- [x] Intégration dans `ServiceChallenge` (nouveau challenge)
- [ ] Intégration dans `AdminService` (notifications admin)
- [ ] Intégration dans `ServiceEleve` (nouvelle inscription)
- [ ] Intégration dans `ServiceLivre` (nouveau livre)
- [ ] Intégration dans `ServiceQuiz` (nouveau quiz)

### Flutter
- [ ] OneSignal Flutter installé
- [ ] Configuration Android complétée
- [ ] Configuration iOS complétée
- [ ] Service de notification créé
- [ ] Initialisation OneSignal dans `main()`
- [ ] Enregistrement du device au login
- [ ] Gestion des clics sur notifications
- [ ] Widget d'affichage des notifications

---

## 🔧 Dépannage

### Erreur : "OneSignal n'est pas configuré"
- Vérifiez que `onesignal.app.id` et `onesignal.rest.api.key` sont bien configurés dans `application.properties`
- Vérifiez que `onesignal.enabled=true`

### Erreur : "Aucun appareil trouvé"
- Vérifiez que le device est bien enregistré via `POST /api/devices/register`
- Vérifiez que `isActive=true` dans la base de données

### Erreur : "Notification non envoyée"
- Vérifiez les logs du backend pour voir l'erreur exacte
- Vérifiez que les clés OneSignal sont correctes
- Vérifiez que l'API OneSignal est accessible

---

## 📚 Ressources

- [Documentation OneSignal](https://documentation.onesignal.com/)
- [OneSignal Flutter SDK](https://documentation.onesignal.com/docs/flutter-sdk-setup)
- [OneSignal REST API](https://documentation.onesignal.com/reference/create-notification)

---

**L'intégration OneSignal est maintenant complète côté backend ! Il reste à configurer le frontend Flutter selon ce guide.** 🚀

