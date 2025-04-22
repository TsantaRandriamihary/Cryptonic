# Documentation de l'application Identity Provider

## 1. Introduction
### 1.1. Présentation
Identity Provider est une application RESTful permettant de gérer l'inscription, l'authentification multifacteur, la gestion des rôles et la sécurité des utilisateurs. Elle repose sur C# et utilise Docker pour la conteneurisation. L'objectif est de fournir un système sécurisé et flexible pour gérer les identités.

### 1.2. Fonctionnalités principales
- **Inscription avec validation par e-mail**.
- **Authentification multifacteur** : PIN à usage unique (valable 90 secondes).
- **Gestion des rôles utilisateurs** : administrateur, membre, etc.
- **Gestion de compte utilisateur** : modification des informations personnelles (sauf e-mail).
- **Durée de vie configurable des sessions**.
- **Sécurisation des mots de passe** : hashing sécurisé.
- **Limitation des tentatives de connexion** : configurable (par défaut, 3 tentatives).
- **Documentation API** : générée via Swagger.

---

## 2. Installation

### 2.1. Prérequis
- **Docker** : Assurez-vous que Docker est installé et configuré sur votre machine.
- **Autres outils** : Consultez le fichier `README.md` pour toute dépendance ou configuration supplémentaire.

### 2.2. Instructions
1. Clonez le dépôt du projet :
   ```bash
   git clone https://github.com/ETU002393Mahandry/Identity-Provider
   ```
2. Naviguez dans le dossier du projet :
   ```bash
   cd identity-provider
   ```
3. Construisez et démarrez les conteneurs Docker :
   ```bash
   docker-compose up --build
   ```
4. Accédez à la documentation Swagger générée à l'adresse suivante :
   ```
   http://localhost:5140/swagger
   ```

---

## 3. Architecture de la base de données

### 3.1. Description des tables
Voici un aperçu des principales tables utilisées dans la base de données :

- **`Etat_compte`** : Statut du compte utilisateur.
- **`Role`** : Rôles attribués aux utilisateurs.
- **`Utilisateur`** : Informations de base des utilisateurs (e-mail, mot de passe, état du compte).
- **`Mdp_histo`** : Historique des mots de passe pour chaque utilisateur.
- **`PIN_histo`** : Historique des PIN utilisés pour l'authentification.
- **`User_session`** : Gestion des sessions utilisateur avec dates d'expiration.
- **`Tentative_histo`** : Historique des tentatives de connexion (succès/échec).
- **`Genre`** : Genre des utilisateurs.
- **`Parametre`** : Paramètres globaux (durée PIN, sessions, etc.).
- **`User_profile`** : Profil utilisateur détaillé (nom, prénom, date de naissance, etc.).

### 3.2. Schéma relationnel
*(À insérer : Diagramme UML ou description des relations entre les tables)*

---

## 4. Scénarios d'utilisation

### 4.1. Inscription d'un utilisateur
1. L'utilisateur soumet son adresse e-mail et un mot de passe via l'API.
2. Le système envoie un code PIN à usage unique à l'adresse e-mail fournie (valable 90 secondes).
3. L'utilisateur soumet le code PIN via l'API pour valider son inscription.
4. Une fois validé, l'utilisateur remplit les informations de son profil (nom, prénom, etc.).

### 4.2. Connexion d'un utilisateur
1. L'utilisateur soumet son adresse e-mail et son mot de passe.
2. Si le mot de passe est incorrect, le nombre de tentatives échouées est incrémenté.
3. Après 3 tentatives échouées, l'utilisateur doit soumettre un nouveau code PIN envoyé par e-mail.
4. Si les informations sont correctes, une session utilisateur est créée avec une durée de vie définie.

---

## 5. Endpoints API
*(À compléter : Liste des endpoints avec leurs méthodes HTTP, paramètres, et exemples de requêtes/réponses)*

---

## 6. Maintenance
- **Mises à jour** : Les mises à jour du projet doivent être effectuées en mettant à jour les conteneurs Docker :
  ```bash
  docker-compose down
  docker-compose up --build
  ```
- **Sauvegardes** : Assurez-vous de sauvegarder régulièrement les données de la base (via des outils comme `pg_dump` si PostgreSQL est utilisé).

---

## 7. Annexes
- **Liens utiles** :
  - Documentation Docker : [https://docs.docker.com/](https://docs.docker.com/)
  - Swagger : [https://swagger.io/](https://swagger.io/)
- **Glossaire** :
  - **PIN** : Code personnel à usage unique pour la validation.
  - **Session** : Période durant laquelle un utilisateur reste authentifié.
- **Todo list** : *(Inclure la liste existante ici si nécessaire)*
