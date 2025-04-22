package itu.p16.crypto.model.user;

public class Utilisateur {
    int idUser,idEtat, idRole;
    String email, mdp;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int IdUser) {
        idUser = IdUser;
    }

    public int getIdEtat() {
        return idEtat;
    }

    public void setIdEtat(int IdEtat) {
        idEtat = IdEtat;
    }

    public int getIdRole(){
        return idRole;
    }

    public void setIdRole(int IdRole){
        idRole = IdRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        email = Email;
    }

    public String getMdp() {
        return mdp;
    }
    
    public void setMdp(String Mdp) {
        mdp = Mdp;
    }
    
}
