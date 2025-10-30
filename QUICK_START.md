# 🚀 Démarrage Rapide - API EDUGO

## ⚡ En 3 Étapes

### 1️⃣ Préparer la Base de Données

```sql
-- Se connecter à MySQL
mysql -u root -p

-- Créer la base de données
CREATE DATABASE edugodatabase;

-- Vérifier
SHOW DATABASES;
```

### 2️⃣ Lancer l'Application

```bash
# Dans le dossier du projet
cd C:\Users\PC\Documents\ApiEdugo\edugo

# Lancer l'application
mvn spring-boot:run
```

**Attendre le message :**
```
Started EdugoApplication in X.XXX seconds
Administrateur par défaut créé : admin@edugo.com / admin123
```

### 3️⃣ Accéder à Swagger

Ouvrir votre navigateur et aller sur :

**http://localhost:8080/swagger-ui.html**

---

## 🔐 Se Connecter dans Swagger

1. **Scroller jusqu'à la section "Authentification"**
2. **Cliquer sur** `POST /api/auth/login`
3. **Cliquer sur "Try it out"**
4. **Remplir avec :**
   ```json
   {
     "email": "admin@edugo.com",
     "motDePasse": "admin123"
   }
   ```
5. **Cliquer sur "Execute"**
6. **Copier le token** de la réponse
7. **Cliquer sur le bouton "Authorize"** (en haut à droite)
8. **Entrer** : `Bearer VOTRE_TOKEN_COPIE`
9. **Cliquer sur "Authorize"**
10. **C'est fait !** Vous pouvez maintenant tester tous les endpoints

---

## 📝 Premier Test

### Créer un Niveau

1. Dans Swagger, trouver **GET /api/admin/niveaux**
2. Cliquer sur "Try it out" puis "Execute"
3. Vous verrez la liste (actuellement vide)
4. Pour **créer** un niveau, trouver **POST /api/admin/niveaux**
5. Entrer dans le body :
   ```json
   {
     "nom": "CP1"
   }
   ```
6. Cliquer sur "Execute"
7. Le niveau est créé ! 🎉

---

## 🎯 Ce que vous pouvez faire maintenant

### Administration
- ✅ Créer des niveaux (CP1, CP2, 6ème, etc.)
- ✅ Créer des classes
- ✅ Créer des matières
- ✅ Ajouter des livres
- ✅ Créer des exercices
- ✅ Créer des défis
- ✅ Créer des challenges
- ✅ Gérer les badges

### Élèves
- ✅ Consulter les livres
- ✅ Voir les exercices
- ✅ Participer aux challenges
- ✅ Voir les notifications

---

## 🔍 URLs Utiles

| Description | URL |
|-------------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs | http://localhost:8080/v3/api-docs |
| Health Check | http://localhost:8080/actuator/health |
| Base API | http://localhost:8080/api |

---

## ⚠️ Dépannage

### L'application ne démarre pas

**Vérifier MySQL :**
```bash
# Vérifier que MySQL est démarré
mysql -u root -p
```

**Vérifier le port :**
- MySQL doit être sur le port **3306**
- L'application doit être sur le port **8080**

### Erreur de base de données

Vérifier dans `application.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/edugodatabase
spring.datasource.username=root
spring.datasource.password=
```

### Swagger ne s'affiche pas

Vérifier que l'application est bien démarrée. Regarder les logs pour des erreurs.

---

## 📚 Documentation Complète

- **Guide API** : `GUIDE_API.md`
- **Documentation Swagger** : `DOCUMENTATION_SWAGGER.md`
- **Résumé Implémentation** : `RESUME_IMPLEMENTATION.md`
- **Prêt à l'utilisation** : `READY_TO_USE.md`

---

**Tout est prêt ! Bon développement ! 🎉**

