# 🔄 Guide Complet du Flux de Participation aux Challenges

## 📋 Vue d'ensemble

Ce guide explique **exactement** ce qui se passe lorsque vous cliquez sur "Participer au Challenge" et comment récupérer les questions après.

---

## ⚡ Réponse Rapide : Quand peut-on participer ?

**🎯 IMMÉDIATEMENT !** 

Dès que vous cliquez sur "Participer au Challenge" et que la demande réussit (code HTTP 200), vous pouvez **IMMÉDIATEMENT** :
- ✅ Charger les questions
- ✅ Commencer à répondre aux questions
- ✅ Soumettre vos réponses

**Il n'y a AUCUN délai d'attente ni approbation nécessaire !** La participation est créée instantanément avec le statut `"EN_COURS"`.

---

## 🎯 Flux Complet : Clic sur "Participer au Challenge"

### Étape 1 : Clic sur "Participer au Challenge"

**Endpoint à appeler** :
```
POST /api/challenges/participer/{eleveId}/{challengeId}
```

**Ou alternativement** :
```
POST /api/eleve/challenges/participer/{eleveId}/{challengeId}
```

**Authentification** : Requise (Bearer Token)
**Rôle** : ELEVE uniquement

**Exemple Flutter** :
```dart
Future<Participation> participerChallenge(int eleveId, int challengeId) async {
  try {
    final response = await dio.post(
      '/api/challenges/participer/$eleveId/$challengeId',
      options: Options(
        headers: {
          'Authorization': 'Bearer ${token}',
          'Content-Type': 'application/json',
        },
      ),
    );
    
    if (response.statusCode == 200) {
      final participation = Participation.fromJson(response.data);
      print('Participation créée: ${participation.id}');
      print('Statut: ${participation.statut}'); // Doit être "EN_COURS"
      return participation;
    }
    throw Exception('Erreur lors de la participation');
  } catch (e) {
    if (e.toString().contains('déjà participé')) {
      throw Exception('Vous participez déjà à ce challenge');
    }
    if (e.toString().contains('pas actuellement disponible')) {
      throw Exception('Ce challenge n\'est pas actuellement disponible');
    }
    throw Exception('Erreur: $e');
  }
}
```

**Réponse attendue (200 OK)** :
```json
{
  "id": 1,
  "eleve": {
    "id": 7,
    "nom": "DJIRE",
    "prenom": "Levier"
  },
  "challenge": {
    "id": 5,
    "titre": "Challenge de Lecture"
  },
  "score": 0,
  "rang": null,
  "tempsPasse": 0,
  "statut": "EN_COURS",
  "dateParticipation": "2025-01-21T14:30:00",
  "aParticiper": true
}
```

### Ce qui se passe dans le backend :

1. ✅ Vérification que l'élève existe
2. ✅ Vérification que le challenge existe
3. ✅ Vérification que le challenge est **actif** (date actuelle entre `dateDebut` et `dateFin`)
4. ✅ Vérification que l'élève **n'a pas déjà participé**
5. ✅ **Création IMMÉDIATE** d'une `Participation` avec :
   - `statut` : `"EN_COURS"` ← **Vous pouvez IMMÉDIATEMENT commencer !**
   - `score` : `0`
   - `rang` : `null`
   - `tempsPasse` : `0`
   - `aParticiper` : `true`

**⚡ IMPORTANT : Dès que vous recevez la réponse HTTP 200 avec `statut: "EN_COURS"`, vous pouvez IMMÉDIATEMENT charger les questions et commencer à répondre !**

### Erreurs possibles :

| Erreur | Code HTTP | Cause | Solution |
|--------|-----------|-------|----------|
| "Élève introuvable" | 404 | L'ID de l'élève n'existe pas | Vérifier l'ID de l'élève |
| "Challenge introuvable" | 404 | L'ID du challenge n'existe pas | Vérifier l'ID du challenge |
| "Ce challenge n'est pas actuellement disponible" | 500 | Le challenge n'est pas actif | Vérifier les dates du challenge |
| "Vous participez déjà à ce challenge" | 500 | L'élève a déjà participé | Récupérer la participation existante |

---

### Étape 2 : Récupérer les Questions du Challenge (IMMÉDIATEMENT après participation)

**⚡ Vous pouvez charger les questions IMMÉDIATEMENT après avoir reçu la confirmation de participation !**

**⚠️ IMPORTANT** : Vous **DOIVEZ** participer au challenge **AVANT** de récupérer les questions, mais c'est **instantané** - pas besoin d'attendre !

**Endpoint à appeler** :
```
GET /api/questions/by-challenges/{challengeId}
```

**Authentification** : Requise (Bearer Token)
**Rôle** : ELEVE ou ADMIN

**Exemple Flutter** :
```dart
Future<List<Question>> getQuestionsByChallenge(int challengeId) async {
  try {
    final response = await dio.get(
      '/api/questions/by-challenges/$challengeId',
      options: Options(
        headers: {
          'Authorization': 'Bearer ${token}',
        },
      ),
    );
    
    if (response.statusCode == 200) {
      final List<dynamic> data = response.data;
      final questions = data.map((json) => Question.fromJson(json)).toList();
      print('${questions.length} questions chargées');
      return questions;
    }
    throw Exception('Erreur lors de la récupération des questions');
  } catch (e) {
    throw Exception('Erreur: $e');
  }
}
```

**Réponse attendue (200 OK)** :
```json
[
  {
    "id": 1,
    "intitule": "Quelle est la capitale du Mali ?",
    "type": "QCU",
    "numeroOrdre": 1,
    "reponsesPossibles": [
      {
        "id": 10,
        "libelle": "Bamako",
        "estCorrecte": null  // ⚠️ Masqué pour les élèves
      },
      {
        "id": 11,
        "libelle": "Kayes",
        "estCorrecte": null  // ⚠️ Masqué pour les élèves
      }
    ]
  },
  {
    "id": 2,
    "intitule": "Sélectionnez les langues nationales du Mali :",
    "type": "QCM",
    "numeroOrdre": 2,
    "reponsesPossibles": [
      {
        "id": 15,
        "libelle": "Bambara",
        "estCorrecte": null  // ⚠️ Masqué pour les élèves
      },
      {
        "id": 16,
        "libelle": "Peul",
        "estCorrecte": null  // ⚠️ Masqué pour les élèves
      }
    ]
  }
]
```

**⚠️ IMPORTANT** : Le champ `estCorrecte` est automatiquement masqué (`null`) pour les élèves par le backend.

---

## 🔄 Flux Complet dans Flutter

### Exemple Complet : Écran de Participation

```dart
class ChallengeParticipationScreen extends StatefulWidget {
  final int challengeId;
  final int eleveId;

  const ChallengeParticipationScreen({
    Key? key,
    required this.challengeId,
    required this.eleveId,
  }) : super(key: key);

  @override
  _ChallengeParticipationScreenState createState() => _ChallengeParticipationScreenState();
}

class _ChallengeParticipationScreenState extends State<ChallengeParticipationScreen> {
  final Dio dio = Dio();
  bool isLoading = true;
  bool isParticipating = false;
  bool hasParticipated = false;
  List<Question> questions = [];
  Participation? participation;

  @override
  void initState() {
    super.initState();
    checkParticipationStatus();
  }

  Future<void> checkParticipationStatus() async {
    try {
      // Vérifier si l'élève a déjà participé
      final response = await dio.get(
        '/api/challenges/participation/${widget.eleveId}/${widget.challengeId}',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );
      
      if (response.statusCode == 200) {
        // L'élève a déjà participé
        setState(() {
          participation = ParticipationDetailResponse.fromJson(response.data);
          hasParticipated = true;
          isLoading = false;
        });
        
        // Si le statut est "EN_COURS", charger les questions
        if (participation!.statut == 'EN_COURS') {
          loadQuestions();
        }
      }
    } catch (e) {
      // Pas encore de participation - proposer de participer
      setState(() {
        hasParticipated = false;
        isLoading = false;
      });
    }
  }

  Future<void> participateInChallenge() async {
    try {
      setState(() {
        isParticipating = true;
      });

      // ÉTAPE 1 : Participer au challenge
      final response = await dio.post(
        '/api/challenges/participer/${widget.eleveId}/${widget.challengeId}',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        final participationData = Participation.fromJson(response.data);
        
        setState(() {
          participation = participationData;
          hasParticipated = true;
          isParticipating = false;
        });

        // ÉTAPE 2 : Charger les questions après participation
        await loadQuestions();
        
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Participation réussie ! Vous pouvez maintenant répondre aux questions.')),
        );
      }
    } catch (e) {
      setState(() {
        isParticipating = false;
      });
      
      String errorMessage = 'Erreur lors de la participation';
      if (e.toString().contains('déjà participé')) {
        errorMessage = 'Vous participez déjà à ce challenge';
        // Charger les questions même si déjà participé
        await loadQuestions();
      } else if (e.toString().contains('pas actuellement disponible')) {
        errorMessage = 'Ce challenge n\'est pas actuellement disponible';
      }
      
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(errorMessage)),
      );
    }
  }

  Future<void> loadQuestions() async {
    try {
      setState(() {
        isLoading = true;
      });

      // ÉTAPE 2 : Récupérer les questions du challenge
      final response = await dio.get(
        '/api/questions/by-challenges/${widget.challengeId}',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        final loadedQuestions = data.map((json) => Question.fromJson(json)).toList();
        
        setState(() {
          questions = loadedQuestions;
          isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        isLoading = false;
      });
      
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erreur lors du chargement des questions: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('Chargement...')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    // Si pas encore participé, proposer de participer
    if (!hasParticipated) {
      return Scaffold(
        appBar: AppBar(title: Text('Participer au Challenge')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Voulez-vous participer à ce challenge ?'),
              SizedBox(height: 16),
              ElevatedButton(
                onPressed: isParticipating ? null : participateInChallenge,
                child: isParticipating
                    ? CircularProgressIndicator()
                    : Text('Participer'),
              ),
            ],
          ),
        ),
      );
    }

    // Si déjà participé et terminé, afficher les résultats
    if (participation != null && participation!.statut == 'TERMINE') {
      return Scaffold(
        appBar: AppBar(title: Text('Challenge Terminé')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Score: ${participation!.score}/${participation!.totalPoints}'),
              if (participation!.rang != null)
                Text('Rang: ${participation!.rang}'),
            ],
          ),
        ),
      );
    }

    // Si participé mais pas encore terminé, afficher les questions
    if (questions.isEmpty) {
      return Scaffold(
        appBar: AppBar(title: Text('Chargement des questions...')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    // Afficher les questions
    return Scaffold(
      appBar: AppBar(title: Text('Challenge')),
      body: ListView.builder(
        itemCount: questions.length,
        itemBuilder: (context, index) {
          final question = questions[index];
          return QuestionWidget(
            question: question,
            questionNumber: index + 1,
          );
        },
      ),
    );
  }
}
```

---

## ⚠️ Points Importants

### 1. Ordre des Appels API

**✅ Correct** :
1. `POST /api/challenges/participer/{eleveId}/{challengeId}` → Participer
2. `GET /api/questions/by-challenges/{challengeId}` → Charger les questions

**❌ Incorrect** :
1. Charger les questions avant de participer → Erreur possible

### 2. Gestion des Erreurs

**Si l'erreur est "Vous participez déjà à ce challenge"** :
- L'élève a déjà participé
- Récupérer la participation existante avec `GET /api/challenges/participation/{eleveId}/{challengeId}`
- Si le statut est `"EN_COURS"`, charger les questions

**Si l'erreur est "Ce challenge n'est pas actuellement disponible"** :
- Le challenge n'est pas actif
- Vérifier les dates du challenge
- Afficher un message d'erreur à l'utilisateur

### 3. Statuts de Participation

| Statut | Signification | Action |
|--------|---------------|--------|
| `"EN_COURS"` | L'élève a participé mais n'a pas encore soumis ses réponses | Charger les questions |
| `"TERMINE"` | L'élève a soumis ses réponses | Afficher les résultats |
| `"VALIDE"` | Participation validée par un admin | Afficher les résultats |
| `"DISQUALIFIE"` | Participation disqualifiée | Afficher un message d'erreur |

---

## 🔍 Débogage

### Problème : Les questions ne s'affichent pas

**Vérifications** :
1. ✅ Vérifier que la participation a été créée (statut 200)
2. ✅ Vérifier que le statut de la participation est `"EN_COURS"`
3. ✅ Vérifier que l'endpoint `/api/questions/by-challenges/{challengeId}` retourne des données
4. ✅ Vérifier que le challenge a bien des questions associées
5. ✅ Vérifier les logs du backend pour voir les erreurs

**Logs à vérifier** :
- Backend : Vérifier les logs Spring Boot
- Frontend : Vérifier la console du navigateur/Flutter DevTools

### Problème : Erreur lors de la participation

**Vérifications** :
1. ✅ Vérifier que l'élève existe (ID correct)
2. ✅ Vérifier que le challenge existe (ID correct)
3. ✅ Vérifier que le challenge est actif (dates)
4. ✅ Vérifier que l'élève n'a pas déjà participé
5. ✅ Vérifier que l'authentification est correcte (token JWT valide)

---

Ce guide vous donne **exactement** ce qui se passe lors du clic sur "Participer au Challenge" ! 🚀

