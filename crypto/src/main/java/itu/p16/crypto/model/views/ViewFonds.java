package itu.p16.crypto.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ViewFonds {

    @Id
    @Column(name = "rownum")  
    private int rownum;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "fond_actuel")
    private double fondActuel;

    // Getters and setters
    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public double getFondActuel() {
        return fondActuel;
    }

    public void setFondActuel(double fondActuel) {
        this.fondActuel = fondActuel;
    }
}
