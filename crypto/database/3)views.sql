-- Liste portefeuille par utilisateur corrigée avec un rownum simple (ROW_NUMBER)
CREATE OR REPLACE VIEW view_portefeuille AS
SELECT
    ROW_NUMBER() OVER () AS rownum,  
    mc.id_user,
    c.id_crypto,
    c.nom AS crypto_name,
    c.symbole AS crypto_symbol,
    (
        COALESCE(SUM(CASE WHEN mc.montant_achat > 0 THEN mc.quantite ELSE 0 END), 0) - 
        COALESCE(SUM(CASE WHEN mc.montant_vente > 0 THEN mc.quantite ELSE 0 END), 0)
    ) AS quantite_actuelle,
    c.prix AS prix_unitaire_actuel
FROM
    Mvt_crypto mc
JOIN
    Crypto c ON mc.id_crypto = c.id_crypto
GROUP BY
    mc.id_user, c.id_crypto, c.nom, c.symbole, c.prix
ORDER BY
    mc.id_user, c.nom;

    
-- Fonds actuels
CREATE OR REPLACE VIEW view_fonds AS
SELECT
    ROW_NUMBER() OVER () AS rownum,  
    t.id_user,
    COALESCE(SUM(t.montant_depot), 0) - COALESCE(SUM(t.montant_retrait), 0) AS fond_actuel
FROM 
    Transaction t
GROUP BY 
    t.id_user
ORDER BY 
    t.id_user;

-- Transactions historique
CREATE OR REPLACE VIEW view_transactions AS
SELECT 
    t.id_transaction,
    t.date_transaction,
    t.montant_depot,
    t.montant_retrait,
    t.id_mvt,
    t.id_user
FROM 
    Transaction t
ORDER BY 
    t.id_user, t.date_transaction;


-- Mouvement de crypto
CREATE OR REPLACE VIEW view_mouvement_crypto AS
SELECT 
    mc.id_mvt,
    mc.date_mvt,
    mc.quantite,
    mc.montant_achat,
    mc.montant_vente,
    mc.commission_achat,
    mc.commission_vente,
    mc.id_user,
    mc.id_crypto,
    c.nom AS crypto_nom,
    c.symbole AS crypto_symbole
FROM 
    Mvt_crypto mc
JOIN 
    Crypto c ON mc.id_crypto = c.id_crypto
ORDER BY 
    mc.date_mvt ASC,         
    mc.id_user ASC,           
    mc.montant_achat ASC;    


-- Historique de changement de cours
CREATE OR REPLACE VIEW view_crypto_historique AS
SELECT 
    ROW_NUMBER() OVER () AS rownum,  
    c.id_crypto,
    c.nom AS crypto_nom,
    c.symbole AS crypto_symbole,
    ch.prix AS prix_historique,
    ch.date_modif AS date_changement
FROM 
    Crypto c
JOIN 
    Crypto_histo ch ON c.id_crypto = ch.id_crypto
ORDER BY 
    c.id_crypto, ch.date_modif;
