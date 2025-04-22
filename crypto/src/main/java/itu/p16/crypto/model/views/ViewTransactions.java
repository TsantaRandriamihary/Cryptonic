package itu.p16.crypto.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ViewTransactions {

    @Id
    @Column(name = "id_transaction")
    private int idTransaction;

    @Column(name = "date_transaction")
    private java.sql.Timestamp dateTransaction;

    @Column(name = "montant_depot")
    private double montantDepot;

    @Column(name = "montant_retrait")
    private double montantRetrait;

    @Column(name = "id_mvt")
    private Integer idMvt; // Change le type de 'int' à 'Integer'

    @Column(name = "id_user")
    private int idUser;

    // Getters and setters
    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public java.sql.Timestamp getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(java.sql.Timestamp dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public double getMontantDepot() {
        return montantDepot;
    }

    public void setMontantDepot(double montantDepot) {
        this.montantDepot = montantDepot;
    }

    public double getMontantRetrait() {
        return montantRetrait;
    }

    public void setMontantRetrait(double montantRetrait) {
        this.montantRetrait = montantRetrait;
    }

    public Integer getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(Integer idMvt) {
        this.idMvt = idMvt;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
