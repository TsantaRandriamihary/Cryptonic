package itu.p16.crypto.service.views;


import itu.p16.crypto.model.user.UtilisateurWithProfil;
import itu.p16.crypto.service.crypto.MvtCryptoService;
import itu.p16.crypto.service.user.UtilisateurService;
import itu.p16.crypto.service.views.AnalyseCommissionService;
import itu.p16.crypto.model.views.ViewMouvementCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyseUserService {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ViewMouvementCryptoService vmvtCryptoService;

    @Autowired
    private MvtCryptoService mvtCryptoService;



    public Map<UtilisateurWithProfil, Map<String, Double>> analyserUtilisateurPortefeuille(Timestamp dateMax) {
        List<UtilisateurWithProfil> utilisateurs = utilisateurService.getAllUtilisateursWithProfiles();
        
        Map<UtilisateurWithProfil, Map<String, Double>> resultatsAnalyse = new HashMap<>();
        
        for (UtilisateurWithProfil utilisateur : utilisateurs) {
            int idUser = utilisateur.getUser().getIdUser(); 
            
            List<ViewMouvementCrypto> mouvements = vmvtCryptoService.getMouvementsByIdUser(idUser);
            
            if (dateMax != null) {
                mouvements = mouvements.stream()
                        .filter(mvt -> mvt.getDateMvt().before(dateMax) || mvt.getDateMvt().equals(dateMax))
                        .collect(Collectors.toList());
            }

            double sommeMontantAchat = mouvements.stream()
                    .mapToDouble(mvt -> mvt.getMontantAchat() != null ? mvt.getMontantAchat() : 0)
                    .sum();

            double sommeMontantVente = mouvements.stream()
                    .mapToDouble(mvt -> mvt.getMontantVente() != null ? mvt.getMontantVente() : 0)
                    .sum();
            
            double valeurPortefeuille = mvtCryptoService.getValeurTotalePortefeuille(idUser, dateMax);

            Map<String, Double> analyseUtilisateur = new HashMap<>();
            analyseUtilisateur.put("sommeMontantAchat", Math.round(sommeMontantAchat * 100.0) / 100.0);
            analyseUtilisateur.put("sommeMontantVente", Math.round(sommeMontantVente * 100.0) / 100.0);
            analyseUtilisateur.put("valeurPortefeuille", Math.round(valeurPortefeuille * 100.0) / 100.0);
            
            resultatsAnalyse.put(utilisateur, analyseUtilisateur);
        }

        return resultatsAnalyse;
    }
}
