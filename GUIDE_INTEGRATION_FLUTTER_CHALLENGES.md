# 🚀 Guide Complet d'Intégration Flutter : Participation aux Challenges

## 📋 Table des matières
1. [Architecture Recommandée](#architecture-recommandée)
2. [Modèles de Données](#modèles-de-données)
3. [Services API](#services-api)
4. [Écrans Flutter](#écrans-flutter)
5. [Exemple Complet](#exemple-complet)
6. [Gestion d'Erreurs](#gestion-derreurs)
7. [Meilleures Pratiques](#meilleures-pratiques)

---

## 🏗️ Architecture Recommandée

### Structure des dossiers Flutter recommandée :

```
lib/
├── models/
│   ├── challenge/
│   │   ├── challenge.dart
│   │   ├── participation.dart
│   │   ├── participation_detail.dart
│   │   └── leaderboard_entry.dart
│   └── question/
│       ├── question.dart
│       └── reponse_possible.dart
├── services/
│   ├── challenge_service.dart
│   ├── question_service.dart
│   └── auth_service.dart
├── screens/
│   ├── challenges/
│   │   ├── challenges_list_screen.dart
│   │   ├── challenge_detail_screen.dart
│   │   ├── challenge_participation_screen.dart
│   │   └── challenge_results_screen.dart
└── widgets/
    └── challenge/
        ├── question_widget.dart
        └── leaderboard_widget.dart
```

---

## 📦 Modèles de Données

### 1. Challenge Model

```dart
class Challenge {
  final int id;
  final String titre;
  final String? description;
  final DateTime dateDebut;
  final DateTime dateFin;
  final int points;
  final String? theme;
  final String? typeChallenge; // INTERCLASSE, INTERNIVEAU
  final int? niveauId;
  final int? classeId;

  Challenge({
    required this.id,
    required this.titre,
    this.description,
    required this.dateDebut,
    required this.dateFin,
    required this.points,
    this.theme,
    this.typeChallenge,
    this.niveauId,
    this.classeId,
  });

  factory Challenge.fromJson(Map<String, dynamic> json) {
    return Challenge(
      id: json['id'],
      titre: json['titre'],
      description: json['description'],
      dateDebut: DateTime.parse(json['dateDebut']),
      dateFin: DateTime.parse(json['dateFin']),
      points: json['points'] ?? 0,
      theme: json['theme'],
      typeChallenge: json['typeChallenge'],
      niveauId: json['niveauId'],
      classeId: json['classeId'],
    );
  }

  bool get isActive {
    final now = DateTime.now();
    return now.isAfter(dateDebut) && now.isBefore(dateFin);
  }

  bool get isUpcoming {
    final now = DateTime.now();
    return now.isBefore(dateDebut);
  }

  bool get isExpired {
    final now = DateTime.now();
    return now.isAfter(dateFin);
  }
}
```

### 2. Participation Model

```dart
class Participation {
  final int id;
  final int eleveId;
  final int challengeId;
  final int? score;
  final int? rang;
  final int tempsPasse;
  final String statut; // EN_COURS, TERMINE, VALIDE, DISQUALIFIE
  final DateTime dateParticipation;
  final bool aParticiper;
  final Badge? badge;

  Participation({
    required this.id,
    required this.eleveId,
    required this.challengeId,
    this.score,
    this.rang,
    required this.tempsPasse,
    required this.statut,
    required this.dateParticipation,
    required this.aParticiper,
    this.badge,
  });

  factory Participation.fromJson(Map<String, dynamic> json) {
    return Participation(
      id: json['id'],
      eleveId: json['eleve']['id'] ?? json['eleveId'],
      challengeId: json['challenge']['id'] ?? json['challengeId'],
      score: json['score'],
      rang: json['rang'],
      tempsPasse: json['tempsPasse'] ?? 0,
      statut: json['statut'] ?? 'EN_COURS',
      dateParticipation: DateTime.parse(json['dateParticipation']),
      aParticiper: json['aParticiper'] ?? true,
      badge: json['badge'] != null ? Badge.fromJson(json['badge']) : null,
    );
  }

  bool get isEnCours => statut == 'EN_COURS';
  bool get isTermine => statut == 'TERMINE';
  bool get isValide => statut == 'VALIDE';
  bool get isDisqualifie => statut == 'DISQUALIFIE';
}

class Badge {
  final int id;
  final String nom;
  final String? description;
  final String? icone;

  Badge({
    required this.id,
    required this.nom,
    this.description,
    this.icone,
  });

  factory Badge.fromJson(Map<String, dynamic> json) {
    return Badge(
      id: json['id'],
      nom: json['nom'],
      description: json['description'],
      icone: json['icone'],
    );
  }
}
```

### 3. ParticipationDetailResponse Model

```dart
class ParticipationDetailResponse {
  final int id;
  final int eleveId;
  final String eleveNom;
  final String elevePrenom;
  final int challengeId;
  final String challengeTitre;
  final int? score;
  final int totalPoints;
  final int? rang;
  final int tempsPasse;
  final String statut;
  final DateTime dateParticipation;
  final int? badgeId;
  final String? badgeNom;
  final String? badgeIcone;
  final double? pourcentageReussite;
  final int? pointsGagnes;

  ParticipationDetailResponse({
    required this.id,
    required this.eleveId,
    required this.eleveNom,
    required this.elevePrenom,
    required this.challengeId,
    required this.challengeTitre,
    this.score,
    required this.totalPoints,
    this.rang,
    required this.tempsPasse,
    required this.statut,
    required this.dateParticipation,
    this.badgeId,
    this.badgeNom,
    this.badgeIcone,
    this.pourcentageReussite,
    this.pointsGagnes,
  });

  factory ParticipationDetailResponse.fromJson(Map<String, dynamic> json) {
    return ParticipationDetailResponse(
      id: json['id'],
      eleveId: json['eleveId'],
      eleveNom: json['eleveNom'],
      elevePrenom: json['elevePrenom'],
      challengeId: json['challengeId'],
      challengeTitre: json['challengeTitre'],
      score: json['score'],
      totalPoints: json['totalPoints'],
      rang: json['rang'],
      tempsPasse: json['tempsPasse'],
      statut: json['statut'],
      dateParticipation: DateTime.parse(json['dateParticipation']),
      badgeId: json['badgeId'],
      badgeNom: json['badgeNom'],
      badgeIcone: json['badgeIcone'],
      pourcentageReussite: json['pourcentageReussite']?.toDouble(),
      pointsGagnes: json['pointsGagnes'],
    );
  }

  double get percentage {
    if (score == null || totalPoints == 0) return 0.0;
    return (score! / totalPoints) * 100;
  }

  String get rangDisplay {
    if (rang == null) return 'Non classé';
    if (rang == 1) return '1er 🥇';
    if (rang == 2) return '2ème 🥈';
    if (rang == 3) return '3ème 🥉';
    return '$rang ème';
  }
}
```

### 4. LeaderboardEntry Model

```dart
class LeaderboardEntry {
  final int eleveId;
  final String nom;
  final String prenom;
  final DateTime dateParticipation;
  final int points;

  LeaderboardEntry({
    required this.eleveId,
    required this.nom,
    required this.prenom,
    required this.dateParticipation,
    required this.points,
  });

  factory LeaderboardEntry.fromJson(Map<String, dynamic> json) {
    return LeaderboardEntry(
      eleveId: json['eleveId'],
      nom: json['nom'],
      prenom: json['prenom'],
      dateParticipation: DateTime.parse(json['dateParticipation']),
      points: json['points'] ?? 0,
    );
  }

  String get fullName => '$prenom $nom';
}
```

---

## 🔌 Services API

### ChallengeService

```dart
import 'package:dio/dio.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/challenge/challenge.dart';
import '../models/challenge/participation.dart';
import '../models/challenge/participation_detail.dart';
import '../models/challenge/leaderboard_entry.dart';

class ChallengeService {
  final String baseUrl = 'http://votre-ip:8080/api'; // Remplacez par votre IP
  final Dio dio;

  ChallengeService(this.dio);

  // Helper pour récupérer le token
  Future<String> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token') ?? '';
  }

  // Récupérer les challenges disponibles pour un élève
  Future<List<Challenge>> getChallengesDisponibles(int eleveId) async {
    try {
      final response = await dio.get(
        '$baseUrl/challenges/disponibles/$eleveId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Challenge.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération des challenges');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Participer à un challenge
  Future<Participation> participerChallenge(int eleveId, int challengeId) async {
    try {
      final response = await dio.post(
        '$baseUrl/challenges/participer/$eleveId/$challengeId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
            'Content-Type': 'application/json',
          },
        ),
      );

      if (response.statusCode == 200) {
        return Participation.fromJson(response.data);
      }
      throw Exception('Erreur lors de la participation');
    } catch (e) {
      if (e.toString().contains('déjà participé')) {
        throw Exception('Vous participez déjà à ce challenge');
      }
      if (e.toString().contains('pas actuellement disponible')) {
        throw Exception('Ce challenge n\'est pas actuellement disponible');
      }
      throw Exception('Erreur lors de la participation: $e');
    }
  }

  // Récupérer les détails d'une participation
  Future<ParticipationDetailResponse> getParticipationDetail(
    int eleveId,
    int challengeId,
  ) async {
    try {
      final response = await dio.get(
        '$baseUrl/challenges/participation/$eleveId/$challengeId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        return ParticipationDetailResponse.fromJson(response.data);
      }
      throw Exception('Erreur lors de la récupération des détails');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer les challenges participés par un élève
  Future<List<Participation>> getChallengesParticipes(int eleveId) async {
    try {
      final response = await dio.get(
        '$baseUrl/challenges/participes/$eleveId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Participation.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer le classement d'un challenge
  Future<List<LeaderboardEntry>> getLeaderboard(int challengeId) async {
    try {
      final response = await dio.get(
        '$baseUrl/challenges/$challengeId/leaderboard',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => LeaderboardEntry.fromJson(json)).toList();
      }
      throw Exception('Erreur lors de la récupération du classement');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }

  // Récupérer un challenge par ID
  Future<Challenge> getChallengeById(int challengeId) async {
    try {
      final response = await dio.get(
        '$baseUrl/challenges/$challengeId',
        options: Options(
          headers: {
            'Authorization': 'Bearer ${await getToken()}',
          },
        ),
      );

      if (response.statusCode == 200) {
        return Challenge.fromJson(response.data);
      }
      throw Exception('Erreur lors de la récupération du challenge');
    } catch (e) {
      throw Exception('Erreur: $e');
    }
  }
}
```

### QuestionService (Extrait pour challenges)

```dart
class QuestionService {
  final String baseUrl = 'http://votre-ip:8080/api';
  final Dio dio;

  QuestionService(this.dio);

  Future<String> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token') ?? '';
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

  // Soumettre les réponses d'un challenge
  Future<SubmitResultResponse> submitChallenge(
    int challengeId,
    int eleveId,
    List<SubmitAnswer> reponses, {
    int? tempsPasse,
  }) async {
    try {
      final request = SubmitRequest(
        eleveId: eleveId,
        reponses: reponses,
        tempsPasse: tempsPasse,
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
      if (e.toString().contains('d\'abord participer')) {
        throw Exception('Vous devez d\'abord participer à ce challenge');
      }
      if (e.toString().contains('plus disponible')) {
        throw Exception('Ce challenge n\'est plus disponible');
      }
      throw Exception('Erreur lors de la soumission: $e');
    }
  }
}
```

---

## 📱 Écrans Flutter

### 1. Liste des Challenges Disponibles

```dart
class ChallengesListScreen extends StatefulWidget {
  final int eleveId;

  const ChallengesListScreen({Key? key, required this.eleveId}) : super(key: key);

  @override
  _ChallengesListScreenState createState() => _ChallengesListScreenState();
}

class _ChallengesListScreenState extends State<ChallengesListScreen> {
  final ChallengeService challengeService = ChallengeService(Dio());
  List<Challenge> challenges = [];
  bool isLoading = true;
  String? error;

  @override
  void initState() {
    super.initState();
    loadChallenges();
  }

  Future<void> loadChallenges() async {
    try {
      final loadedChallenges = await challengeService.getChallengesDisponibles(widget.eleveId);
      setState(() {
        challenges = loadedChallenges;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        error = e.toString();
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('Challenges')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: Text('Challenges')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Erreur: $error'),
              ElevatedButton(
                onPressed: loadChallenges,
                child: Text('Réessayer'),
              ),
            ],
          ),
        ),
      );
    }

    if (challenges.isEmpty) {
      return Scaffold(
        appBar: AppBar(title: Text('Challenges')),
        body: Center(
          child: Text('Aucun challenge disponible pour le moment'),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text('Challenges Disponibles')),
      body: ListView.builder(
        itemCount: challenges.length,
        itemBuilder: (context, index) {
          final challenge = challenges[index];
          return Card(
            margin: EdgeInsets.all(8),
            child: ListTile(
              title: Text(challenge.titre),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (challenge.description != null)
                    Text(challenge.description!),
                  SizedBox(height: 4),
                  Text('Points: ${challenge.points}'),
                  Text('Date: ${_formatDate(challenge.dateDebut)} - ${_formatDate(challenge.dateFin)}'),
                  _buildStatusBadge(challenge),
                ],
              ),
              trailing: Icon(Icons.arrow_forward_ios),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => ChallengeDetailScreen(
                      challenge: challenge,
                      eleveId: widget.eleveId,
                    ),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }

  Widget _buildStatusBadge(Challenge challenge) {
    if (challenge.isActive) {
      return Chip(
        label: Text('Actif', style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.green,
        avatar: Icon(Icons.check_circle, color: Colors.white, size: 18),
      );
    } else if (challenge.isUpcoming) {
      return Chip(
        label: Text('À venir', style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.blue,
        avatar: Icon(Icons.schedule, color: Colors.white, size: 18),
      );
    } else {
      return Chip(
        label: Text('Terminé', style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.grey,
        avatar: Icon(Icons.cancel, color: Colors.white, size: 18),
      );
    }
  }

  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }
}
```

### 2. Détails d'un Challenge

```dart
class ChallengeDetailScreen extends StatefulWidget {
  final Challenge challenge;
  final int eleveId;

  const ChallengeDetailScreen({
    Key? key,
    required this.challenge,
    required this.eleveId,
  }) : super(key: key);

  @override
  _ChallengeDetailScreenState createState() => _ChallengeDetailScreenState();
}

class _ChallengeDetailScreenState extends State<ChallengeDetailScreen> {
  final ChallengeService challengeService = ChallengeService(Dio());
  ParticipationDetailResponse? participation;
  bool isLoading = true;
  bool isParticipating = false;

  @override
  void initState() {
    super.initState();
    checkParticipation();
  }

  Future<void> checkParticipation() async {
    try {
      final detail = await challengeService.getParticipationDetail(
        widget.eleveId,
        widget.challenge.id,
      );
      setState(() {
        participation = detail;
        isLoading = false;
      });
    } catch (e) {
      // Pas encore de participation
      setState(() {
        participation = null;
        isLoading = false;
      });
    }
  }

  Future<void> handleParticipate() async {
    try {
      setState(() {
        isParticipating = true;
      });

      await challengeService.participerChallenge(
        widget.eleveId,
        widget.challenge.id,
      );

      // Recharger les détails
      await checkParticipation();

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Participation réussie !')),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erreur: $e')),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          isParticipating = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('Challenge')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text(widget.challenge.titre)),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Informations du challenge
            Card(
              child: Padding(
                padding: EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      widget.challenge.titre,
                      style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    SizedBox(height: 8),
                    if (widget.challenge.description != null)
                      Text(widget.challenge.description!),
                    SizedBox(height: 16),
                    Row(
                      children: [
                        Icon(Icons.stars, color: Colors.amber),
                        SizedBox(width: 8),
                        Text('${widget.challenge.points} points'),
                      ],
                    ),
                    SizedBox(height: 8),
                    Row(
                      children: [
                        Icon(Icons.calendar_today),
                        SizedBox(width: 8),
                        Text('Du ${_formatDate(widget.challenge.dateDebut)}'),
                      ],
                    ),
                    SizedBox(height: 8),
                    Row(
                      children: [
                        Icon(Icons.event),
                        SizedBox(width: 8),
                        Text('Au ${_formatDate(widget.challenge.dateFin)}'),
                      ],
                    ),
                  ],
                ),
              ),
            ),

            SizedBox(height: 16),

            // État de la participation
            if (participation == null)
              // Pas encore participé
              Card(
                color: Colors.blue.shade50,
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Text('Vous n\'avez pas encore participé à ce challenge'),
                      SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: widget.challenge.isActive
                            ? (isParticipating ? null : handleParticipate)
                            : null,
                        child: isParticipating
                            ? CircularProgressIndicator()
                            : Text('Participer au Challenge'),
                      ),
                    ],
                  ),
                ),
              )
            else if (participation!.isEnCours)
              // En cours
              Card(
                color: Colors.orange.shade50,
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Text('Vous participez à ce challenge'),
                      SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => ChallengeParticipationScreen(
                                challenge: widget.challenge,
                                eleveId: widget.eleveId,
                              ),
                            ),
                          );
                        },
                        child: Text('Commencer / Continuer'),
                      ),
                    ],
                  ),
                ),
              )
            else if (participation!.isTermine)
              // Terminé - Afficher les résultats
              Card(
                color: Colors.green.shade50,
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Challenge Terminé', style: TextStyle(fontWeight: FontWeight.bold)),
                      SizedBox(height: 8),
                      Text('Score: ${participation!.score}/${participation!.totalPoints}'),
                      if (participation!.rang != null)
                        Text('Rang: ${participation!.rangDisplay}'),
                      if (participation!.pointsGagnes != null)
                        Text('Points gagnés: ${participation!.pointsGagnes}'),
                      if (participation!.badgeNom != null) ...[
                        SizedBox(height: 8),
                        Row(
                          children: [
                            Icon(Icons.emoji_events, color: Colors.amber),
                            SizedBox(width: 8),
                            Text('Badge obtenu: ${participation!.badgeNom}'),
                          ],
                        ),
                      ],
                      SizedBox(height: 16),
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton(
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => ChallengeResultsScreen(
                                      participation: participation!,
                                      challengeId: widget.challenge.id,
                                    ),
                                  ),
                                );
                              },
                              child: Text('Voir les Détails'),
                            ),
                          ),
                          SizedBox(width: 8),
                          Expanded(
                            child: ElevatedButton(
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => ChallengeLeaderboardScreen(
                                      challengeId: widget.challenge.id,
                                    ),
                                  ),
                                );
                              },
                              child: Text('Classement'),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),

            SizedBox(height: 16),

            // Bouton pour voir le classement
            ElevatedButton.icon(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => ChallengeLeaderboardScreen(
                      challengeId: widget.challenge.id,
                    ),
                  ),
                );
              },
              icon: Icon(Icons.leaderboard),
              label: Text('Voir le Classement'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size(double.infinity, 50),
              ),
            ),
          ],
        ),
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }
}
```

### 3. Écran de Participation au Challenge

```dart
class ChallengeParticipationScreen extends StatefulWidget {
  final Challenge challenge;
  final int eleveId;

  const ChallengeParticipationScreen({
    Key? key,
    required this.challenge,
    required this.eleveId,
  }) : super(key: key);

  @override
  _ChallengeParticipationScreenState createState() => _ChallengeParticipationScreenState();
}

class _ChallengeParticipationScreenState extends State<ChallengeParticipationScreen> {
  final ChallengeService challengeService = ChallengeService(Dio());
  final QuestionService questionService = QuestionService(Dio());
  
  bool isLoading = true;
  bool isSubmitting = false;
  List<Question> questions = [];
  Map<int, List<int>> answers = {}; // questionId -> [reponseIds]
  DateTime? startTime;
  int elapsedSeconds = 0;
  Timer? timer;

  @override
  void initState() {
    super.initState();
    loadQuestions();
    startTimer();
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }

  void startTimer() {
    startTime = DateTime.now();
    timer = Timer.periodic(Duration(seconds: 1), (timer) {
      if (startTime != null) {
        setState(() {
          elapsedSeconds = DateTime.now().difference(startTime!).inSeconds;
        });
      }
    });
  }

  String get timerDisplay {
    final minutes = elapsedSeconds ~/ 60;
    final seconds = elapsedSeconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  Future<void> loadQuestions() async {
    try {
      final loadedQuestions = await questionService.getQuestionsByChallenge(widget.challenge.id);
      setState(() {
        questions = loadedQuestions;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        isLoading = false;
      });
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erreur lors du chargement: $e')),
        );
      }
    }
  }

  void onAnswerSelected(int questionId, List<int> selectedIds) {
    setState(() {
      answers[questionId] = selectedIds;
    });
  }

  Future<void> submitAnswers() async {
    if (answers.length != questions.length) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Veuillez répondre à toutes les questions')),
        );
      }
      return;
    }

    try {
      setState(() {
        isSubmitting = true;
      });

      timer?.cancel(); // Arrêter le chronomètre

      List<SubmitAnswer> submitAnswers = answers.entries.map((entry) {
        return SubmitAnswer(
          questionId: entry.key,
          reponseIds: entry.value,
        );
      }).toList();

      // Soumettre les réponses avec le temps passé
      final result = await questionService.submitChallenge(
        widget.challenge.id,
        widget.eleveId,
        submitAnswers,
        tempsPasse: elapsedSeconds,
      );

      // Récupérer les détails mis à jour de la participation
      final participationDetail = await challengeService.getParticipationDetail(
        widget.eleveId,
        widget.challenge.id,
      );

      if (mounted) {
        // Naviguer vers l'écran de résultats
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => ChallengeResultsScreen(
              participation: participationDetail,
              challengeId: widget.challenge.id,
              result: result,
            ),
          ),
        );
      }
    } catch (e) {
      setState(() {
        isSubmitting = false;
      });
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Erreur lors de la soumission: $e')),
        );
      }
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

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.challenge.titre),
        actions: [
          // Afficher le chronomètre
          Center(
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 16),
              child: Text(
                timerDisplay,
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                ),
              ),
            ),
          ),
        ],
      ),
      body: Column(
        children: [
          // Barre de progression
          LinearProgressIndicator(
            value: answers.length / questions.length,
            backgroundColor: Colors.grey.shade300,
          ),
          Padding(
            padding: EdgeInsets.all(8),
            child: Text(
              'Question ${answers.length}/${questions.length}',
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
          ),

          // Liste des questions
          Expanded(
            child: ListView.builder(
              padding: EdgeInsets.all(16),
              itemCount: questions.length,
              itemBuilder: (context, index) {
                final question = questions[index];
                return QuestionWidget(
                  question: question,
                  questionNumber: index + 1,
                  selectedIds: answers[question.id] ?? [],
                  onAnswerSelected: (selectedIds) {
                    onAnswerSelected(question.id, selectedIds);
                  },
                );
              },
            ),
          ),

          // Bouton de soumission
          Padding(
            padding: EdgeInsets.all(16),
            child: ElevatedButton(
              onPressed: (answers.length == questions.length && !isSubmitting)
                  ? submitAnswers
                  : null,
              style: ElevatedButton.styleFrom(
                minimumSize: Size(double.infinity, 50),
              ),
              child: isSubmitting
                  ? CircularProgressIndicator(color: Colors.white)
                  : Text('Soumettre les Réponses'),
            ),
          ),
        ],
      ),
    );
  }
}
```

### 4. Écran de Résultats

```dart
class ChallengeResultsScreen extends StatelessWidget {
  final ParticipationDetailResponse participation;
  final int challengeId;
  final SubmitResultResponse? result;

  const ChallengeResultsScreen({
    Key? key,
    required this.participation,
    required this.challengeId,
    this.result,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Résultats du Challenge')),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Score principal
            Card(
              color: Colors.blue.shade50,
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Column(
                  children: [
                    Text(
                      'Score',
                      style: TextStyle(fontSize: 18, color: Colors.grey),
                    ),
                    SizedBox(height: 8),
                    Text(
                      '${participation.score}/${participation.totalPoints}',
                      style: TextStyle(
                        fontSize: 48,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(height: 8),
                    if (participation.pourcentageReussite != null)
                      Text(
                        '${participation.pourcentageReussite!.toStringAsFixed(1)}%',
                        style: TextStyle(fontSize: 24, color: Colors.blue),
                      ),
                  ],
                ),
              ),
            ),

            SizedBox(height: 16),

            // Classement
            if (participation.rang != null)
              Card(
                child: ListTile(
                  leading: Icon(Icons.emoji_events, color: Colors.amber),
                  title: Text('Classement'),
                  trailing: Text(
                    participation.rangDisplay,
                    style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  ),
                ),
              ),

            SizedBox(height: 16),

            // Points gagnés
            if (participation.pointsGagnes != null)
              Card(
                child: ListTile(
                  leading: Icon(Icons.stars, color: Colors.amber),
                  title: Text('Points Gagnés'),
                  trailing: Text(
                    '${participation.pointsGagnes}',
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ),
              ),

            SizedBox(height: 16),

            // Badge obtenu
            if (participation.badgeNom != null)
              Card(
                color: Colors.amber.shade50,
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Row(
                    children: [
                      Icon(Icons.emoji_events, color: Colors.amber, size: 48),
                      SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'Badge Obtenu !',
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                            Text(participation.badgeNom!),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),

            SizedBox(height: 16),

            // Détails question par question
            if (result != null && result!.details.isNotEmpty) ...[
              Text(
                'Détails des Réponses',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              SizedBox(height: 8),
              ...result!.details.map((detail) {
                return Card(
                  child: ListTile(
                    title: Text('Question ${detail.questionId}'),
                    subtitle: Text('${detail.points} points'),
                    trailing: detail.correct
                        ? Icon(Icons.check_circle, color: Colors.green)
                        : Icon(Icons.cancel, color: Colors.red),
                    leading: CircleAvatar(
                      backgroundColor: detail.correct ? Colors.green : Colors.red,
                      child: Icon(
                        detail.correct ? Icons.check : Icons.close,
                        color: Colors.white,
                      ),
                    ),
                  ),
                );
              }).toList(),
            ],

            SizedBox(height: 16),

            // Actions
            Row(
              children: [
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ChallengeLeaderboardScreen(
                            challengeId: challengeId,
                          ),
                        ),
                      );
                    },
                    icon: Icon(Icons.leaderboard),
                    label: Text('Classement'),
                  ),
                ),
                SizedBox(width: 8),
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: () {
                      Navigator.popUntil(context, (route) => route.isFirst);
                    },
                    icon: Icon(Icons.home),
                    label: Text('Accueil'),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
```

### 5. Écran de Classement

```dart
class ChallengeLeaderboardScreen extends StatefulWidget {
  final int challengeId;

  const ChallengeLeaderboardScreen({Key? key, required this.challengeId}) : super(key: key);

  @override
  _ChallengeLeaderboardScreenState createState() => _ChallengeLeaderboardScreenState();
}

class _ChallengeLeaderboardScreenState extends State<ChallengeLeaderboardScreen> {
  final ChallengeService challengeService = ChallengeService(Dio());
  List<LeaderboardEntry> leaderboard = [];
  bool isLoading = true;
  String? error;

  @override
  void initState() {
    super.initState();
    loadLeaderboard();
  }

  Future<void> loadLeaderboard() async {
    try {
      final entries = await challengeService.getLeaderboard(widget.challengeId);
      setState(() {
        leaderboard = entries;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        error = e.toString();
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: Text('Classement')),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: Text('Classement')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Erreur: $error'),
              ElevatedButton(
                onPressed: loadLeaderboard,
                child: Text('Réessayer'),
              ),
            ],
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text('Classement')),
      body: leaderboard.isEmpty
          ? Center(child: Text('Aucun participant pour le moment'))
          : ListView.builder(
              itemCount: leaderboard.length,
              itemBuilder: (context, index) {
                final entry = leaderboard[index];
                final position = index + 1;
                
                return Card(
                  margin: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                  color: _getRankColor(position),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.white,
                      child: Text(
                        position.toString(),
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          color: _getRankColor(position),
                        ),
                      ),
                    ),
                    title: Text(entry.fullName),
                    subtitle: Text('${entry.points} points'),
                    trailing: _getRankIcon(position),
                  ),
                );
              },
            ),
    );
  }

  Color _getRankColor(int position) {
    if (position == 1) return Colors.amber.shade100;
    if (position == 2) return Colors.grey.shade300;
    if (position == 3) return Colors.orange.shade100;
    return Colors.white;
  }

  Widget? _getRankIcon(int position) {
    if (position == 1) return Icon(Icons.emoji_events, color: Colors.amber);
    if (position == 2) return Icon(Icons.emoji_events, color: Colors.grey);
    if (position == 3) return Icon(Icons.emoji_events, color: Colors.orange);
    return null;
  }
}
```

---

## 💡 Exemple Complet d'Utilisation

### Navigation entre les écrans

```dart
// 1. Liste des challenges
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => ChallengesListScreen(eleveId: 7),
  ),
);

// 2. Détails d'un challenge
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => ChallengeDetailScreen(
      challenge: challenge,
      eleveId: 7,
    ),
  ),
);

// 3. Participation au challenge (si non encore participé)
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => ChallengeParticipationScreen(
      challenge: challenge,
      eleveId: 7,
    ),
  ),
);

// 4. Résultats (après soumission)
Navigator.push(
  context,
  MaterialPageRoute(
    builder: (context) => ChallengeResultsScreen(
      participation: participationDetail,
      challengeId: challenge.id,
    ),
  ),
);
```

---

## ⚠️ Gestion d'Erreurs

### Erreurs communes et solutions

```dart
try {
  await challengeService.participerChallenge(eleveId, challengeId);
} catch (e) {
  String message = 'Erreur inconnue';
  
  if (e.toString().contains('déjà participé')) {
    message = 'Vous participez déjà à ce challenge';
    // Rediriger vers l'écran de participation
  } else if (e.toString().contains('pas actuellement disponible')) {
    message = 'Ce challenge n\'est pas actuellement disponible';
  } else if (e.toString().contains('401')) {
    message = 'Session expirée. Veuillez vous reconnecter';
    // Rediriger vers l'écran de connexion
  } else if (e.toString().contains('500')) {
    message = 'Erreur serveur. Veuillez réessayer plus tard';
  }
  
  ScaffoldMessenger.of(context).showSnackBar(
    SnackBar(content: Text(message)),
  );
}
```

---

## ✅ Meilleures Pratiques

### 1. **Gestion du temps**
- Utiliser un `Timer` pour compter le temps passé
- Envoyer le temps passé lors de la soumission
- Sauvegarder le temps même si l'utilisateur ferme l'app

### 2. **Sauvegarde locale**
- Sauvegarder les réponses localement (SharedPreferences ou Hive)
- Permettre de reprendre la participation si l'app se ferme

### 3. **Validation**
- Vérifier que toutes les questions ont une réponse avant soumission
- Afficher un indicateur de progression
- Confirmer avant soumission (pour éviter les soumissions accidentelles)

### 4. **Feedback utilisateur**
- Afficher un loader pendant le chargement
- Afficher les résultats immédiatement après soumission
- Animer les transitions entre les écrans

### 5. **Gestion d'état**
- Utiliser `setState()` pour mettre à jour l'UI
- Gérer les états de chargement (`isLoading`, `isSubmitting`)
- Gérer les erreurs avec messages clairs

---

Ce guide vous donne tous les éléments nécessaires pour intégrer la participation aux challenges dans votre application Flutter ! 🚀

