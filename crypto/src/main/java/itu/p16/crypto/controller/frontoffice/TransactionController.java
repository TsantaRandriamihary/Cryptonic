package itu.p16.crypto.controller.frontoffice;

import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.transaction.TransactionService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/frontoffice/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/depot")
    public String handleDepot(
            @RequestParam Double montantDepot,
            HttpSession session,
            Model model) {
        try {
            Utilisateur user = (Utilisateur)session.getAttribute("user");
            if (user == null) {
                model.addAttribute("errorMessage", "Veuillez-vous connecter.");
                return "frontoffice/login"; 
            }
            int idUser = user.getIdUser();
            String email = user.getEmail();
            transactionService.tenterTransaction(idUser, montantDepot, 0.0, email); 
            model.addAttribute("montant", montantDepot);
            model.addAttribute("type", "Depot");
            model.addAttribute("email", email);
            return "frontoffice/demandeTransaction"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "frontoffice/depot";
        }
    }

    @PostMapping("/retrait")
    public String handleRetrait(
            @RequestParam Double montantRetrait,
            HttpSession session,
            Model model) {
            try {
                Utilisateur user = (Utilisateur)session.getAttribute("user");
                int idUser = user.getIdUser();
                String email = user.getEmail();
                if (user == null) {
                    model.addAttribute("errorMessage", "Veuillez-vous connecter.");
                    return "frontoffice/login"; 
                }
                transactionService.tenterTransaction(idUser, 0.0, montantRetrait, email);
                model.addAttribute("montant", montantRetrait);
                model.addAttribute("type", "Retrait");
                model.addAttribute("email", email);
                return "frontoffice/demandeTransaction";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "frontoffice/retrait"; 
            }
    }

    
}
