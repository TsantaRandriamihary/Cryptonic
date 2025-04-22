package itu.p16.crypto.model.user;

import java.time.LocalDate;
import java.util.List;

import itu.p16.crypto.model.views.ViewMouvementCrypto;

public class UtilisateurWithProfil {

    private Utilisateur user; 
    private UserProfil userProfil;
    private List<ViewMouvementCrypto> mouvements;

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public UserProfil getUserProfil() {
        return userProfil;
    }

    public void setUserProfil(UserProfil userProfil) {
        this.userProfil = userProfil;
    }

    public List<ViewMouvementCrypto> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<ViewMouvementCrypto> mouvements) {
        this.mouvements = mouvements;
    }
}
