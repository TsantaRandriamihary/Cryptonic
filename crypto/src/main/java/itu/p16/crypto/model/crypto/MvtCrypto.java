package itu.p16.crypto.model.crypto;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Mvt_crypto")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})

public class MvtCrypto  implements Identifiable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMvt;

    @Column(nullable = false)
    private Timestamp dateMvt;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    @Column(nullable = false)
    private Double quantite;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantAchat;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantVente;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double commissionAchat;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double commissionVente;

    @Column(nullable = false)
    private int idUser;

    @Column(nullable = false)
    private int idCrypto;

    // Getters and Setters
    public int getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(int idMvt) {
        this.idMvt = idMvt;
    }

    public Timestamp getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(Timestamp dateMvt) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MvtCrypto mvtCrypto = (MvtCrypto) o;
        return idMvt == mvtCrypto.idMvt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMvt);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idMvt);
    }
}
