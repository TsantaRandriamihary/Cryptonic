package itu.p16.crypto.model.param;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parametre {
    
    @JsonProperty("idParametre")
    private int idParametre;
    
    @JsonProperty("nbrTentative")
    private int nbrTentative;
    
    @JsonProperty("dureeTentative")
    private int dureeTentative;
    
    @JsonProperty("dureeSession")
    private int dureeSession;
    
    @JsonProperty("dureePin")
    private int dureePin;

    public int getIdParametre() {
        return idParametre;
    }

    public void setIdParametre(int idParametre) {
        this.idParametre = idParametre;
    }

    public int getNbrTentative() {
        return nbrTentative;
    }

    public void setNbrTentative(int nbrTentative) {
        this.nbrTentative = nbrTentative;
    }

    public int getDureeTentative() {
        return dureeTentative;
    }

    public void setDureeTentative(int dureeTentative) {
        this.dureeTentative = dureeTentative;
    }

    public int getDureeSession() {
        return dureeSession;
    }

    public void setDureeSession(int dureeSession) {
        this.dureeSession = dureeSession;
    }

    public int getDureePin() {
        return dureePin;
    }

    public void setDureePin(int dureePin) {
        this.dureePin = dureePin;
    }
}
