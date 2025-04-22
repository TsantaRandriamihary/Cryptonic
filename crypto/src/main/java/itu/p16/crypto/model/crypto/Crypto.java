package itu.p16.crypto.model.crypto;

import jakarta.persistence.*;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Crypto")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})

public class Crypto implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCrypto;

    @Column(nullable = false)
    private String nom;

    @Column(length = 50)
    private String symbole;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    @Column(nullable = false)
    private double prix;

    @Convert(converter = DoubleToBigDecimalConverter.class)
    @Column(nullable = false)
    private double prixMin;
    
    @Convert(converter = DoubleToBigDecimalConverter.class)
    @Column(nullable = false)
    private double prixMax;

    // Getters and setters
    public int getIdCrypto() {
        return idCrypto;
    }

    public void setIdCrypto(int idCrypto) {
        this.idCrypto = idCrypto;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    }

    public double getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(double prixMax) {
        this.prixMax = prixMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return idCrypto == crypto.idCrypto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCrypto);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idCrypto);
    }
}

