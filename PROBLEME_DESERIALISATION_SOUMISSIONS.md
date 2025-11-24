# Problème de Désérialisation des Soumissions

## 🔴 Problème Identifié

Flutter (`built_value`) ne peut pas désérialiser la liste `details` dans `SubmitResultResponse` car il ne peut pas déterminer automatiquement le type des éléments de la liste.

**Erreur Flutter :**
```
Invalid argument(s): Unknown type on deserialization. Need either specifiedType or discriminator field.
```

## ✅ Structure JSON Actuelle (Backend)

Le backend renvoie correctement :
```json
{
  "ownerId": 12,
  "ownerType": "QUIZ",
  "eleveId": 7,
  "score": 20,
  "totalPoints": 20,
  "details": [
    {
      "questionId": 22,
      "points": 20,
      "correct": true
    }
  ]
}
```

## 🔧 Modifications Backend Effectuées

### 1. DTOs Créés/Modifiés

#### `SubmitResultResponse.java`
- ✅ Utilise `Integer` au lieu de `int` pour `score` et `totalPoints`
- ✅ Utilise `List<SubmitResultDetail>` pour `details`
- ✅ Annotations `@JsonProperty` sur tous les champs
- ✅ Initialisation par défaut de la liste : `new ArrayList<>()`

#### `SubmitResultDetail.java`
- ✅ Classe séparée (plus de classe interne)
- ✅ Utilise `Integer` et `Boolean` (types wrapper)
- ✅ Annotations `@JsonProperty` sur tous les champs
- ✅ Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)

### 2. Service `ServiceEvaluation.java`
- ✅ Toutes les méthodes utilisent `SubmitResultDetail` au lieu de `SubmitResultResponse.Detail`
- ✅ Les méthodes `submitQuiz`, `submitChallenge`, `submitExercice`, `submitDefi` sont toutes mises à jour

## 🎯 Solution Requise Côté Flutter

Le problème est que `built_value` en Flutter nécessite un **type explicite** pour désérialiser les listes génériques.

### Solution 1 : Spécifier le type explicitement lors de la désérialisation

Dans le code Flutter (`submission_service.dart`), lors de la désérialisation de `SubmitResultResponse`, il faut spécifier explicitement le type de la liste `details` :

```dart
// Au lieu de :
final response = serializers.deserializeWith(
  SubmitResultResponse.serializer,
  json,
);

// Utiliser :
final response = serializers.deserializeWith(
  SubmitResultResponse.serializer,
  json,
  specifiedType: const FullType(SubmitResultResponse, [
    FullType(SubmitResultDetail),
  ]),
);
```

### Solution 2 : Utiliser une désérialisation manuelle

Si `built_value` ne peut pas gérer cela automatiquement, désérialiser manuellement :

```dart
SubmitResultResponse _deserializeSubmitResultResponse(Map<String, dynamic> json) {
  return SubmitResultResponse((b) => b
    ..ownerId = json['ownerId'] as int?
    ..ownerType = json['ownerType'] as String?
    ..eleveId = json['eleveId'] as int?
    ..score = json['score'] as int?
    ..totalPoints = json['totalPoints'] as int?
    ..details = (json['details'] as List<dynamic>?)
        ?.map((e) => SubmitResultDetail.fromJson(e as Map<String, dynamic>))
        .toList()
        .cast<SubmitResultDetail>()
  );
}
```

### Solution 3 : Modifier le modèle `built_value` Flutter

Dans le fichier `.dart` qui définit `SubmitResultResponse`, s'assurer que la liste `details` est correctement typée :

```dart
@BuiltValueField(wireName: 'details')
BuiltList<SubmitResultDetail> get details;
```

Et s'assurer que `SubmitResultDetail` est correctement défini avec `@BuiltValue` :

```dart
@BuiltValue()
abstract class SubmitResultDetail implements Built<SubmitResultDetail, SubmitResultDetailBuilder> {
  @BuiltValueField(wireName: 'questionId')
  int? get questionId;
  
  @BuiltValueField(wireName: 'points')
  int? get points;
  
  @BuiltValueField(wireName: 'correct')
  bool? get correct;
  
  SubmitResultDetail._();
  factory SubmitResultDetail([void Function(SubmitResultDetailBuilder) updates]) = _$SubmitResultDetail;
  static Serializer<SubmitResultDetail> get serializer => _$submitResultDetailSerializer;
}
```

## 📋 Vérifications Backend

✅ **Tous les DTOs ont des annotations `@JsonProperty`**
✅ **Tous les champs utilisent des types wrapper (`Integer`, `Boolean`)**
✅ **La structure JSON est correcte et cohérente**
✅ **Les services utilisent correctement les nouveaux DTOs**

## 🔍 Test de la Sérialisation Backend

Pour vérifier que le backend sérialise correctement, vous pouvez tester avec :

```bash
curl -X POST http://localhost:8080/api/api/exercices/2/submit \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "eleveId": 7,
    "reponses": [
      {
        "questionId": 16,
        "reponseIds": [38]
      }
    ]
  }'
```

Le JSON retourné devrait être exactement comme montré ci-dessus.

## ⚠️ Note Importante

Le problème **n'est PAS** côté backend. Le backend sérialise correctement le JSON. Le problème est que Flutter `built_value` nécessite un type explicite pour désérialiser les listes génériques, et il ne peut pas le déterminer automatiquement à partir du JSON seul.

La solution doit être implémentée **côté Flutter** en spécifiant explicitement le type lors de la désérialisation ou en utilisant une désérialisation manuelle.

