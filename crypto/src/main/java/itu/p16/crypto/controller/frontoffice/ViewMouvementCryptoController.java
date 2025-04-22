package itu.p16.crypto.controller.frontoffice;


import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.model.views.ViewMouvementCrypto;
import itu.p16.crypto.service.views.ViewMouvementCryptoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewMouvementCryptoController {

    @Autowired
    private ViewMouvementCryptoService service;

    @GetMapping("/frontoffice/crypto/mouvements")
    public String getMouvements(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user == null) {
            model.addAttribute("errorMessage", "Veuillez-vous connecter.");
            return "frontoffice/login"; 
        }
        int idUser = user.getIdUser();
        List<ViewMouvementCrypto> mouvements = service.getMouvementsByIdUser(idUser);

        model.addAttribute("mouvements", mouvements);
        return "frontoffice/mvt_crypto"; 
    }
}
