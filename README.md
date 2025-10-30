# 🎓 API EDUGO - Plateforme Éducative Malienne

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-success)

**API REST complète pour une plateforme éducative avec documentation interactive**

</div>

---

## 🚀 Démarrage Ultra-Rapide

```bash
# 1. Créer la base de données
mysql -u root -p -e "CREATE DATABASE edugodatabase;"

# 2. Lancer l'application
mvn spring-boot:run

# 3. Accéder à Swagger
# Ouvrir http://localhost:8080/swagger-ui.html

# 4. Se connecter avec :
# email: admin@edugo.com
# mot de passe: admin123
```

---

## 📋 Table des Matières

- [À Propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Documentation](#-documentation)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Structure du Projet](#-structure-du-projet)
- [Contribuer](#-contribuer)

---

## 📖 À Propos

EDUGO est une plateforme éducative complète permettant la gestion de ressources pédagogiques numériques pour les élèves du primaire et du secondaire au Mali.

### Caractéristiques Principales

- 📚 **Gestion de livres numériques** avec suivi de progression
- 📝 **Exercices et quiz interactifs**
- 🏆 **Système de gamification** (défis, challenges, badges)
- 📊 **Statistiques et analytics**
- 🔔 **Système de notifications**
- 👥 **Gestion des utilisateurs** (Admin et Élèves)

---

## ✨ Fonctionnalités

### Pour les Administrateurs
- ✅ Gestion complète de tous les modules (CRUD)
- ✅ Gestion des utilisateurs (création, modification, suppression)
- ✅ Gestion des structures scolaires (niveaux, classes, matières)
- ✅ Gestion des ressources éducatives (livres, exercices, quiz)
- ✅ Gestion des défis et challenges
- ✅ Gestion des badges et récompenses
- ✅ Consultation des statistiques

### Pour les Élèves
- ✅ Consultation des livres
- ✅ Réalisation d'exercices
- ✅ Participation aux défis et challenges
- ✅ Suivi de progression
- ✅ Gestion des objectifs
- ✅ Notifications et suggestions

---

## 🛠️ Technologies

| Catégorie | Technologies |
|-----------|-------------|
| **Framework** | Spring Boot 3.5.6 |
| **Sécurité** | Spring Security + JWT |
| **Base de Données** | MySQL 8.0 |
| **ORM** | JPA/Hibernate |
| **Documentation** | Swagger/OpenAPI 3 |
| **Validation** | Jakarta Validation |
| **Build** | Maven |
| **Language** | Java 17 |

---

## 📚 Documentation

### Accès à la Documentation

- 🌐 **Swagger UI** : http://localhost:8080/swagger-ui.html
- 📄 **API Docs JSON** : http://localhost:8080/v3/api-docs
- 🩺 **Health Check** : http://localhost:8080/actuator/health

### Documents Disponibles

- 📘 **[Guide de Démarrage Rapide](QUICK_START.md)** - Installation et premiers pas
- 📗 **[Guide API](GUIDE_API.md)** - Tous les endpoints disponibles
- 📙 **[Documentation Swagger](DOCUMENTATION_SWAGGER.md)** - Utilisation de l'interface Swagger
- 📕 **[Résumé Implémentation](RESUME_IMPLEMENTATION.md)** - Détails techniques
- 📓 **[Prêt à l'Utilisation](READY_TO_USE.md)** - Checklist et stats

---

## 🚀 Installation

### Prérequis

- ✅ Java 17 ou supérieur
- ✅ Maven 3.8+
- ✅ MySQL 8.0+
- ✅ Git (optionnel)

### Étapes d'Installation

1. **Cloner le projet** (ou télécharger)
   ```bash
   git clone <repository-url>
   cd edugo
   ```

2. **Configurer la base de données**
   ```bash
   mysql -u root -p -e "CREATE DATABASE edugodatabase;"
   ```

3. **Configurer les connexions** (optionnel)
   
   Éditer `src/main/resources/application.properties` si nécessaire :
   ```properties
   spring.datasource.username=votre_username
   spring.datasource.password=votre_password
   ```

4. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

5. **Accéder à Swagger**
   ```
   http://localhost:8080/swagger-ui.html
   ```

---

## 💡 Utilisation

### Première Connexion

1. **Ouvrir Swagger UI** : http://localhost:8080/swagger-ui.html
2. **Trouver** `POST /api/auth/login`
3. **Cliquer sur "Try it out"**
4. **Entrer les identifiants :**
   ```json
   {
     "email": "admin@edugo.com",
     "motDePasse": "admin123"
   }
   ```
5. **Copier le token** de la réponse
6. **Cliquer sur "Authorize"** (en haut à droite)
7. **Coller** : `Bearer VOTRE_TOKEN`
8. **Utiliser tous les endpoints !**

### Exemple : Créer un Niveau

```bash
POST /api/admin/niveaux
Content-Type: application/json
Authorization: Bearer VOTRE_TOKEN

{
  "nom": "CP1"
}
```

### Exemple : Créer une Classe

```bash
POST /api/admin/classes
Content-Type: application/json
Authorization: Bearer VOTRE_TOKEN

{
  "nom": "6ème A",
  "niveau": {
    "id": 1
  }
}
```

---

## 📁 Structure du Projet

```
edugo/
├── src/main/java/com/example/edugo/
│   ├── config/              # Configuration (Security, Swagger, Admin)
│   ├── controller/          # Contrôleurs REST
│   │   ├── AuthController.java
│   │   ├── AdminController.java
│   │   └── EleveController.java
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # Entités JPA
│   │   ├── Principales/     # Entités métier
│   │   ├── User.java
│   │   └── Admin.java
│   ├── exception/           # Gestion d'exceptions
│   ├── repository/          # Repositories JPA
│   ├── security/            # Configuration sécurité JWT
│   └── service/             # Services métier
├── src/main/resources/
│   └── application.properties
├── pom.xml
├── README.md
├── QUICK_START.md
├── GUIDE_API.md
├── DOCUMENTATION_SWAGGER.md
└── READY_TO_USE.md
```

---

## 🔐 Sécurité

### Authentification JWT

- **Token d'accès** : 24 heures de validité
- **Refresh token** : 7 jours de validité
- **Chiffrement** : BCrypt pour les mots de passe

### Rôles

- **ADMIN** : Accès complet à tous les endpoints
- **ELEVE** : Accès aux fonctionnalités élèves uniquement

### Protection des Endpoints

- 🔓 **Publics** : `/api/auth/**`
- 🔐 **Admin** : `/api/admin/**`
- 🔐 **Élèves + Admin** : `/api/eleve/**`

---

## 📊 Statistiques du Projet

- **Endpoints** : 50+
- **Modules** : 10+
- **Repositories** : 15+
- **Services** : 10+
- **Controllers** : 3
- **Entités** : 30+

---

## 🤝 Contribuer

Les contributions sont les bienvenues !

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

## 📝 License

Ce projet est sous licence Apache 2.0.

---

## 📞 Support

Pour toute question ou support :

- 📧 **Email** : support@edugo.ml
- 🌐 **Website** : https://www.edugo.ml
- 📖 **Documentation** : http://localhost:8080/swagger-ui.html

---

## 🎉 Merci !

Merci d'utiliser EDUGO pour l'éducation malienne.

**Développé avec ❤️ pour l'éducation au Mali**

---

<div align="center">

**Made with ❤️ in Mali 🇲🇱**

[Documentation](#-documentation) • [Installation](#-installation) • [Usage](#-utilisation)

</div>

