package itu.p16.crypto.controller.frontoffice;

import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.model.views.ViewFonds;
import itu.p16.crypto.model.views.ViewTransactions;
import itu.p16.crypto.repository.views.ViewFondsRepository;
import itu.p16.crypto.repository.views.ViewTransactionsRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class FondsController {

    @Autowired
    private ViewFondsRepository fondsRepository;

    @Autowired
    private ViewTransactionsRepository transactionsRepository;

    @GetMapping("/frontoffice/fonds")
    public String showFonds(Model model,HttpSession session) {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user == null) {
            model.addAttribute("errorMessage", "Veuillez-vous connecter.");
            return "frontoffice/login"; 
        }
        int idUser = user.getIdUser();
        Optional<ViewFonds> fonds= fondsRepository.findFondActuelByUserId(idUser); 
        if (fonds.isPresent()) {
            model.addAttribute("fondActuel", fonds.get().getFondActuel());
        } else {
            model.addAttribute("fondActuel", 0); 
        }

        List<ViewTransactions> transactions = transactionsRepository.findByIdUser(idUser);  
        model.addAttribute("transactions", transactions);

        return "frontoffice/fonds"; 
    }

    @GetMapping("/frontoffice/depot")
    public String showDepotForm() {
        return "frontoffice/depot"; 
    }

    @GetMapping("/frontoffice/retrait")
    public String showRetraitForm() {
        return "frontoffice/retrait"; 
    }
}
