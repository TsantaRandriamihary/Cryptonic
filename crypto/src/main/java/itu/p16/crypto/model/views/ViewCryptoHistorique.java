package itu.p16.crypto.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ViewCryptoHistorique {

    @Id
    @Column(name = "rownum")
    private int rownum;

    @Column(name = "id_crypto")
    private int idCrypto;

    @Column(name = "crypto_nom")
    private String cryptoNom;

    @Column(name = "crypto_symbole")
    private String cryptoSymbole;

    @Column(name = "prix_historique")
    private double prixHistorique;

    @Column(name = "date_changement")
    private java.sql.Timestamp dateChangement;

    // Getters and setters
    public int getIdCrypto() {
        return idCrypto;
    }

    public void setIdCrypto(int idCrypto) {
        this.idCrypto = idCrypto;
    }

    public String getCryptoNom() {
        return cryptoNom;
    }

    public void setCryptoNom(String cryptoNom) {
        this.cryptoNom = cryptoNom;
    }

    public String getCryptoSymbole() {
        return cryptoSymbole;
    }

    public void setCryptoSymbole(String cryptoSymbole) {
        this.cryptoSymbole = cryptoSymbole;
    }

    public double getPrixHistorique() {
        return prixHistorique;
    }

    public void setPrixHistorique(double prixHistorique) {
        this.prixHistorique = prixHistorique;
    }

    public java.sql.Timestamp getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(java.sql.Timestamp dateChangement) {
        this.dateChangement = dateChangement;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

}
