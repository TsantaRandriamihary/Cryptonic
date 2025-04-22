CREATE TABLE Etat_compte(
   id_etat SERIAL,
   val VARCHAR(255)  NOT NULL,
   PRIMARY KEY(id_etat)
);

CREATE TABLE Role(
   id_role SERIAL,
   val VARCHAR(255)  NOT NULL,
   PRIMARY KEY(id_role)
);

CREATE TABLE Utilisateur(
   id_user SERIAL,
   email VARCHAR(255)  NOT NULL,
   mdp VARCHAR(255)  NOT NULL,
   id_etat INTEGER NOT NULL,
   id_role INTEGER NOT NULL DEFAULT 1,
   PRIMARY KEY(id_user),
   UNIQUE(email),
   FOREIGN KEY(id_etat) REFERENCES Etat_compte(id_etat),
   FOREIGN KEY(id_role) REFERENCES Role(id_role)
);

CREATE TABLE Mdp_histo(
   id_mdp SERIAL,
   mdp_new VARCHAR(255)  NOT NULL,
   date_modif TIMESTAMP NOT NULL,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_mdp),
   UNIQUE(mdp_new),
   FOREIGN KEY(id_user) REFERENCES Utilisateur(id_user)
);

CREATE TABLE PIN_histo(
   id_pin SERIAL,
   pin INTEGER NOT NULL,
   date_creation TIMESTAMP,
   date_expiration TIMESTAMP,
   email VARCHAR(255)  NOT NULL,
   PRIMARY KEY(id_pin)
);

CREATE TABLE User_session(
   id_session VARCHAR(255) ,
   date_creation TIMESTAMP NOT NULL,
   date_expiration TIMESTAMP NOT NULL,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_session),
   FOREIGN KEY(id_user) REFERENCES Utilisateur(id_user)
);

CREATE TABLE Tentative_histo(
   id_tentative SERIAL,
   success BOOLEAN NOT NULL,
   date_connection TIMESTAMP NOT NULL,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_tentative),
   FOREIGN KEY(id_user) REFERENCES Utilisateur(id_user)
);

CREATE TABLE Genre(
   id_genre SERIAL,
   val VARCHAR(255)  NOT NULL,
   PRIMARY KEY(id_genre)
);

CREATE TABLE Parametre(
   id_parametre SERIAL,
   nbr_tentative INTEGER DEFAULT 3,
   duree_tentative INTEGER DEFAULT 90,
   duree_session INTEGER DEFAULT 86400,
   duree_pin INTEGER DEFAULT 90,
   PRIMARY KEY(id_parametre)
);

CREATE TABLE User_profile(
   id_profil SERIAL,
   nom VARCHAR(255)  NOT NULL,
   prenom VARCHAR(255)  NOT NULL,
   naissance DATE NOT NULL,
   image TEXT,
   id_genre INTEGER NOT NULL,
   id_user INTEGER NOT NULL,
   PRIMARY KEY(id_profil),
   FOREIGN KEY(id_genre) REFERENCES Genre(id_genre),
   FOREIGN KEY(id_user) REFERENCES Utilisateur(id_user)
);


