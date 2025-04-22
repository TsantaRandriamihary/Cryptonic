package itu.p16.crypto.controller.backoffice;


import itu.p16.crypto.model.transaction.DemandeTransaction;
import itu.p16.crypto.service.transaction.DemandeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backoffice/demandes")
public class DemandeTransactionController {

    @Autowired
    private DemandeTransactionService demandeTransactionService;

    /**
     * Afficher la liste des demandes de transaction non traitées.
     */
    @GetMapping
    public String listerDemandesNonTraitees(Model model) {
        List<DemandeTransaction> demandes = demandeTransactionService.getAllDemandesNonTraitees();
        model.addAttribute("demandes", demandes);
        return "backoffice/demandes";
    }

    /**
     * Accepter une demande de transaction.
     */
    @PostMapping("/accepter/{id}")
    public String accepterDemande(@PathVariable("id") int id) {
        try {
            demandeTransactionService.validerDemande(id);
        } catch (Exception e) {
            System.out.println("Erreur lors de la validation : " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/backoffice/demandes";
    }

    /**
     * Refuser une demande de transaction.
     */
    @PostMapping("/refuser/{id}")
    public String refuserDemande(@PathVariable("id") int id) {
        try {
            demandeTransactionService.refuserDemande(id);
        } catch (Exception e) {
            System.out.println("Erreur lors du refus : " + e.getMessage());
        }
        return "redirect:/backoffice/demandes";
    }
}
