package itu.p16.crypto.model.transaction;

import jakarta.persistence.*;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Transaction")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})
public class Transaction implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransaction;

    @Column(nullable = false)
    private java.sql.Timestamp dateTransaction;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantDepot;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantRetrait;

    private Integer idUser;

    private Integer idMvt;
    
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

    public Double getMontantDepot() {
        return montantDepot;
    }

    public void setMontantDepot(double montantDepot) {
        this.montantDepot = montantDepot;
    }

    public Double getMontantRetrait() {
        return montantRetrait;
    }

    public void setMontantRetrait(double montantRetrait) {
        this.montantRetrait = montantRetrait;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Integer getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(int idMvt) {
        this.idMvt = idMvt;
    }

    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return idTransaction == that.idTransaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaction);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idTransaction);
    }
}
