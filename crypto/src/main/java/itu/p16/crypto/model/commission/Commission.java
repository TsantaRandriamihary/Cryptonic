package itu.p16.crypto.model.commission;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Commission")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})

public class Commission implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCommission;

    @Column(name = "pourcent_achat")
    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double pourcentAchat;

    @Column(name = "pourcent_vente")
    @Convert(converter = DoubleToBigDecimalConverter.class)
    private Double pourcentVente;

    @Column(name = "date_modif")
    private Timestamp dateModif;

    public Commission() {
       
    }
    public Commission(double pourcentAchat, double pourcentVente) {
        this.pourcentAchat = pourcentAchat;
        this.pourcentVente = pourcentVente;
    }
    
    public Commission(double pourcentVente, double pourcentAchat, Timestamp dateModif) {
        this.pourcentAchat = pourcentAchat;
        this.pourcentVente = pourcentVente;
        this.dateModif = dateModif;
    }

    // Getters and setters
    public int getIdCommission() {
        return idCommission;
    }

    public void setIdCommission(int idCommission) {
        this.idCommission = idCommission;
    }

    public Double getPourcentAchat() {
        return pourcentAchat;
    }

    public void setPourcentAchat(double pourcentAchat) {
        this.pourcentAchat = pourcentAchat;
    }

    public Double getPourcentVente() {
        return pourcentVente;
    }

    public void setPourcentVente(double pourcentVente) {
        this.pourcentVente = pourcentVente;
    }

    public Timestamp getDateModif() {
        return dateModif;
    }

    public void setDateModif(Timestamp dateModif) {
        this.dateModif = dateModif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commission that = (Commission) o;
        return idCommission == that.idCommission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommission);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idCommission);
    }
}
