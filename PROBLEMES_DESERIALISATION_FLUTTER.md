# 🔧 Problèmes de Désérialisation et Solutions - Backend & Flutter

## 📋 Problèmes Identifiés dans les Logs Flutter

### ⚠️ Problème Principal : Double `/api/api` dans les URLs

**Observation** : Toutes les URLs dans les logs contiennent `/api/api/...` au lieu de `/api/...`

**Exemples** :
- `GET http://192.168.1.12:8080/api/api/questions/by-defis/1` ❌
- `POST http://192.168.1.12:8080/api/api/defis/participer/7/1` ❌
- `POST http://192.168.1.12:8080/api/api/eleve/exercices/soumettre/7/2` ❌

**Solution** : Corriger la configuration de base URL dans Flutter pour ne pas dupliquer `/api`

---

## 📋 Problèmes Identifiés

### 1. ❌ Erreur 403 lors de la participation aux défis

**Erreur** : `POST /api/api/defis/participer/7/1` retourne 403 Forbidden

**Cause** : 
- Le frontend appelle `/api/api/defis/participer/7/1` (double `/api/api`)
- L'endpoint correct est `/api/eleve/defis/participer/{eleveId}/{defiId}` ou `/api/defis/participer/{eleveId}/{defiId}`

**Solution** : Corriger l'URL dans le frontend Flutter pour utiliser `/api/eleve/defis/participer/7/1` ou `/api/defis/participer/7/1`

---

### 2. ❌ 0 questions retournées pour challenges et défis

**Erreur** : `[QuestionService] Received 0 questions for challenge 15` et `[QuestionService] Received 0 questions for defi 1`

**Causes possibles** :
1. Les questions ne sont pas associées aux challenges/défis dans la base de données
2. Les méthodes `findByChallengeId` et `findByDefiId` ne fonctionnent pas correctement

**Vérifications à faire** :
- Vérifier dans la base de données que les questions ont bien `challenge_id` ou `defi_id` rempli
- Vérifier que les endpoints `/api/questions/by-challenges/{challengeId}` et `/api/questions/by-defis/{defiId}` fonctionnent

**Solution** : S'assurer que les questions sont bien créées avec `challengeId` ou `defiId` lors de leur création

---

### 3. ❌ Erreur de désérialisation lors de la soumission d'exercices

**Erreur** : `Invalid argument(s): Unknown type on deserialization. Need either specifiedType or discriminator field.`

**Cause** : Le frontend envoie :
```json
{
  "eleveId": 7,
  "reponses": [
    {
      "questionId": 16,
      "reponseIds": [38]
    }
  ]
}
```

Mais l'endpoint `/api/eleve/exercices/soumettre/{eleveId}/{exerciceId}` attend :
```json
{
  "reponse": "string"
}
```

**Solution** : Le frontend doit utiliser l'endpoint correct selon le type d'exercice :

#### Pour exercices avec questions (QCU/QCM/VRAI_FAUX) :
```
POST /api/exercices/{exerciceId}/submit
Body: SubmitRequest {
  "eleveId": 7,
  "reponses": [
    {
      "questionId": 16,
      "reponseIds": [38]
    }
  ]
}
Response: SubmitResultResponse
```

#### Pour exercices texte libre :
```
POST /api/eleve/exercices/soumettre/{eleveId}/{exerciceId}
Body: ExerciceSubmissionRequest {
  "reponse": "texte libre"
}
Response: FaireExerciceResponse
```

---

### 4. ❌ Erreur BuiltList pour l'historique des exercices

**Erreur** : `Deserializing to 'BuiltList<FaireExerciceResponse>' failed due to: Bad state: No builder factory for BuiltList<FaireExerciceResponse>`

**Cause** : Problème de configuration de la désérialisation Flutter avec `built_value`

**Solution côté Flutter** :
1. S'assurer que `FaireExerciceResponse` est correctement configuré avec `built_value`
2. Ou utiliser une `List<FaireExerciceResponse>` standard au lieu de `BuiltList`

**Exemple Flutter** :
```dart
// Au lieu de BuiltList
final List<FaireExerciceResponse> historique = 
    (response.data as List)
        .map((json) => FaireExerciceResponse.fromJson(json))
        .toList();
```

---

### 5. ❌ Format de réponse incorrect pour la progression des livres

**Erreur** : `Unexpected response format for book progress:`

**Cause** : L'endpoint `/api/livres/progression/{eleveId}/{livreId}` peut retourner `null` si aucune progression n'existe

**Solution** : Gérer le cas `null` dans le frontend :

```dart
try {
  final response = await dio.get('/api/livres/progression/$eleveId/$livreId');
  if (response.statusCode == 200) {
    if (response.data == null) {
      // Aucune progression pour ce livre
      return null;
    }
    return ProgressionResponse.fromJson(response.data);
  }
} catch (e) {
  // Gérer l'erreur
}
```

---

## 🔍 Endpoints Corrects

### Participation aux défis
- ✅ `POST /api/eleve/defis/participer/{eleveId}/{defiId}`
- ✅ `POST /api/defis/participer/{eleveId}/{defiId}`

### Questions
- ✅ `GET /api/questions/by-challenges/{challengeId}`
- ✅ `GET /api/questions/by-defis/{defiId}`
- ✅ `GET /api/questions/by-exercices/{exerciceId}`

### Soumission d'exercices

#### Avec questions (QCU/QCM/VRAI_FAUX)
- ✅ `POST /api/exercices/{exerciceId}/submit`
- Body: `SubmitRequest`
- Response: `SubmitResultResponse`

#### Texte libre
- ✅ `POST /api/eleve/exercices/soumettre/{eleveId}/{exerciceId}`
- Body: `ExerciceSubmissionRequest { "reponse": "string" }`
- Response: `FaireExerciceResponse`

### Progression des livres
- ✅ `GET /api/livres/progression/{eleveId}/{livreId}`
- Response: `ProgressionResponse` ou `null`

---

## 📝 Checklist de Correction

- [ ] Corriger l'URL de participation aux défis (enlever le double `/api/api`)
- [ ] Vérifier que les questions sont bien associées aux challenges/défis dans la DB
- [ ] Utiliser le bon endpoint pour la soumission d'exercices selon le type
- [ ] Corriger la désérialisation BuiltList dans Flutter
- [ ] Gérer le cas `null` pour la progression des livres

