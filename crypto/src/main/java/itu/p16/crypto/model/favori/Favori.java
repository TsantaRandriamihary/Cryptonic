package itu.p16.crypto.model.favori;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import itu.p16.crypto.converter.DoubleToBigDecimalConverter;
import itu.p16.crypto.listener.GenericEntityListener;
import itu.p16.crypto.model.Identifiable;

@Entity
@Table(name = "Favori")
@EntityListeners({GenericEntityListener.class, AuditingEntityListener.class})

public class Favori implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFavori;

    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "id_crypto")
    private Integer idCrypto;


    public Favori() {
       
    }
    
    // Getters and setters
    public Integer getIdFavori() {
        return idFavori;
    }

    public void setIdFavori(Integer idFavori) {
        this.idFavori = idFavori;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Integer getIdCrypto() {
        return idCrypto;
    }

    public void setIdCrypto(int idCrypto) {
        this.idCrypto = idCrypto;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favori that = (Favori) o;
        return idFavori == that.idFavori;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFavori);
    }

    @Override
    public String getId() {
        return String.valueOf(this.idFavori);
    }

}
