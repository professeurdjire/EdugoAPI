# 📱 Guide Complet des Notifications : Flutter (Élève) et Angular (Admin)

## 📋 Table des matières
1. [Notifications Flutter (Élèves)](#notifications-flutter-élèves)
2. [Notifications Angular (Admin)](#notifications-angular-admin)
3. [Configuration OneSignal](#configuration-onesignal)
4. [Types de Notifications](#types-de-notifications)

---

## 📱 Notifications Flutter (Élèves)

### 1. Installation et Configuration

#### Installation OneSignal Flutter

Ajoutez dans `pubspec.yaml` :

```yaml
dependencies:
  onesignal_flutter: ^5.0.0
  shared_preferences: ^2.2.0
  device_info_plus: ^9.0.0
  package_info_plus: ^5.0.0
  http: ^1.1.0
```

Puis :
```bash
flutter pub get
```

#### Configuration Android

Dans `android/app/build.gradle` :

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

Dans `android/app/src/main/AndroidManifest.xml` :

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

#### Configuration iOS

Dans `ios/Runner/Info.plist` :

```xml
<key>UIBackgroundModes</key>
<array>
    <string>remote-notification</string>
</array>
```

### 2. Service de Notification Flutter

#### Créer le Service de Notification

```dart
// lib/services/notification_service.dart
import 'package:onesignal_flutter/onesignal_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';

class NotificationService {
  static final NotificationService _instance = NotificationService._internal();
  factory NotificationService() => _instance;
  NotificationService._internal();

  static const String oneSignalAppId = 'YOUR_ONESIGNAL_APP_ID'; // Remplacez par votre App ID
  bool _initialized = false;

  /// Initialiser OneSignal
  Future<void> initialize(String userId, String userRole) async {
    if (_initialized) return;

    try {
      // Initialiser OneSignal
      await OneSignal.shared.setAppId(oneSignalAppId);
      
      // Demander la permission pour les notifications
      await OneSignal.shared.promptUserForPushNotificationPermission(
        fallbackToSettings: true,
      );

      // Obtenir le Player ID
      DeviceState? deviceState = await OneSignal.shared.getDeviceState();
      String? playerId = deviceState?.userId;

      if (playerId != null) {
        print('OneSignal Player ID: $playerId');
        
        // Enregistrer le device dans le backend
        await registerDevice(playerId, userId, userRole);
        
        // Configurer les tags pour filtrer les notifications
        await OneSignal.shared.sendTags({
          "user_id": userId,
          "user_role": userRole,
        });
      }

      // Configurer les handlers de notifications
      _setupNotificationHandlers();

      _initialized = true;
      print('OneSignal initialisé avec succès');
    } catch (e) {
      print('Erreur lors de l\'initialisation de OneSignal: $e');
    }
  }

  /// Configurer les handlers de notifications
  void _setupNotificationHandlers() {
    // Notification reçue en avant-plan
    OneSignal.shared.setNotificationWillShowInForegroundHandler((OSNotificationReceivedEvent event) {
      OSNotification notification = event.notification;
      print('Notification reçue en avant-plan: ${notification.title}');
      
      // Afficher la notification
      event.complete(notification);
    });

    // Notification cliquée
    OneSignal.shared.setNotificationOpenedHandler((OSNotificationOpenedResult result) {
      OSNotification notification = result.notification;
      print('Notification cliquée: ${notification.title}');
      
      // Traiter le clic sur la notification
      _handleNotificationClick(notification);
    });
  }

  /// Enregistrer le device dans le backend
  Future<void> registerDevice(String playerId, String userId, String userRole) async {
    try {
      final token = await _getToken();
      final platform = Platform.isAndroid ? 'Android' : 'iOS';
      final deviceModel = await _getDeviceModel();
      final appVersion = await _getAppVersion();

      final response = await http.post(
        Uri.parse('http://VOTRE_IP:8080/api/devices/register'), // Remplacez par votre IP
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode({
          'oneSignalPlayerId': playerId,
          'userId': int.parse(userId),
          'userRole': userRole,
          'platform': platform,
          'deviceModel': deviceModel,
          'appVersion': appVersion,
        }),
      );

      if (response.statusCode == 200) {
        print('Device enregistré avec succès');
      } else {
        print('Erreur lors de l\'enregistrement du device: ${response.statusCode}');
      }
    } catch (e) {
      print('Erreur lors de l\'enregistrement du device: $e');
    }
  }

  /// Gérer le clic sur une notification
  void _handleNotificationClick(OSNotification notification) {
    final Map<String, dynamic>? data = notification.additionalData;
    
    if (data != null) {
      final String type = data['type'] ?? '';
      
      // Naviguer vers la page appropriée selon le type de notification
      switch (type) {
        case 'NOUVEAU_CHALLENGE':
          final int challengeId = data['challengeId'];
          _navigateToChallenge(challengeId);
          break;
          
        case 'CHALLENGE_TERMINE':
          final int challengeId = data['challengeId'];
          _navigateToChallengeResults(challengeId);
          break;
          
        default:
          _navigateToHome();
      }
    } else {
      _navigateToHome();
    }
  }

  /// Naviguer vers un challenge
  void _navigateToChallenge(int challengeId) {
    // Implémentez votre navigation ici
    // Exemple avec GetX :
    // Get.to(() => ChallengeDetailScreen(challengeId: challengeId));
    
    // Exemple avec Navigator :
    // Navigator.pushNamed(context, '/challenge/$challengeId');
    print('Navigation vers challenge: $challengeId');
  }

  /// Naviguer vers les résultats d'un challenge
  void _navigateToChallengeResults(int challengeId) {
    // Implémentez votre navigation ici
    // Exemple : Navigator.pushNamed(context, '/challenge/$challengeId/results');
    print('Navigation vers résultats challenge: $challengeId');
  }

  /// Naviguer vers l'accueil
  void _navigateToHome() {
    // Implémentez votre navigation ici
    // Exemple : Navigator.pushNamedAndRemoveUntil(context, '/home', (route) => false);
    print('Navigation vers accueil');
  }

  /// Obtenir le token JWT
  Future<String> _getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token') ?? '';
  }

  /// Obtenir le modèle de l'appareil
  Future<String> _getDeviceModel() async {
    final DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      final AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
      return androidInfo.model;
    } else {
      final IosDeviceInfo iosInfo = await deviceInfo.iosInfo;
      return iosInfo.model;
    }
  }

  /// Obtenir la version de l'app
  Future<String> _getAppVersion() async {
    final PackageInfo packageInfo = await PackageInfo.fromPlatform();
    return packageInfo.version;
  }

  /// Désactiver le device (lors de la déconnexion)
  Future<void> deactivateDevice(String playerId) async {
    try {
      final token = await _getToken();
      final response = await http.delete(
        Uri.parse('http://VOTRE_IP:8080/api/devices/$playerId'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 204) {
        print('Device désactivé avec succès');
      }
    } catch (e) {
      print('Erreur lors de la désactivation du device: $e');
    }
  }
}
```

### 3. Initialisation dans l'Application Flutter

```dart
// lib/main.dart
import 'package:flutter/material.dart';
import 'services/notification_service.dart';
import 'services/auth_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialiser les notifications après le login
  await initializeNotifications();
  
  runApp(MyApp());
}

Future<void> initializeNotifications() async {
  // Récupérer l'utilisateur connecté
  final authService = AuthService();
  final user = await authService.getCurrentUser();
  
  if (user != null) {
    // Initialiser OneSignal avec l'ID et le rôle de l'utilisateur
    await NotificationService().initialize(
      user.id.toString(),
      'ELEVE', // ou récupérer depuis le user
    );
  }
}
```

### 4. Utilisation dans un Écran Flutter

```dart
class ChallengeDetailScreen extends StatelessWidget {
  final int challengeId;
  final int eleveId;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Challenge')),
      body: Column(
        children: [
          // ... autres widgets ...
          
          ElevatedButton(
            onPressed: () async {
              // Participer au challenge
              await participateInChallenge();
              
              // Les notifications seront automatiquement reçues
              // grâce à OneSignal
            },
            child: Text('Participer au Challenge'),
          ),
        ],
      ),
    );
  }
}
```

---

## 🖥️ Notifications Angular (Admin)

### 1. Installation et Configuration

#### Installation OneSignal Angular

```bash
npm install onesignal-ngx
```

Ou avec Angular CLI :

```bash
ng add onesignal-ngx
```

#### Configuration dans Angular

Dans `angular.json` :

```json
{
  "projects": {
    "your-app": {
      "architect": {
        "build": {
          "options": {
            "scripts": [
              "node_modules/onesignal-ngx/dist/OneSignal.js"
            ]
          }
        }
      }
    }
  }
}
```

### 2. Service de Notification Angular

#### Créer le Service de Notification

```typescript
// src/app/services/notification.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

declare var OneSignal: any;

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = environment.apiUrl; // http://VOTRE_IP:8080/api
  private oneSignalAppId = 'YOUR_ONESIGNAL_APP_ID'; // Remplacez par votre App ID
  private initialized = false;

  constructor(private http: HttpClient) {}

  /**
   * Initialiser OneSignal pour l'admin
   */
  async initialize(userId: number, userRole: string): Promise<void> {
    if (this.initialized) return;

    try {
      // Initialiser OneSignal
      await OneSignal.init({
        appId: this.oneSignalAppId,
        notifyButton: {
          enable: false
        },
        allowLocalhostAsSecureOrigin: true
      });

      // Obtenir le Player ID
      const playerId = await OneSignal.getUserId();
      
      if (playerId) {
        console.log('OneSignal Player ID:', playerId);
        
        // Enregistrer le device dans le backend
        await this.registerDevice(playerId, userId, userRole);
        
        // Configurer les tags
        OneSignal.sendTags({
          user_id: userId.toString(),
          user_role: userRole
        });
      }

      // Configurer les handlers
      this.setupNotificationHandlers();

      this.initialized = true;
      console.log('OneSignal initialisé avec succès');
    } catch (error) {
      console.error('Erreur lors de l\'initialisation de OneSignal:', error);
    }
  }

  /**
   * Configurer les handlers de notifications
   */
  private setupNotificationHandlers(): void {
    // Notification reçue
    OneSignal.addListenerForNotificationOpened((notification: any) => {
      console.log('Notification reçue:', notification);
      
      // Traiter la notification
      this.handleNotificationClick(notification);
    });
  }

  /**
   * Enregistrer le device dans le backend
   */
  private async registerDevice(playerId: string, userId: number, userRole: string): Promise<void> {
    try {
      const token = this.getToken();
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      });

      const deviceInfo = {
        oneSignalPlayerId: playerId,
        userId: userId,
        userRole: userRole,
        platform: 'Web',
        deviceModel: navigator.userAgent,
        appVersion: '1.0.0'
      };

      const response = await this.http.post(
        `${this.apiUrl}/devices/register`,
        deviceInfo,
        { headers }
      ).toPromise();

      console.log('Device enregistré avec succès');
    } catch (error) {
      console.error('Erreur lors de l\'enregistrement du device:', error);
    }
  }

  /**
   * Gérer le clic sur une notification
   */
  private handleNotificationClick(notification: any): void {
    const data = notification.additionalData;
    
    if (data && data.type) {
      const type = data.type;
      
      switch (type) {
        case 'NOUVEL_ELEVE':
          // Naviguer vers la liste des élèves
          this.navigateToStudents();
          break;
          
        case 'NOUVEAU_LIVRE_AJOUTE':
          // Naviguer vers la liste des livres
          this.navigateToBooks();
          break;
          
        case 'PARTICIPATION_CHALLENGE':
          // Naviguer vers les participations
          this.navigateToParticipations();
          break;
          
        default:
          this.navigateToDashboard();
      }
    } else {
      this.navigateToDashboard();
    }
  }

  /**
   * Naviguer vers la liste des élèves
   */
  private navigateToStudents(): void {
    // Implémentez votre navigation ici
    // Exemple : this.router.navigate(['/admin/students']);
    console.log('Navigation vers liste des élèves');
  }

  /**
   * Naviguer vers la liste des livres
   */
  private navigateToBooks(): void {
    // Implémentez votre navigation ici
    // Exemple : this.router.navigate(['/admin/books']);
    console.log('Navigation vers liste des livres');
  }

  /**
   * Naviguer vers les participations
   */
  private navigateToParticipations(): void {
    // Implémentez votre navigation ici
    console.log('Navigation vers participations');
  }

  /**
   * Naviguer vers le dashboard
   */
  private navigateToDashboard(): void {
    // Implémentez votre navigation ici
    // Exemple : this.router.navigate(['/admin/dashboard']);
    console.log('Navigation vers dashboard');
  }

  /**
   * Obtenir le token JWT
   */
  private getToken(): string {
    return localStorage.getItem('token') || '';
  }

  /**
   * Désactiver le device (lors de la déconnexion)
   */
  async deactivateDevice(playerId: string): Promise<void> {
    try {
      const token = this.getToken();
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      await this.http.delete(
        `${this.apiUrl}/devices/${playerId}`,
        { headers }
      ).toPromise();

      console.log('Device désactivé avec succès');
    } catch (error) {
      console.error('Erreur lors de la désactivation du device:', error);
    }
  }
}
```

### 3. Initialisation dans Angular

Dans `app.component.ts` ou `auth.service.ts` :

```typescript
import { Component, OnInit } from '@angular/core';
import { NotificationService } from './services/notification.service';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  async ngOnInit() {
    // Initialiser les notifications après le login
    const user = this.authService.getCurrentUser();
    
    if (user && user.role === 'ADMIN') {
      await this.notificationService.initialize(user.id, 'ADMIN');
    }
  }
}
```

### 4. Composant d'Affichage des Notifications

```typescript
// src/app/components/notification-list/notification-list.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.css']
})
export class NotificationListComponent implements OnInit {
  notifications: any[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadNotifications();
  }

  async loadNotifications() {
    try {
      const token = localStorage.getItem('token');
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      const response = await this.http.get(
        `${environment.apiUrl}/admin/notifications/non-lues`,
        { headers }
      ).toPromise() as any[];

      this.notifications = response;
      this.loading = false;
    } catch (error) {
      console.error('Erreur lors du chargement des notifications:', error);
      this.loading = false;
    }
  }

  markAsRead(notificationId: number) {
    // Implémentez la logique pour marquer comme lu
    console.log('Marquer comme lu:', notificationId);
  }
}
```

```html
<!-- src/app/components/notification-list/notification-list.component.html -->
<div class="notification-list">
  <h2>Notifications</h2>
  
  <div *ngIf="loading" class="loading">
    Chargement...
  </div>
  
  <div *ngIf="!loading && notifications.length === 0" class="empty">
    Aucune notification
  </div>
  
  <div *ngIf="!loading && notifications.length > 0">
    <div *ngFor="let notification of notifications" class="notification-item">
      <h3>{{ notification.titre }}</h3>
      <p>{{ notification.message }}</p>
      <small>{{ notification.dateExplication | date:'short' }}</small>
      <button (click)="markAsRead(notification.id)">Marquer comme lu</button>
    </div>
  </div>
</div>
```

---

## 🔧 Configuration OneSignal

### Obtenir vos Clés OneSignal

1. Créer un compte sur [https://onesignal.com](https://onesignal.com)
2. Créer une nouvelle application
3. Sélectionner votre plateforme (Android, iOS, Web)
4. Dans "Settings" > "Keys & IDs" :
   - **App ID** : À utiliser dans le code
   - **REST API Key** : À mettre dans `application.properties` du backend

### Configuration Backend

Dans `src/main/resources/application.properties` :

```properties
onesignal.app.id=votre-app-id
onesignal.rest.api.key=votre-rest-api-key
onesignal.enabled=true
```

---

## 📊 Types de Notifications

### Pour les Élèves (Flutter)

| Type | Titre | Message | Données |
|------|-------|---------|---------|
| `NOUVEAU_CHALLENGE` | "🎯 Nouveau Challenge disponible !" | Titre + description | `challengeId`, `titre` |
| `CHALLENGE_TERMINE` | "🎉 Challenge terminé !" | Score, rang, badge, points | `challengeId`, `score`, `rang`, `badgeObtenu`, `pointsGagnes` |

### Pour les Administrateurs (Angular)

| Type | Titre | Message | Données |
|------|-------|---------|---------|
| `NOUVEL_ELEVE` | "👤 Nouvel élève inscrit" | Nom + prénom | `eleveId`, `nom`, `prenom` |
| `NOUVEAU_LIVRE_AJOUTE` | "📚 Nouveau livre ajouté" | Titre | `livreId`, `titre` |
| `PARTICIPATION_CHALLENGE` | "🎯 Nouvelle participation" | Élève + challenge | `challengeId`, `eleveId` |

---

Ce guide vous donne **tout** ce qu'il faut pour implémenter les notifications dans Flutter et Angular ! 🚀

