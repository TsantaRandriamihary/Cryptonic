package itu.p16.crypto.controller.frontoffice;


import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.model.user.UtilisateurWithProfil;
import itu.p16.crypto.model.views.ViewMouvementCrypto;
import itu.p16.crypto.service.crypto.CryptoService;
import itu.p16.crypto.service.user.UtilisateurService;
import itu.p16.crypto.service.views.HistoriqueOperationService;
import itu.p16.crypto.service.views.ViewMouvementCryptoService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HistoriqueOperationController {

    private final HistoriqueOperationService historiqueOperationService;
    private final UtilisateurService utilisateurService;
    private final CryptoService cryptoService;

    public HistoriqueOperationController(HistoriqueOperationService historiqueOperationService,
                                         UtilisateurService utilisateurService,
                                         CryptoService cryptoService) {
        this.historiqueOperationService = historiqueOperationService;
        this.utilisateurService = utilisateurService;
        this.cryptoService = cryptoService;
    }

    @GetMapping("/frontoffice/historique")
    public String afficherHistorique(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateHeure,
            @RequestParam(required = false) Integer idCrypto,
            @RequestParam(required = false) Integer idUser,
            Model model) {

        Timestamp dateTimestamp = null;
        if (dateHeure != null) {
            dateTimestamp = Timestamp.valueOf(dateHeure); 
        }

        List<UtilisateurWithProfil> utilisateursAvecProfils = utilisateurService.getAllUtilisateursWithProfiles(); 
        List<Crypto> cryptos = cryptoService.getAll(); 
        List<UtilisateurWithProfil> utilisateursFiltres = historiqueOperationService.filterUsersWithCryptoMovements(dateTimestamp, idCrypto, idUser);

        model.addAttribute("utilisateurs", utilisateursFiltres);
        model.addAttribute("allUtilisateurs", utilisateursAvecProfils);
        model.addAttribute("cryptos", cryptos);
        model.addAttribute("selectedDate", dateHeure);
        model.addAttribute("selectedCrypto", idCrypto);
        model.addAttribute("selectedUser", idUser);

        return "frontoffice/historique"; 
    }
}
