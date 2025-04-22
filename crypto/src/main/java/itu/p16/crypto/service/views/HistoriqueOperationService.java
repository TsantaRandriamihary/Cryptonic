package itu.p16.crypto.service.views;

import itu.p16.crypto.model.user.UtilisateurWithProfil;
import itu.p16.crypto.model.views.ViewMouvementCrypto;
import itu.p16.crypto.service.user.UtilisateurService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoriqueOperationService {

    private final UtilisateurService utilisateurService;
    private final ViewMouvementCryptoService viewMouvementCryptoService;

    public HistoriqueOperationService(UtilisateurService utilisateurService, ViewMouvementCryptoService viewMouvementCryptoService) {
        this.utilisateurService = utilisateurService;
        this.viewMouvementCryptoService = viewMouvementCryptoService;
    }

    public List<UtilisateurWithProfil> getUsersWithCryptoMovements() {
        List<UtilisateurWithProfil> usersWithProfiles = utilisateurService.getAllUtilisateursWithProfiles();

        return usersWithProfiles.stream().map(userWithProfile -> {
            int userId = userWithProfile.getUser().getIdUser();
            List<ViewMouvementCrypto> mouvements = viewMouvementCryptoService.getMouvementsByIdUser(userId);
            userWithProfile.setMouvements(mouvements); 
            return userWithProfile;
        }).collect(Collectors.toList());
    }


    public List<UtilisateurWithProfil> filterUsersWithCryptoMovements(Timestamp dateHeure, Integer idCrypto, Integer idUser) {
        List<UtilisateurWithProfil> usersWithProfiles = getUsersWithCryptoMovements();
        if (idUser != null) {
            usersWithProfiles = usersWithProfiles.stream()
                .filter(user -> user.getUser().getIdUser() == idUser)
                .collect(Collectors.toList());
        }
        for (UtilisateurWithProfil userWithProfile : usersWithProfiles) {
            List<ViewMouvementCrypto> filteredMovements = userWithProfile.getMouvements().stream()
                .filter(mvt -> (dateHeure == null || mvt.getDateMvt().before(dateHeure) || mvt.getDateMvt().equals(dateHeure))) 
                .filter(mvt -> (idCrypto == null || mvt.getIdCrypto() == idCrypto)) 
                .collect(Collectors.toList());
    
            userWithProfile.setMouvements(filteredMovements);
        }
        return usersWithProfiles.stream()
            .filter(user -> !user.getMouvements().isEmpty())
            .collect(Collectors.toList());
    }
}
