# 📝 Système Complet de Gestion des Questions et Évaluation

## 📋 Table des matières
1. [Types de Questions](#types-de-questions)
2. [Structure des Données](#structure-des-données)
3. [Récupération des Questions](#récupération-des-questions)
4. [Soumission des Réponses](#soumission-des-réponses)
5. [Calcul des Points](#calcul-des-points)
6. [Intégration Flutter](#intégration-flutter)
7. [Exemples de Code](#exemples-de-code)

---

## 🎯 Types de Questions

Le système supporte **4 types de questions** :

### 1. **QCU (Question à Choix Unique)**
- **Description** : Une seule réponse correcte parmi plusieurs choix
- **Validation** : Exactement **1 réponse correcte** requise dans `reponsesPossibles`
- **Minimum** : 2 options requises
- **Format de soumission** : Un seul `reponseId` dans le tableau

### 2. **QCM (Question à Choix Multiples)**
- **Description** : Plusieurs réponses correctes possibles
- **Validation** : Au moins **1 réponse correcte** requise
- **Minimum** : 2 options requises
- **Format de soumission** : Plusieurs `reponseIds` dans le tableau

### 3. **VRAI_FAUX**
- **Description** : Question Vrai/Faux classique
- **Validation** : Exactement **2 options** (Vrai et Faux), **1 seule correcte**
- **Format de soumission** : Un seul `reponseId` (celui de "Vrai" ou "Faux")

### 4. **APPARIEMENT**
- **Description** : Question d'appariement (en cours de développement)
- **Minimum** : 2 options requises

---

## 📊 Structure des Données

### QuestionResponse (Retournée par l'API)

```json
{
  "id": 1,
  "intitule": "Quelle est la capitale de la France ?",
  "type": "QCU",
  "numeroOrdre": 1,
  "reponsesPossibles": [
    {
      "id": 10,
      "libelle": "Paris",
      "estCorrecte": true
    },
    {
      "id": 11,
      "libelle": "Londres",
      "estCorrecte": false
    },
    {
      "id": 12,
      "libelle": "Berlin",
      "estCorrecte": false
    }
  ]
}
```

**Note importante** : Le champ `estCorrecte` est **automatiquement masqué (`null`)** pour les élèves par le backend. Les admins voient toujours les réponses correctes. Côté Flutter, vous n'avez rien à faire - le backend gère déjà la sécurité !

### ReponsePossibleResponse

```json
{
  "id": 10,
  "libelle": "Paris",
  "estCorrecte": true
}
```

---

## 🔍 Récupération des Questions

### Endpoints disponibles

✅ **MAINTENANT ACCESSIBLES AUX ÉLÈVES** : Ces endpoints sont maintenant accessibles aux élèves et aux admins.

⚠️ **SÉCURITÉ** : Pour les **élèves**, le champ `estCorrecte` est automatiquement masqué (`null`) dans les réponses pour éviter la triche. Seuls les **admins** voient les réponses correctes.

#### Pour Quiz :
```
GET /api/questions/by-quiz/{quizId}
```
**Rôle requis** : ELEVE ou ADMIN
**Authentification** : Requise (Bearer Token)

#### Pour Exercice :
```
GET /api/questions/by-exercices/{exerciceId}
```
**Rôle requis** : ELEVE ou ADMIN
**Authentification** : Requise (Bearer Token)

#### Pour Challenge :
```
GET /api/questions/by-challenges/{challengeId}
```
**Rôle requis** : ELEVE ou ADMIN
**Authentification** : Requise (Bearer Token)

#### Pour Défi :
```
GET /api/questions/by-defis/{defiId}
```
**Rôle requis** : ELEVE ou ADMIN
**Authentification** : Requise (Bearer Token)

---

## 📤 Soumission des Réponses

### Endpoints de soumission

#### 1. Soumettre un Quiz
```
POST /api/quizzes/{quizId}/submit
```

**Body (JSON)** :
```json
{
  "eleveId": 7,
  "reponses": [
    {
      "questionId": 1,
      "reponseIds": [10]
    },
    {
      "questionId": 2,
      "reponseIds": [15, 16]
    }
  ]
}
```

#### 2. Soumettre un Exercice
```
POST /api/exercices/{exerciceId}/submit
```

**Body** : Même format que pour Quiz

#### 3. Soumettre un Challenge
```
POST /api/challenges/{challengeId}/submit
```

**Body** : Même format que pour Quiz

### Format de SubmitRequest

```json
{
  "eleveId": 7,
  "reponses": [
    {
      "questionId": 1,
      "reponseIds": [10]  // Pour QCU/VRAI_FAUX : 1 seul ID
    },
    {
      "questionId": 2,
      "reponseIds": [15, 16, 17]  // Pour QCM : plusieurs IDs
    }
  ]
}
```

**Règles** :
- `reponseIds` est un tableau :
  - **QCU/VRAI_FAUX** : 1 seul ID (ex: `[10]`)
  - **QCM** : Plusieurs IDs (ex: `[15, 16, 17]`)
- Toutes les questions du quiz/exercice/challenge doivent être incluses dans `reponses`

---

## ✅ Réponse de Soumission (SubmitResultResponse)

Après soumission, vous recevez :

```json
{
  "ownerId": 5,
  "ownerType": "QUIZ",  // ou "EXERCICE" ou "CHALLENGE"
  "eleveId": 7,
  "score": 15,          // Points gagnés
  "totalPoints": 20,    // Points totaux possibles
  "details": [
    {
      "questionId": 1,
      "points": 10,
      "correct": true
    },
    {
      "questionId": 2,
      "points": 10,
      "correct": false
    }
  ]
}
```

**Champs importants** :
- `score` : Total des points gagnés pour les bonnes réponses
- `totalPoints` : Total des points possibles (somme de tous les `points` des questions)
- `details` : Détail question par question
  - `correct` : `true` si la réponse est correcte, `false` sinon
  - `points` : Points de la question (gagnés si `correct = true`)

---

## 🎯 Calcul des Points

### Logique de validation

#### Pour QCU et VRAI_FAUX :
1. Le système vérifie que **exactement 1 réponse** a été sélectionnée
2. Compare cette réponse avec la réponse correcte unique
3. Si les IDs correspondent : ✅ **Correct** → Points gagnés
4. Sinon : ❌ **Incorrect** → 0 points

**Code backend** :
```java
correct = selected.size() == 1 && correctIds.size() == 1 
         && selected.iterator().next().equals(correctIds.iterator().next());
```

#### Pour QCM :
1. Le système récupère **toutes les réponses correctes** de la question
2. Compare l'ensemble des réponses sélectionnées avec l'ensemble des réponses correctes
3. Si les deux ensembles sont identiques : ✅ **Correct** → Points gagnés
4. Sinon : ❌ **Incorrect** → 0 points

**Code backend** :
```java
correct = selected.equals(correctIds);
```

### Calcul du score final

```java
int score = 0;
for (Question q : questions) {
    boolean correct = // ... validation ...
    int pts = q.getPoints();
    if (correct) {
        score += pts;  // Ajoute les points si correct
    }
}
```

**Exemple** :
- Question 1 : 10 points → Correct → +10 points
- Question 2 : 5 points → Incorrect → +0 points
- Question 3 : 15 points → Correct → +15 points
- **Score total** : 25 points

---

## 💰 Ajout des Points à l'Élève

⚠️ **IMPORTANT** : Actuellement, le service `ServiceEvaluation` **ne crédite pas automatiquement** les points à l'élève. Il retourne seulement le score calculé.

### Solution (Flutter)

Vous devez appeler l'endpoint d'ajout de points **après** avoir reçu la réponse de soumission :

```
POST /api/eleve/ajouter-points/{eleveId}
Body: { "points": 25 }
```

**Exemple de flux Flutter** :
```dart
// 1. Soumettre les réponses
final result = await questionService.submitQuiz(quizId, eleveId, reponses);

// 2. Ajouter les points gagnés à l'élève
await questionService.addPointsToEleve(eleveId, result.score);

// 3. Récupérer le nouveau total de points
final newTotal = await questionService.getElevePoints(eleveId);
```

**Note** : Cette étape est importante car les points ne sont pas ajoutés automatiquement par le backend. Vous devez l'appeler manuellement après chaque soumission réussie.

### Récupération du total de points

Pour récupérer le total de points accumulés d'un élève :

```
GET /api/eleve/points/{id}
```

**Réponse** :
```json
{
  "points": 150
}
```

**Note** : Le champ `pointAccumule` est également disponible dans le profil de l'élève :
```
GET /api/auth/me
GET /api/eleve/profil/{id}
```

---

## 📱 Intégration Flutter

### 1. Modèles de données Flutter

#### Question Model

```dart
class Question {
  final int id;
  final String intitule;
  final String type; // "QCU", "QCM", "VRAI_FAUX", "APPARIEMENT"
  final int? numeroOrdre;
  final List<ReponsePossible> reponsesPossibles;

  Question({
    required this.id,
    required this.intitule,
    required this.type,
    this.numeroOrdre,
    required this.reponsesPossibles,
  });

  factory Question.fromJson(Map<String, dynamic> json) {
    return Question(
      id: json['id'],
      intitule: json['intitule'],
      type: json['type'],
      numeroOrdre: json['numeroOrdre'],
      reponsesPossibles: (json['reponsesPossibles'] as List)
          .map((r) => ReponsePossible.fromJson(r))
          .toList(),
    );
  }

  // Note: Le masquage des réponses correctes est fait automatiquement par le backend
  // Pour les élèves, estCorrecte sera toujours null
}
```

#### ReponsePossible Model

```dart
class ReponsePossible {
  final int id;
  final String libelle;
  final bool? estCorrecte; // null après hideCorrect()

  ReponsePossible({
    required this.id,
    required this.libelle,
    this.estCorrecte,
  });

  factory ReponsePossible.fromJson(Map<String, dynamic> json) {
    return ReponsePossible(
      id: json['id'],
      libelle: json['libelle'],
      estCorrecte: json['estCorrecte'],
    );
  }

  // Note: Le backend masque automatiquement estCorrecte pour les élèves
}
```

#### SubmitRequest Model

```dart
class SubmitRequest {
  final int eleveId;
  final List<SubmitAnswer> reponses;

  SubmitRequest({
    required this.eleveId,
    required this.reponses,
  });

  Map<String, dynamic> toJson() {
    return {
      'eleveId': eleveId,
      'reponses': reponses.map((r) => r.toJson()).toList(),
    };
  }
}

class SubmitAnswer {
  final int questionId;
  final List<int> reponseIds;

  SubmitAnswer({
    required this.questionId,
    required this.reponseIds,
  });

  Map<String, dynamic> toJson() {
    return {
      'questionId': questionId,
      'reponseIds': reponseIds,
    };
  }
}
```

#### SubmitResultResponse Model

```dart
class SubmitResultResponse {
  final int ownerId;
  final String ownerType; // "QUIZ", "EXERCICE", "CHALLENGE"
  final int eleveId;
  final int score;
  final int totalPoints;
  final List<ResultDetail> details;

  SubmitResultResponse({
    required this.ownerId,
    required this.ownerType,
    required this.eleveId,
    required this.score,
    required this.totalPoints,
    required this.details,
  });

  factory SubmitResultResponse.fromJson(Map<String, dynamic> json) {
    return SubmitResultResponse(
      ownerId: json['ownerId'],
      ownerType: json['ownerType'],
      eleveId: json['eleveId'],
      score: json['score'],
      totalPoints: json['totalPoints'],
      details: (json['details'] as List)
          .map((d) => ResultDetail.fromJson(d))
          .toList(),
    );
  }

  // Calculer le pourcentage
  double get percentage => totalPoints > 0 ? (score / totalPoints) * 100 : 0;
}

class ResultDetail {
  final int questionId;
  final int points;
  final bool correct;

  ResultDetail({
    required this.questionId,
    required this.points,
    required this.correct,
  });

  factory ResultDetail.fromJson(Map<String, dynamic> json) {
    return ResultDetail(
      questionId: json['questionId'],
      points: json['points'],
      correct: json['correct'],
    );
  }
}
```

### 2. Service API Flutter

```dart
class QuestionService {
  final String baseUrl = 'http://votre-ip:8080/api';
  final Dio dio;

  QuestionService(this.dio);

  // Récupérer les questions d'un quiz
  Future<List<Question>> getQuestionsByQuiz(int quizId) async {
    try {
      final response = await dio.get(
        '$baseUrl/questions/by-quiz/$quizId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Question.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération des questions');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer les questions d'un exercice
  Future<List<Question>> getQuestionsByExercice(int exerciceId) async {
    try {
      final response = await dio.get(
        '$baseUrl/questions/by-exercices/$exerciceId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Question.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération des questions');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer les questions d'un challenge
  Future<List<Question>> getQuestionsByChallenge(int challengeId) async {
    try {
      final response = await dio.get(
        '$baseUrl/questions/by-challenges/$challengeId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Question.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération des questions');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Soumettre un quiz
  Future<SubmitResultResponse> submitQuiz(
    int quizId,
    int eleveId,
    List<SubmitAnswer> reponses,
  ) async {
    try {
      final request = SubmitRequest(
        eleveId: eleveId,
        reponses: reponses,
      );

      final response = await dio.post(
        '$baseUrl/quizzes/$quizId/submit',
        data: request.toJson(),
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        return SubmitResultResponse.fromJson(response.data);
      }
      throw Exception('Erreur lors de la soumission');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Soumettre un exercice
  Future<SubmitResultResponse> submitExercice(
    int exerciceId,
    int eleveId,
    List<SubmitAnswer> reponses,
  ) async {
    try {
      final request = SubmitRequest(
        eleveId: eleveId,
        reponses: reponses,
      );

      final response = await dio.post(
        '$baseUrl/exercices/$exerciceId/submit',
        data: request.toJson(),
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        return SubmitResultResponse.fromJson(response.data);
      }
      throw Exception('Erreur lors de la soumission');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Soumettre un challenge
  Future<SubmitResultResponse> submitChallenge(
    int challengeId,
    int eleveId,
    List<SubmitAnswer> reponses,
  ) async {
    try {
      final request = SubmitRequest(
        eleveId: eleveId,
        reponses: reponses,
      );

      final response = await dio.post(
        '$baseUrl/challenges/$challengeId/submit',
        data: request.toJson(),
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        return SubmitResultResponse.fromJson(response.data);
      }
      throw Exception('Erreur lors de la soumission');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Ajouter des points à l'élève
  Future<void> addPointsToEleve(int eleveId, int points) async {
    try {
      final response = await dio.post(
        '$baseUrl/eleve/ajouter-points/$eleveId',
        data: {'points': points},
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode != 200) {
        throw Exception('Erreur lors de l\'ajout des points');
      }
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer les points totaux de l'élève
  Future<int> getElevePoints(int eleveId) async {
    try {
      final response = await dio.get(
        '$baseUrl/eleve/points/$eleveId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        return response.data['points'];
      }
      throw Exception('Erreur lors de la récupération des points');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Helper pour récupérer le token
  Future<String> getToken() async {
    // Implémentez la logique de récupération du token depuis votre stockage
    // Exemple avec SharedPreferences:
    // final prefs = await SharedPreferences.getInstance();
    // return prefs.getString('token') ?? '';
    return '';
  }
}
```

### 3. Widget Flutter pour afficher une question

```dart
class QuestionWidget extends StatefulWidget {
  final Question question;
  final Function(List<int> selectedIds) onAnswerSelected;

  const QuestionWidget({
    Key? key,
    required this.question,
    required this.onAnswerSelected,
  }) : super(key: key);

  @override
  _QuestionWidgetState createState() => _QuestionWidgetState();
}

class _QuestionWidgetState extends State<QuestionWidget> {
  List<int> selectedIds = [];

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.all(16),
      child: Padding(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              widget.question.intitule,
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 16),
            ...widget.question.reponsesPossibles.map((reponse) {
              return CheckboxListTile(
                title: Text(reponse.libelle),
                value: selectedIds.contains(reponse.id),
                onChanged: (bool? value) {
                  setState(() {
                    if (value == true) {
                      if (widget.question.type == 'QCU' ||
                          widget.question.type == 'VRAI_FAUX') {
                        // Une seule réponse possible
                        selectedIds = [reponse.id];
                      } else if (widget.question.type == 'QCM') {
                        // Plusieurs réponses possibles
                        selectedIds.add(reponse.id);
                      }
                    } else {
                      selectedIds.remove(reponse.id);
                    }
                    widget.onAnswerSelected(selectedIds);
                  });
                },
              );
            }).toList(),
          ],
        ),
      ),
    );
  }
}
```

### 4. Écran complet de quiz/exercice/challenge

```dart
class QuizScreen extends StatefulWidget {
  final int quizId;
  final int eleveId;
  final String type; // "quiz", "exercice", "challenge"

  const QuizScreen({
    Key? key,
    required this.quizId,
    required this.eleveId,
    required this.type,
  }) : super(key: key);

  @override
  _QuizScreenState createState() => _QuizScreenState();
}

class _QuizScreenState extends State<QuizScreen> {
  final QuestionService questionService = QuestionService(Dio());
  List<Question> questions = [];
  Map<int, List<int>> answers = {}; // questionId -> [reponseIds]
  bool isLoading = true;
  bool isSubmitting = false;
  SubmitResultResponse? result;

  @override
  void initState() {
    super.initState();
    loadQuestions();
  }

  Future<void> loadQuestions() async {
    try {
      List<Question> loadedQuestions;
      
      switch (widget.type) {
        case 'quiz':
          loadedQuestions = await questionService.getQuestionsByQuiz(widget.quizId);
          break;
        case 'exercice':
          loadedQuestions = await questionService.getQuestionsByExercice(widget.quizId);
          break;
        case 'challenge':
          loadedQuestions = await questionService.getQuestionsByChallenge(widget.quizId);
          break;
        default:
          throw Exception('Type invalide');
      }

      setState(() {
        // Le backend masque déjà estCorrecte pour les élèves, pas besoin de le faire ici
        questions = loadedQuestions;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erreur: $e')),
      );
    }
  }

  void onAnswerSelected(int questionId, List<int> selectedIds) {
    setState(() {
      answers[questionId] = selectedIds;
    });
  }

  Future<void> submitAnswers() async {
    if (answers.length != questions.length) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Veuillez répondre à toutes les questions')),
      );
      return;
    }

    setState(() {
      isSubmitting = true;
    });

    try {
      List<SubmitAnswer> submitAnswers = answers.entries.map((entry) {
        return SubmitAnswer(
          questionId: entry.key,
          reponseIds: entry.value,
        );
      }).toList();

      SubmitResultResponse response;
      
      switch (widget.type) {
        case 'quiz':
          response = await questionService.submitQuiz(
            widget.quizId,
            widget.eleveId,
            submitAnswers,
          );
          break;
        case 'exercice':
          response = await questionService.submitExercice(
            widget.quizId,
            widget.eleveId,
            submitAnswers,
          );
          break;
        case 'challenge':
          response = await questionService.submitChallenge(
            widget.quizId,
            widget.eleveId,
            submitAnswers,
          );
          break;
        default:
          throw Exception('Type invalide');
      }

      // Ajouter les points à l'élève
      await questionService.addPointsToEleve(widget.eleveId, response.score);

      setState(() {
        result = response;
        isSubmitting = false;
      });

      // Afficher les résultats
      showResultsDialog(response);
    } catch (e) {
      setState(() {
        isSubmitting = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erreur lors de la soumission: $e')),
      );
    }
  }

  void showResultsDialog(SubmitResultResponse result) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Résultats'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text('Score: ${result.score}/${result.totalPoints}'),
            Text('Pourcentage: ${result.percentage.toStringAsFixed(1)}%'),
            SizedBox(height: 16),
            Text('Détails:'),
            ...result.details.map((detail) {
              return ListTile(
                title: Text('Question ${detail.questionId}'),
                trailing: detail.correct
                    ? Icon(Icons.check, color: Colors.green)
                    : Icon(Icons.close, color: Colors.red),
                subtitle: Text('${detail.points} points'),
              );
            }).toList(),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pop();
              Navigator.of(context).pop(); // Retour à l'écran précédent
            },
            child: Text('OK'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('Chargement...')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (result != null) {
      return Scaffold(
        appBar: AppBar(title: Text('Résultats')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Score: ${result!.score}/${result!.totalPoints}',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              Text('Pourcentage: ${result!.percentage.toStringAsFixed(1)}%'),
            ],
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Text('Quiz'),
        actions: [
          if (isSubmitting)
            Center(
              child: Padding(
                padding: EdgeInsets.all(16),
                child: CircularProgressIndicator(),
              ),
            )
          else
            TextButton(
              onPressed: submitAnswers,
              child: Text(
                'Soumettre',
                style: TextStyle(color: Colors.white),
              ),
            ),
        ],
      ),
      body: ListView.builder(
        itemCount: questions.length,
        itemBuilder: (context, index) {
          final question = questions[index];
          return QuestionWidget(
            question: question,
            onAnswerSelected: (selectedIds) {
              onAnswerSelected(question.id, selectedIds);
            },
          );
        },
      ),
    );
  }
}
```

---

## 📝 Exemples d'Utilisation

### Scénario 1 : Passer un Quiz

```dart
// 1. Naviguer vers l'écran du quiz
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => QuizScreen(
      quizId: 5,
      eleveId: 7,
      type: 'quiz',
    ),
  ),
);

// 2. L'écran charge automatiquement les questions
// 3. L'élève répond aux questions
// 4. L'élève clique sur "Soumettre"
// 5. Les réponses sont envoyées au backend
// 6. Le score est calculé et affiché
// 7. Les points sont ajoutés au total de l'élève
```

### Scénario 2 : Vérifier le total de points

```dart
final questionService = QuestionService(Dio());
final totalPoints = await questionService.getElevePoints(7);
print('Total de points: $totalPoints');
```

---

## ⚠️ Points Importants à Retenir

### 1. **Sécurité des Réponses**
- Le champ `estCorrecte` est **automatiquement masqué (`null`)** par le backend pour les élèves
- Seuls les **admins** voient les réponses correctes
- **Aucune action requise côté Flutter** - la sécurité est gérée par le backend

### 2. **Validation des Réponses**
- Pour **QCU/VRAI_FAUX** : Envoyez exactement 1 ID dans `reponseIds`
- Pour **QCM** : Envoyez tous les IDs sélectionnés dans `reponseIds`
- Toutes les questions doivent avoir une réponse avant soumission

### 3. **Calcul des Points**
- Les points sont **ajoutés uniquement** si la réponse est correcte
- Le score est la somme des points des questions correctes
- Actuellement, **vous devez appeler manuellement** l'endpoint d'ajout de points après la soumission

### 4. **Endpoints Protégés**
- ✅ Les endpoints de récupération des questions sont **maintenant accessibles aux élèves**
- Les réponses correctes sont **automatiquement masquées** pour les élèves par le backend
- Seuls les **admins** voient les réponses correctes

---

## 📊 Processus Complet : Du Quiz au Total de Points

### Flux complet étape par étape :

#### 1. Récupérer les questions d'un quiz/exercice/challenge
```dart
final questions = await questionService.getQuestionsByQuiz(quizId);
// Pour les élèves : estCorrecte sera null dans toutes les réponses
// Pour les admins : estCorrecte sera true/false selon la réponse
```

#### 2. Afficher les questions à l'élève
```dart
// L'élève sélectionne ses réponses
answers[questionId] = [reponseId1, reponseId2]; // Pour QCM
// ou
answers[questionId] = [reponseId]; // Pour QCU/VRAI_FAUX
```

#### 3. Soumettre les réponses
```dart
final result = await questionService.submitQuiz(quizId, eleveId, reponses);
// result.score = points gagnés (ex: 25)
// result.totalPoints = points totaux possibles (ex: 50)
// result.details = détails question par question
```

#### 4. Ajouter les points à l'élève ⚠️ IMPORTANT
```dart
// ⚠️ Cette étape est OBLIGATOIRE car le backend ne le fait pas automatiquement
await questionService.addPointsToEleve(eleveId, result.score);
```

#### 5. Vérifier le nouveau total de points
```dart
final newTotal = await questionService.getElevePoints(eleveId);
// newTotal = total de points accumulés (ex: 175)
```

#### 6. Afficher les résultats
```dart
showDialog(
  context: context,
  builder: (context) => AlertDialog(
    title: Text('Résultats'),
    content: Column(
      children: [
        Text('Score: ${result.score}/${result.totalPoints}'),
        Text('Points gagnés: ${result.score}'),
        Text('Total de points: $newTotal'),
        // Afficher les détails question par question
      ],
    ),
  ),
);
```

---

## 📋 Récapitulatif des Endpoints

### Pour récupérer les questions :
- `GET /api/questions/by-quiz/{quizId}` - Questions d'un quiz
- `GET /api/questions/by-exercices/{exerciceId}` - Questions d'un exercice
- `GET /api/questions/by-challenges/{challengeId}` - Questions d'un challenge
- `GET /api/questions/by-defis/{defiId}` - Questions d'un défi

**Authentification** : Requise (Bearer Token)
**Rôle** : ELEVE ou ADMIN
**Sécurité** : `estCorrecte` est masqué (`null`) pour les élèves

### Pour soumettre les réponses :
- `POST /api/quizzes/{quizId}/submit` - Soumettre un quiz
- `POST /api/exercices/{exerciceId}/submit` - Soumettre un exercice
- `POST /api/challenges/{challengeId}/submit` - Soumettre un challenge

**Authentification** : Requise (Bearer Token)
**Rôle** : ELEVE uniquement

### Pour gérer les points :
- `POST /api/eleve/ajouter-points/{eleveId}` - Ajouter des points (⚠️ À appeler après soumission)
- `GET /api/eleve/points/{id}` - Récupérer le total de points

**Authentification** : Requise (Bearer Token)
**Rôle** : ELEVE uniquement

---

## 🔧 Améliorations Recommandées (Backend)

1. ✅ **Ouvrir les endpoints** de questions aux élèves - **FAIT**
2. ✅ **Masquer `estCorrecte`** dans la réponse API pour les élèves - **FAIT**
3. ⚠️ **Ajouter automatiquement les points** après soumission (actuellement manuel)
4. **Créer une table de résultats** pour l'historique des soumissions

---

## 🎯 Exemple Complet Flutter (Code Simplifié)

```dart
class QuizFlow {
  final QuestionService questionService = QuestionService(Dio());
  
  Future<void> completeQuiz(int quizId, int eleveId) async {
    try {
      // 1. Charger les questions
      final questions = await questionService.getQuestionsByQuiz(quizId);
      
      // 2. L'utilisateur répond aux questions (dans votre UI)
      Map<int, List<int>> answers = {
        1: [10],           // Question 1 : Réponse QCU
        2: [15, 16, 17],   // Question 2 : Réponses QCM
        3: [20],           // Question 3 : Réponse VRAI_FAUX
      };
      
      // 3. Préparer les réponses pour la soumission
      List<SubmitAnswer> submitAnswers = answers.entries.map((entry) {
        return SubmitAnswer(
          questionId: entry.key,
          reponseIds: entry.value,
        );
      }).toList();
      
      // 4. Soumettre les réponses
      final result = await questionService.submitQuiz(
        quizId,
        eleveId,
        submitAnswers,
      );
      
      // 5. Ajouter les points à l'élève
      await questionService.addPointsToEleve(eleveId, result.score);
      
      // 6. Récupérer le nouveau total
      final newTotal = await questionService.getElevePoints(eleveId);
      
      // 7. Afficher les résultats
      print('Score: ${result.score}/${result.totalPoints}');
      print('Points gagnés: ${result.score}');
      print('Total de points: $newTotal');
      
      // Afficher les détails question par question
      for (var detail in result.details) {
        print('Question ${detail.questionId}: ${detail.correct ? "✓" : "✗"} (${detail.points} pts)');
      }
      
    } catch (e) {
      print('Erreur: $e');
    }
  }
}
```

---

Cette documentation vous donne toutes les informations nécessaires pour intégrer le système de questions dans votre application Flutter ! 🚀

**Points clés à retenir** :
1. ✅ Les questions sont accessibles aux élèves (le backend masque les réponses correctes)
2. ✅ Les réponses sont soumises via `/api/{quizzes|exercices|challenges}/{id}/submit`
3. ⚠️ **IMPORTANT** : Vous devez appeler `addPointsToEleve()` après chaque soumission réussie
4. ✅ Les points totaux sont récupérables via `GET /api/eleve/points/{id}`

