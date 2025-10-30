TESTING and Integration Guide

But: French content below — concise instructions for running unit and integration tests and coordinating with the Angular frontend.

1) Pré-requis
- Java 17 installé localement et utilisé par Maven/`mvnw`.
- Maven wrapper (`mvnw.cmd`) fonctionne (fournie dans le dépôt).
- Base de données (MySQL) configurée si les tests d'intégration en ont besoin — utilisez un profil de test ou une base H2 en mémoire si possible.

2) Commandes utiles (PowerShell)
# Build rapide sans tests
.
```powershell
# build et package rapide (tests sautés)
.\mvnw.cmd -DskipTests package
```

# Lancer les tests unitaires
```powershell
.\mvnw.cmd test
```

# Lancer les tests d'intégration (Failsafe)
# Les tests d'intégration doivent suivre la convention *IT.java
```powershell
.\mvnw.cmd verify
# ou pour lancer uniquement les integration-tests (si vous avez une configuration custom):
.\mvnw.cmd -DskipTests=false -DskipITs=false verify
```

3) Démarrer l'application backend (pour tests manuels ou intégration avec le frontend)
```powershell
# lance l'application (profile local par défaut)
.\mvnw.cmd spring-boot:run
```
- L'API écoute normalement sur le port configuré dans `src/main/resources/application.properties`.

4) Coordination avec le frontend Angular
- Dans le projet Angular, configurez `environment` pour pointer vers l'URL du backend (par ex. http://localhost:8080).
- Pour faciliter le développement local, vous pouvez utiliser un `proxy.conf.json` dans le projet Angular :

  {
    "/api": {
      "target": "http://localhost:8080",
      "secure": false,
      "changeOrigin": true
    }
  }

  puis lancer :
  ```powershell
  npm install
  ng serve --proxy-config proxy.conf.json
  ```

- Vérifiez aussi la configuration CORS côté backend si nécessaire (voir `SecurityConfig` et les filtres JWT). Activez les origines du frontend (http://localhost:4200) pendant la phase d'intégration locale.

5) Tests d'intégration end-to-end
- Workflow suggéré:
  1. Démarrer la base de données de test (ou utiliser un conteneur Docker).
  2. Lancer le backend : `mvnw spring-boot:run`.
  3. Lancer le frontend avec le proxy (ng serve).
  4. Exécuter tests e2e côté Angular (si présents) ou exécuter tests d'intégration backend (`mvn verify`).

6) CI / pipeline
- Assurez-vous que le runner CI utilise Java 17 (image JDK 17) et exécute `mvn -B -DskipTests=false verify`.
- Étapes recommandées: build -> unit tests -> integration tests (failsafe) -> package -> docker image (si besoin).

7) Bonnes pratiques
- Nommez vos tests d'intégration `*IT.java` pour que Failsafe les détecte.
- Gardez les tests unitaires rapides et isolés; évitez d'utiliser la base de données réelle.
- Si vous attendez un frontend Angular, ajoutez un `smoke` ou `contract` test simple côté backend pour vérifier endpoints clés.

8) Support / aide
- Dites-moi si vous voulez que je :
  - ajoute un profil Maven `integration-test` plus strict,
  - génère un exemple de `*IT.java` (test d'intégration de base),
  - ajuste `application.properties` pour un profil `test` utilisant H2.
