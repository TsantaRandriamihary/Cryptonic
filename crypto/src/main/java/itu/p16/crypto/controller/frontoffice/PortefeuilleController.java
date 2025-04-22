package itu.p16.crypto.controller.frontoffice;


import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.model.views.ViewPortefeuille;
import itu.p16.crypto.service.views.ViewPortefeuilleService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PortefeuilleController {
    @Autowired
    private  ViewPortefeuilleService portefeuilleService;

    @Autowired
    public PortefeuilleController(ViewPortefeuilleService portefeuilleService) {
        this.portefeuilleService = portefeuilleService;
    }

    @GetMapping("/frontoffice/portefeuille")
    public String afficherPortefeuille(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user == null) {
            model.addAttribute("errorMessage", "Veuillez-vous connecter.");
            return "frontoffice/login"; 
        }
        int idUser = user.getIdUser();
        List<ViewPortefeuille> portefeuille = portefeuilleService.getPortefeuilleByUser(idUser);
        model.addAttribute("portefeuille", portefeuille);
        return "frontoffice/portefeuille";  
    }

    @GetMapping("/frontoffice/portefeuille/data")
    @ResponseBody
    public Iterable<ViewPortefeuille> getPortefeuilleData(HttpSession session) {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user != null) {
            int idUser = user.getIdUser();
            List<ViewPortefeuille> portefeuilles = portefeuilleService.getPortefeuilleByUser(idUser);
            for (ViewPortefeuille portfolio : portefeuilles) {
                System.out.println(portfolio.getCryptoName() + " " + portfolio.getQuantiteActuelle());
            }
            
            return portefeuilles;  
        }
        return null;
    }
}
