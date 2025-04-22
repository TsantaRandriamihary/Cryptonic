package itu.p16.crypto.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ViewMouvementCrypto {

    @Id
    @Column(name = "id_mvt")
    private int idMvt;

    @Column(name = "date_mvt")
    private java.sql.Timestamp dateMvt;

    @Column(name = "quantite")
    private double quantite;

    @Column(name = "montant_achat")
    private Double montantAchat;

    @Column(name = "montant_vente")
    private Double montantVente;

    @Column(name = "commission_achat")
    private Double commissionAchat;

    @Column(name = "commission_vente")
    private Double commissionVente;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "id_crypto")
    private int idCrypto;

    @Column(name = "crypto_nom")
    private String cryptoNom;

    @Column(name = "crypto_symbole")
    private String cryptoSymbole;

    // Getters and setters
    public int getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(int idMvt) {
        this.idMvt = idMvt;
    }

    public java.sql.Timestamp getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(java.sql.Timestamp dateMvt) {
        this.dateMvt = dateMvt;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Double getMontantAchat() {
        return montantAchat;
    }

    public void setMontantAchat(double montantAchat) {
        this.montantAchat = montantAchat;
    }

    public Double getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(double montantVente) {
        this.montantVente = montantVente;
    }



    public Double getCommissionAchat() {
        return commissionAchat;
    }

    public void setCommissionAchat(double commissionAchat) {
        this.commissionAchat = commissionAchat;
    }

    public Double getCommissionVente() {
        return commissionVente;
    }

    public void setCommissionVente(double commissionVente) {
        this.commissionVente = commissionVente;
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
}
