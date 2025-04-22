package itu.p16.crypto.model.transaction;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Demande_transaction")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})

public class DemandeTransaction implements Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande")
    private Integer idDemande; 

    @Column(name = "montant_depot")
    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantDepot;

    @Column(name = "montant_retrait")
    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double montantRetrait;

    @Column(name = "date_creation")
    private Timestamp dateCreation;

    @Column(name = "id_user", nullable = false)
    private int idUser;

    @Column(name = "est_traite", nullable = false)
    private boolean estTraite;

    @Column(name = "quantite")
    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double quantite;

    @Column(name = "id_crypto")
    private Integer idCrypto;

    // Getters et Setters
    public Integer getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Integer idDemande) {
        this.idDemande = idDemande;
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

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isEstTraite() {
        return estTraite;
    }

    public void setEstTraite(boolean estTraite) {
        this.estTraite = estTraite;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Integer getIdCrypto() {
        return idCrypto;
    }

    public void setIdCrypto(Integer idCrypto) {
        this.idCrypto = idCrypto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandeTransaction that = (DemandeTransaction) o;
        return idDemande == that.idDemande;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDemande);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idDemande);
    }
}
