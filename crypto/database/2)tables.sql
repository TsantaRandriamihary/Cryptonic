CREATE TABLE Crypto(
   id_crypto SERIAL,
   nom VARCHAR(255)  NOT NULL,
   symbole VARCHAR(50) ,
   prix NUMERIC(15,2)   NOT NULL,
   prix_min NUMERIC(15,2)   NOT NULL,
   prix_max NUMERIC(15,2)   NOT NULL,
   PRIMARY KEY(id_crypto)
);

CREATE TABLE Mvt_crypto(
   id_mvt SERIAL,
   date_mvt TIMESTAMP NOT NULL,
   quantite NUMERIC(15,2)   NOT NULL,
   montant_achat NUMERIC(15,2)  ,
   id_user INTEGER NOT NULL,
   montant_vente NUMERIC(15,2)  ,
   commission_achat NUMERIC(15,2)  ,
   commission_vente NUMERIC(15,2)  ,
   id_crypto INTEGER NOT NULL,
   PRIMARY KEY(id_mvt),
   FOREIGN KEY(id_crypto) REFERENCES Crypto(id_crypto)
);

CREATE TABLE Crypto_histo(
   id_crypto_histo SERIAL,
   prix NUMERIC(15,2)   NOT NULL,
   date_modif TIMESTAMP NOT NULL,
   id_crypto INTEGER NOT NULL,
   PRIMARY KEY(id_crypto_histo),
   FOREIGN KEY(id_crypto) REFERENCES Crypto(id_crypto)
);

CREATE TABLE Demande_transaction(
   id_demande SERIAL,
   montant_depot NUMERIC(15,2)  ,
   montant_retrait NUMERIC(15,2)  ,
   date_creation TIMESTAMP,
   id_user INTEGER NOT NULL,
   est_traite BOOLEAN NOT NULL,
   quantite NUMERIC(15,2)  ,
   id_crypto INTEGER,
   PRIMARY KEY(id_demande),
   FOREIGN KEY(id_crypto) REFERENCES Crypto(id_crypto)
);


CREATE TABLE Commission(
   id_commission SERIAL,
   pourcent_achat NUMERIC(15,2)  ,
   pourcent_vente NUMERIC(15,2)  ,
   date_modif TIMESTAMP,
   PRIMARY KEY(id_commission)
);

CREATE TABLE Favori(
   id_favori SERIAL,
   id_user INTEGER,
   id_crypto INTEGER,
   PRIMARY KEY(id_favori),
   FOREIGN KEY(id_crypto) REFERENCES Crypto(id_crypto)
);

CREATE TABLE Transaction(
   id_transaction SERIAL,
   date_transaction TIMESTAMP NOT NULL,
   montant_depot NUMERIC(15,2)  ,
   montant_retrait NUMERIC(15,2)  ,
   id_user INTEGER NOT NULL,
   id_mvt INTEGER,
   PRIMARY KEY(id_transaction),
   FOREIGN KEY(id_mvt) REFERENCES Mvt_crypto(id_mvt)
);
