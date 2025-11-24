# 📚 Explication : Système de Progression des Livres de Lecture

## ❓ Question

**"C'est quand on ouvre les livres pour lecture que la progression des livres de lecture avance ou quoi ?"**

## ✅ Réponse

**NON**, la progression **ne s'avance PAS automatiquement** quand on ouvre un livre. Elle doit être **mise à jour manuellement** par le frontend (Flutter) pendant que l'élève lit le livre.

---

## 🔄 Comment Fonctionne la Progression

### 1. **Ouverture du Livre** (Lecture)
Quand l'élève ouvre un livre pour le lire :
- ✅ Le frontend charge le livre et affiche les pages
- ❌ **La progression n'est PAS mise à jour automatiquement**
- Le frontend doit **suivre la page actuelle** pendant la lecture

### 2. **Pendant la Lecture**
Le frontend doit :
- ✅ **Détecter** quand l'élève change de page (scroll, swipe, etc.)
- ✅ **Enregistrer** la page actuelle dans l'état local
- ✅ **Envoyer** périodiquement la progression au backend

### 3. **Mise à Jour de la Progression**
Le frontend doit appeler l'endpoint pour mettre à jour la progression :

```dart
// Exemple Flutter
POST /api/livres/progression/{eleveId}/{livreId}
Body: {
  "pageActuelle": 15  // Page actuelle où se trouve l'élève
}
```

---

## 📊 Structure de la Progression

### Entité `Progression`
```java
- id: Long
- eleve: Eleve (relation)
- livre: Livre (relation)
- pageActuelle: Integer (page où se trouve l'élève)
- pourcentageCompletion: Integer (calculé automatiquement)
- tempsLecture: Integer (en minutes)
- dateDerniereLecture: LocalDateTime
```

### Calcul Automatique
Le backend calcule automatiquement le **pourcentage de completion** :
```java
pourcentageCompletion = (pageActuelle * 100) / totalPages
```

**Exemple** :
- Livre avec 100 pages
- Élève à la page 25
- `pourcentageCompletion = (25 * 100) / 100 = 25%`

---

## 🔌 Endpoints Disponibles

### 1. **Mettre à Jour la Progression**
```
POST /api/livres/progression/{eleveId}/{livreId}
```

**Body** :
```json
{
  "pageActuelle": 15
}
```

**Réponse** :
```json
{
  "id": 1,
  "eleveId": 7,
  "eleveNom": "DJIRE Levier",
  "livreId": 21,
  "livreTitre": "La chevre de ma mere",
  "pageActuelle": 15,
  "pourcentageCompletion": 42,
  "dateMiseAJour": "2025-01-23T10:30:00"
}
```

**Comportement** :
- Si une progression existe déjà → **Mise à jour**
- Si aucune progression n'existe → **Création** d'une nouvelle progression

---

### 2. **Récupérer la Progression d'un Livre Spécifique**
```
GET /api/livres/progression/{eleveId}/{livreId}
```

**Réponse** :
```json
{
  "id": 1,
  "eleveId": 7,
  "eleveNom": "DJIRE Levier",
  "livreId": 21,
  "livreTitre": "La chevre de ma mere",
  "pageActuelle": 15,
  "pourcentageCompletion": 42,
  "dateMiseAJour": "2025-01-23T10:30:00"
}
```

**Si aucune progression** :
```json
{
  "eleveId": 7,
  "livreId": 21,
  "pageActuelle": 0,
  "pourcentageCompletion": 0
}
```

---

### 3. **Récupérer Toutes les Progressions d'un Élève**
```
GET /api/livres/progression/{eleveId}
```

**Réponse** :
```json
[
  {
    "id": 1,
    "eleveId": 7,
    "livreId": 21,
    "livreTitre": "La chevre de ma mere",
    "pageActuelle": 15,
    "pourcentageCompletion": 42,
    "dateMiseAJour": "2025-01-23T10:30:00"
  },
  {
    "id": 2,
    "eleveId": 7,
    "livreId": 22,
    "livreTitre": "Avalez le crapaud",
    "pageActuelle": 30,
    "pourcentageCompletion": 58,
    "dateMiseAJour": "2025-01-22T14:20:00"
  }
]
```

---

## 💡 Implémentation Flutter Recommandée

### 1. **Quand Mettre à Jour la Progression ?**

#### Option A : Mise à Jour Continue (Recommandé)
```dart
// Pendant la lecture, mettre à jour toutes les 5 pages ou toutes les 30 secondes
Timer.periodic(Duration(seconds: 30), (timer) {
  if (currentPage != lastSavedPage) {
    updateProgression(eleveId, livreId, currentPage);
    lastSavedPage = currentPage;
  }
});
```

#### Option B : Mise à Jour sur Changement de Page
```dart
// Quand l'élève change de page
onPageChanged: (int page) {
  updateProgression(eleveId, livreId, page);
}
```

#### Option C : Mise à Jour à la Fermeture
```dart
// Quand l'élève ferme le livre
onClose: () {
  updateProgression(eleveId, livreId, currentPage);
}
```

---

### 2. **Exemple de Code Flutter**

```dart
class BookReaderScreen extends StatefulWidget {
  final int eleveId;
  final int livreId;
  final int totalPages;
  
  @override
  _BookReaderScreenState createState() => _BookReaderScreenState();
}

class _BookReaderScreenState extends State<BookReaderScreen> {
  int currentPage = 0;
  int lastSavedPage = 0;
  Timer? progressionTimer;
  
  @override
  void initState() {
    super.initState();
    // Charger la progression existante
    loadProgression();
    
    // Mettre à jour la progression toutes les 30 secondes
    progressionTimer = Timer.periodic(Duration(seconds: 30), (timer) {
      if (currentPage != lastSavedPage) {
        updateProgression();
      }
    });
  }
  
  @override
  void dispose() {
    // Sauvegarder la progression avant de fermer
    updateProgression();
    progressionTimer?.cancel();
    super.dispose();
  }
  
  Future<void> loadProgression() async {
    try {
      final progression = await apiService.getProgressionLivre(
        widget.eleveId, 
        widget.livreId
      );
      if (progression != null) {
        setState(() {
          currentPage = progression.pageActuelle;
          lastSavedPage = progression.pageActuelle;
        });
      }
    } catch (e) {
      print('Erreur lors du chargement de la progression: $e');
    }
  }
  
  Future<void> updateProgression() async {
    try {
      await apiService.updateProgressionLecture(
        widget.eleveId,
        widget.livreId,
        currentPage,
      );
      lastSavedPage = currentPage;
      print('✅ Progression mise à jour: page $currentPage');
    } catch (e) {
      print('❌ Erreur lors de la mise à jour: $e');
    }
  }
  
  void onPageChanged(int page) {
    setState(() {
      currentPage = page;
    });
    // Optionnel: mettre à jour immédiatement
    // updateProgression();
  }
  
  @override
  Widget build(BuildContext context) {
    return PageView(
      controller: PageController(initialPage: currentPage),
      onPageChanged: onPageChanged,
      children: [
        // Pages du livre
      ],
    );
  }
}
```

---

## 📝 Points Importants

### ✅ Ce que le Backend Fait Automatiquement
1. **Calcule le pourcentage** : `(pageActuelle * 100) / totalPages`
2. **Met à jour la date** : `dateDerniereLecture = maintenant`
3. **Crée ou met à jour** : Si la progression existe, elle est mise à jour, sinon créée

### ❌ Ce que le Backend NE Fait PAS
1. **Ne détecte PAS** automatiquement quand l'élève lit
2. **Ne suit PAS** automatiquement la page actuelle
3. **Ne met PAS à jour** automatiquement la progression

### ✅ Ce que le Frontend DOIT Faire
1. **Détecter** la page actuelle pendant la lecture
2. **Appeler** l'endpoint `POST /api/livres/progression/{eleveId}/{livreId}` périodiquement
3. **Sauvegarder** la progression avant de fermer le livre

---

## 🎯 Scénario Complet

### Scénario 1 : Première Lecture
1. Élève ouvre le livre → Frontend charge le livre
2. Élève commence à lire → Frontend suit la page actuelle
3. Élève arrive à la page 5 → Frontend appelle `POST /progression/7/21` avec `pageActuelle: 5`
4. Backend crée une nouvelle progression avec `pageActuelle: 5`, `pourcentageCompletion: 14%`
5. Élève continue → Frontend continue de mettre à jour périodiquement

### Scénario 2 : Reprendre la Lecture
1. Élève ouvre le livre → Frontend appelle `GET /progression/7/21`
2. Backend retourne `pageActuelle: 15`, `pourcentageCompletion: 42%`
3. Frontend charge le livre à la page 15
4. Élève continue à lire → Frontend met à jour la progression

### Scénario 3 : Fin de Lecture
1. Élève arrive à la dernière page → Frontend appelle `POST /progression/7/21` avec `pageActuelle: 36`
2. Backend met à jour la progression avec `pageActuelle: 36`, `pourcentageCompletion: 100%`
3. Livre marqué comme "terminé" dans les statistiques

---

## 🔍 Vérification

Pour vérifier que la progression fonctionne :

1. **Ouvrir un livre** dans Flutter
2. **Lire quelques pages**
3. **Vérifier** que `POST /api/livres/progression/{eleveId}/{livreId}` est appelé
4. **Vérifier** la réponse avec `pageActuelle` et `pourcentageCompletion`
5. **Fermer et rouvrir** le livre
6. **Vérifier** que `GET /api/livres/progression/{eleveId}/{livreId}` retourne la bonne page

---

## 📊 Résumé

| Action | Automatique ? | Qui le Fait ? |
|--------|---------------|---------------|
| Ouvrir le livre | ✅ Oui | Frontend |
| Suivre la page actuelle | ❌ Non | Frontend (doit implémenter) |
| Mettre à jour la progression | ❌ Non | Frontend (doit appeler l'API) |
| Calculer le pourcentage | ✅ Oui | Backend |
| Sauvegarder en base | ✅ Oui | Backend |
| Charger la progression | ❌ Non | Frontend (doit appeler l'API) |

---

## ✅ Checklist pour le Frontend

- [ ] Détecter la page actuelle pendant la lecture
- [ ] Appeler `POST /api/livres/progression/{eleveId}/{livreId}` périodiquement
- [ ] Appeler `GET /api/livres/progression/{eleveId}/{livreId}` à l'ouverture
- [ ] Sauvegarder la progression avant de fermer le livre
- [ ] Afficher le pourcentage de completion dans l'UI
- [ ] Gérer les erreurs de connexion (retry, cache local)

---

## 🚀 Conclusion

**La progression n'est PAS automatique**. Le frontend doit :
1. **Suivre** la page actuelle pendant la lecture
2. **Appeler** l'endpoint de mise à jour périodiquement
3. **Charger** la progression existante à l'ouverture

Le backend calcule automatiquement le pourcentage et sauvegarde en base, mais il ne peut pas savoir où se trouve l'élève dans le livre sans que le frontend le lui dise.

