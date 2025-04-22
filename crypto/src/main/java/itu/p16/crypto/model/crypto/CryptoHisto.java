package itu.p16.crypto.model.crypto;

import jakarta.persistence.*;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Crypto_histo")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})
public class CryptoHisto implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCryptoHisto;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private java.sql.Timestamp dateModif;

    @ManyToOne
    @JoinColumn(name = "id_crypto", nullable = false)
    private Crypto crypto;

    // Getters and setters
    public int getIdCryptoHisto() {
        return idCryptoHisto;
    }

    public void setIdCryptoHisto(int idCryptoHisto) {
        this.idCryptoHisto = idCryptoHisto;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public java.sql.Timestamp getDateModif() {
        return dateModif;
    }

    public void setDateModif(java.sql.Timestamp dateModif) {
        this.dateModif = dateModif;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoHisto that = (CryptoHisto) o;
        return idCryptoHisto == that.idCryptoHisto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCryptoHisto);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idCryptoHisto);
    }
}
