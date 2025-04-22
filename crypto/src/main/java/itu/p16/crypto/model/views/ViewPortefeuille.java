package itu.p16.crypto.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ViewPortefeuille { 

    @Id
    @Column(name = "rownum")
    private int rownum;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "id_crypto")
    private int idCrypto;

    @Column(name = "crypto_name")
    private String cryptoName;

    @Column(name = "crypto_symbol")
    private String cryptoSymbol;

    @Column(name = "quantite_actuelle")
    private double quantiteActuelle;

    @Column(name = "prix_unitaire_actuel")
    private double prixUnitaireActuel;

    // Getters and setters
    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdCrypto() {
        return idCrypto;
    }

    public void setIdCrypto(int idCrypto) {
        this.idCrypto = idCrypto;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public double getQuantiteActuelle() {
        return quantiteActuelle;
    }

    public void setQuantiteActuelle(double quantiteActuelle) {
        this.quantiteActuelle = quantiteActuelle;
    }

    public double getPrixUnitaireActuel() {
        return prixUnitaireActuel;
    }

    public void setPrixUnitaireActuel(double prixUnitaireActuel) {
        this.prixUnitaireActuel = prixUnitaireActuel;
    }
}
