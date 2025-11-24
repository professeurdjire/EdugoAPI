# ✅ Optimisation des Performances : Comptage des Questions

## 🔍 Problème Identifié

**Symptôme** : Timeouts de connexion (30 secondes) sur plusieurs endpoints :
- `/api/api/eleves/7/quizzes` - timeout
- `/api/api/eleve/challenges/participes/7` - timeout
- `/api/api/defis/participes/7` - timeout
- `/api/api/livres/progression/7` - timeout
- `/api/api/eleve/exercices/historique/7` - timeout
- `/api/api/eleve/points/7` - timeout

**Cause** : Le comptage des questions pour chaque challenge/défi se faisait via `findByChallengeId().size()`, ce qui :
1. Chargeait **toutes les questions** avec leurs relations (JOIN FETCH)
2. Faisait une requête **par challenge/défi** (problème N+1)
3. Était très lent avec beaucoup de challenges/défis

---

## ✅ Optimisations Appliquées

### 1. Ajout de Méthodes COUNT Optimisées dans `QuestionRepository`

**Fichier** : `src/main/java/com/example/edugo/repository/QuestionRepository.java`

**Ajouts** :
```java
// Compter les questions par challenge (optimisé)
@Query("SELECT COUNT(q) FROM Question q WHERE q.challenge.id = :challengeId")
Long countByChallengeId(@Param("challengeId") Long challengeId);

// Compter les questions par défi (optimisé)
@Query("SELECT COUNT(q) FROM Question q WHERE q.defi.id = :defiId")
Long countByDefiId(@Param("defiId") Long defiId);

// Compter les questions par exercice (optimisé)
@Query("SELECT COUNT(q) FROM Question q WHERE q.exercice.id = :exerciceId")
Long countByExerciceId(@Param("exerciceId") Long exerciceId);

// Compter les questions pour plusieurs challenges en une seule requête (batch)
@Query("SELECT q.challenge.id, COUNT(q) FROM Question q WHERE q.challenge.id IN :challengeIds GROUP BY q.challenge.id")
List<Object[]> countByChallengeIds(@Param("challengeIds") List<Long> challengeIds);

// Compter les questions pour plusieurs défis en une seule requête (batch)
@Query("SELECT q.defi.id, COUNT(q) FROM Question q WHERE q.defi.id IN :defiIds GROUP BY q.defi.id")
List<Object[]> countByDefiIds(@Param("defiIds") List<Long> defiIds);
```

**Avantages** :
- ✅ Utilise `COUNT()` au lieu de charger toutes les questions
- ✅ Requêtes batch pour compter plusieurs challenges/défis en une seule fois
- ✅ Réduit drastiquement le nombre de requêtes SQL

---

### 2. Optimisation de `ServiceChallenge.toResponse()`

**Fichier** : `src/main/java/com/example/edugo/service/ServiceChallenge.java`

**Avant** :
```java
public ChallengeResponse toResponse(Challenge challenge) {
    // ...
    // ❌ Charge toutes les questions avec JOIN FETCH
    int nombreQuestions = questionRepository.findByChallengeId(challenge.getId()).size();
    res.setNombreQuestions(nombreQuestions);
    return res;
}
```

**Après** :
```java
public ChallengeResponse toResponse(Challenge challenge) {
    return toResponse(challenge, null);
}

public ChallengeResponse toResponse(Challenge challenge, Map<Long, Integer> questionCountsMap) {
    // ...
    // ✅ Utilise le map si fourni (batch), sinon COUNT direct
    if (questionCountsMap != null && questionCountsMap.containsKey(challenge.getId())) {
        res.setNombreQuestions(questionCountsMap.get(challenge.getId()));
    } else {
        Long count = questionRepository.countByChallengeId(challenge.getId());
        res.setNombreQuestions(count != null ? count.intValue() : 0);
    }
    return res;
}
```

---

### 3. Optimisation de `ServiceChallenge.getChallengesDisponibles()`

**Fichier** : `src/main/java/com/example/edugo/service/ServiceChallenge.java`

**Avant** :
```java
return challengesActifs.stream()
    .filter(...)
    .map(this::toResponse)  // ❌ Une requête par challenge
    .toList();
```

**Après** :
```java
// Filtrer d'abord
List<Challenge> challengesFiltres = challengesActifs.stream()
    .filter(...)
    .toList();

// ✅ Compter les questions pour tous les challenges en une seule requête
Map<Long, Integer> questionCountsMap = new HashMap<>();
if (!challengesFiltres.isEmpty()) {
    List<Long> challengeIds = challengesFiltres.stream().map(Challenge::getId).toList();
    List<Object[]> counts = questionRepository.countByChallengeIds(challengeIds);
    for (Object[] count : counts) {
        Long challengeId = (Long) count[0];
        Long countValue = (Long) count[1];
        questionCountsMap.put(challengeId, countValue.intValue());
    }
}

// Mapper avec les comptes pré-calculés
return challengesFiltres.stream()
    .map(challenge -> toResponse(challenge, questionCountsMap))
    .toList();
```

**Résultat** :
- ✅ **1 requête SQL** au lieu de N requêtes (N = nombre de challenges)
- ✅ **Réduction drastique** du temps de réponse

---

### 4. Optimisation de `ServiceDefi.toResponse()` et `getDefisDisponibles()`

**Fichier** : `src/main/java/com/example/edugo/service/ServiceDefi.java`

**Même principe** :
- Méthode `toResponse()` avec support du map de comptes
- `getDefisDisponibles()` et `getAllDefis()` utilisent le batch count

---

### 5. Optimisation de `AdminService.toChallengeResponse()`

**Fichier** : `src/main/java/com/example/edugo/service/AdminService.java`

**Avant** :
```java
int nombreQuestions = questionRepository.findByChallengeId(challenge.getId()).size();
```

**Après** :
```java
Long count = questionRepository.countByChallengeId(challenge.getId());
response.setNombreQuestions(count != null ? count.intValue() : 0);
```

---

## 📊 Impact des Optimisations

### Avant
- **N requêtes SQL** pour N challenges/défis
- Chaque requête charge **toutes les questions** avec JOIN FETCH
- **Temps de réponse** : 30+ secondes (timeout)

### Après
- **1 requête SQL** pour tous les challenges/défis (batch)
- Utilise `COUNT()` au lieu de charger les données
- **Temps de réponse** : < 1 seconde (attendu)

### Exemple avec 10 challenges

**Avant** :
- 10 requêtes `SELECT q FROM Question q LEFT JOIN FETCH ... WHERE q.challenge.id = ?`
- Chaque requête charge toutes les questions avec leurs relations
- **Total** : ~10-30 secondes

**Après** :
- 1 requête `SELECT q.challenge.id, COUNT(q) FROM Question q WHERE q.challenge.id IN (...) GROUP BY q.challenge.id`
- Ne charge que les IDs et les comptes
- **Total** : < 1 seconde

---

## 🔍 Endpoints Optimisés

- ✅ `GET /api/eleve/challenges/disponibles/{id}` - Utilise batch count
- ✅ `GET /api/challenges` - Utilise COUNT direct
- ✅ `GET /api/challenges/{id}` - Utilise COUNT direct
- ✅ `GET /api/admin/challenges` - Utilise COUNT direct
- ✅ `GET /api/defis/disponibles/{id}` - Utilise batch count
- ✅ `GET /api/defis` - Utilise batch count

---

## 📝 Notes Importantes

1. **Compatibilité** : Les méthodes `toResponse()` existantes continuent de fonctionner (surcharge avec map optionnel)

2. **Performance** : Le batch count est particulièrement efficace pour les listes, mais le COUNT direct reste rapide pour les objets individuels

3. **Évolutivité** : Cette approche peut gérer des centaines de challenges/défis sans problème de performance

4. **Autres Optimisations Possibles** :
   - Ajouter des index sur `question.challenge_id` et `question.defi_id`
   - Utiliser la pagination pour les grandes listes
   - Mettre en cache les comptes si les questions changent rarement

---

## ✅ Checklist

- [x] Ajout de méthodes COUNT dans `QuestionRepository`
- [x] Ajout de méthodes batch COUNT dans `QuestionRepository`
- [x] Optimisation de `ServiceChallenge.toResponse()`
- [x] Optimisation de `ServiceChallenge.getChallengesDisponibles()`
- [x] Optimisation de `ServiceDefi.toResponse()`
- [x] Optimisation de `ServiceDefi.getDefisDisponibles()`
- [x] Optimisation de `ServiceDefi.getAllDefis()`
- [x] Optimisation de `AdminService.toChallengeResponse()`

---

## 🚀 Prochaines Étapes

1. **Tester les endpoints** pour vérifier que les timeouts sont résolus
2. **Monitorer les performances** avec des outils de profiling
3. **Ajouter des index** sur les colonnes de clés étrangères si nécessaire
4. **Considérer la pagination** pour les grandes listes

