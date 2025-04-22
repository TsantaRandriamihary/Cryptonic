# MEMBRES DU GROUPE 
- **ANDRIANANTENAINA Mahandry ETU002393**
- **SAMIANDRISOA Mamy Marcello ETU002641**
- **ANDRINIAINA Fihobiana ETU002411**
- **RANDRIAMIHARY Tsanta Mialitiana Fenosoa ETU002576**

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


# Documentation de l'application Crypto

## 1. Introduction
### 1.1. Présentation
Crypto est une application web développée avec Spring Boot et Thymeleaf, permettant la gestion des cryptomonnaies. Elle intègre des fonctionnalités de gestion des transactions, de portefeuille et de suivi des cours. L'application repose sur le web service Identity Provider pour l'inscription et l'authentification sécurisée des utilisateurs.

### 1.2. Fonctionnalités principales
- **Inscription et connexion sécurisées via l'Identity Provider**.
- **Affichage des cours des cryptomonnaies** :les prix sont mis à jour dynamiquement toutes les 10 secondes (modifiables).
- **Gestion des fonds de l'utilisateur** : Affichage de fonds, historique de dépôts et retraits.
- **Transactions de cryptomonnaies** : avec confirmation via PIN par e-mail.
   - ***Achat*** : (avec vérification des fonds disponibles).
   - ***Vente*** : (avec vérification des quantites disponibles).
   - ***Confirmation par PIN pour toutes les transactions*** : 
- **Portefeuille utilisateur** : liste des cryptomonnaies détenues avec leurs quantités.
- **Historique des mouvement de cryptomonnaie** : liste de l'historique des achats et ventes.
- **Graphique d'évolution des prix des cryptomonnaies.** : Graphique d'évolution des prix des cryptomonnaies.

---


## 2. Installation

### 2.1. Prérequis
- **Docker** : Assurez-vous que Docker est installé et configuré sur votre machine.
- **Java 17 ou supérieur**
- **Maven**
- **Docker** : Pour la base de données PostgreSQL.
- **Identity Provider** : Assurez-vous que l'Identity Provider est configuré et fonctionnel.

### 2.2. Instructions pour demarrer le projet
1. Clonez le dépôt du projet :
   ```bash
   git clone https://github.com/ETU002393Mahandry/Identity-Provider
   ```
2. Naviguez dans le dossier du projet :
   ```bash
   cd crypto
   ```

3. Construisez et démarrez les conteneurs Docker :
   ```bash
   docker-compose up --build
   ```

4. Configurez les paramètres dans application.properties :
spring.datasource.url=jdbc:postgresql://localhost:5432/crypto
spring.datasource.username=votre_user_postgres
spring.datasource.password=votre_pwd_postgres
spring.jpa.hibernate.ddl-auto=update
---

5. Naviguez vers le dossier database pour configurer la base de donnees PostgreSQL et executez les commandes docker : 

   docker run --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=crypto -p 5432:5432 -d postgres:latest
   docker exec -it postgres-container psql -U postgres -d crypto

    ```bash
   cd database
   ```

6. Executez dans l'ordre dans la commande PSQL : database.sql => tables.sql => views.sql => trigger.sql => insert.sql


## 3. Architecture de la base de données

### 3.1. Description des tables
Voici un aperçu des principales tables utilisées dans la base de données :

- **Crypto** : Informations sur les cryptomonnaies.
- **Crypto_histo** : Historique des prix des cryptomonnaies.
- **Mvt_crypto** : Historique des mouvements d'achat et de vente de cryptomonnaies.
- **PIN_histo** : Historique des codes PIN pour la confirmation des transactions.
- **Transaction** : Transactions des utilisateurs (dépôt, retrait, achat, vente).
- **Parametre** : Configuration globale de l'application.

## 4. Scénarios d'utilisation
### 4.1. Inscription et connexion
1. Les utilisateurs s'inscrivent via le web service Identity Provider.
2. Après validation par e-mail, ils peuvent se connecter pour accéder aux fonctionnalités.
### 4.2. Gestion des cryptomonnaies
1. L'utilisateur accède à la liste des cryptomonnaies disponibles avec leurs cours actualisés toutes les 10 secondes.
2. Les prix varient aléatoirement entre prix_min et prix_max.
### 4.3. Gestion des fonds
1. Dépôt :
   1. L'utilisateur entre un montant à déposer.
   2. Un PIN est envoyé par e-mail pour confirmer l'opération.
2. Retrait :
   1. L'utilisateur entre un montant à retirer.
   2. Un PIN est également requis pour valider le retrait.
### 4.4. Transactions de cryptomonnaies
1. Achat :
   1. L'utilisateur choisit une cryptomonnaie et entre une quantité.
   2. Le système vérifie que l'utilisateur dispose de suffisamment de fonds.
   3. Un PIN est requis pour valider l'achat.
2. Vente :
   1. L'utilisateur choisit une cryptomonnaie et une quantité à vendre.
   2. Le système vérifie que l'utilisateur possède suffisamment de quantités dans son portefeuille.
   3. Un PIN est requis pour valider la vente.
### 4.5. Portefeuille et historique
1. L'utilisateur peut consulter son portefeuille actuel (cryptomonnaies et quantités).
2. Il peut également consulter l'historique de ses transactions (dépôt, retrait, achat, vente).
### 4.6. Graphique d'évolution des prix
1. Un graphique interactif montre l'évolution des prix des cryptomonnaies, basé sur les données de la table Crypto_histo.




## 5. Maintenance
### 5.1. Mises à jour
- Pour appliquer des mises à jour :

mvn clean install
docker-compose down
docker-compose up --build

## 6. Annexes
### 6.1. Liens utiles
- Documentation Spring Boot : https://spring.io/projects/spring-boot
- Documentation Thymeleaf : https://www.thymeleaf.org/
- Documentation Docker : https://docs.docker.com/
### 6.2. Glossaire
- **PIN** : Code personnel unique utilisé pour confirmer les transactions.
- **Portefeuille** : Ensemble des cryptomonnaies détenues par un utilisateur.
- **Session** : Période durant laquelle un utilisateur reste connecté.
