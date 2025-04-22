package itu.p16.crypto.controller.frontoffice;

import itu.p16.crypto.model.commission.Commission;
import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.repository.crypto.CryptoRepository;
import itu.p16.crypto.service.commission.CommissionService;
import itu.p16.crypto.service.crypto.MvtCryptoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/frontoffice/crypto")
public class CryptoFrontController {

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private MvtCryptoService mvtCryptoService;

    @Autowired
    private CommissionService commissionService;


    @GetMapping("/list")
    public String getCryptoList(Model model) {
        List<Crypto> cryptos = cryptoRepository.findAll();
        model.addAttribute("cryptos", cryptos);
        return "frontoffice/liste_crypto"; 
    }

    @GetMapping("/prices")
    @ResponseBody
    public List<Crypto> getUpdatedPrices() {
        return cryptoRepository.findAll(); 
    }

    @PostMapping("/buy/{id}")
    public String buyCrypto(@PathVariable int id, Model model) {
        Crypto crypto = cryptoRepository.findById(id).orElse(null);
        if (crypto != null) {
            model.addAttribute("crypto", crypto);
            model.addAttribute("type", "achat");
            return "frontoffice/crypto-transaction";
        }
        return "redirect:/frontoffice/crypto/list";
    }

    @PostMapping("/sell/{id}")
    public String sellCrypto(@PathVariable int id, Model model) {
        Crypto crypto = cryptoRepository.findById(id).orElse(null);
        if (crypto != null) {
            model.addAttribute("crypto", crypto);
            model.addAttribute("type", "vente");
            return "frontoffice/crypto-transaction";
        }
        return "redirect:/frontoffice/crypto/list";
    }

    @PostMapping("/tenter")
    public String tenterTransaction(
            HttpSession session,
            @RequestParam("type") String type, 
            @RequestParam("idCrypto") Integer idCrypto,
            @RequestParam("quantite") Double quantite,
            @RequestParam("cryptoPrice") Double prixUnitaire,
            Model model) {
        try {
            Integer userId = getCurrentUserId(session); 
            String email = getCurrentUserEmail(session);
            Double montantTransaction = prixUnitaire;

            if ("achat".equalsIgnoreCase(type)) {
                mvtCryptoService.tenterAchat(userId, idCrypto, quantite, montantTransaction, email);
                model.addAttribute("montant", montantTransaction * quantite);
            } else if ("vente".equalsIgnoreCase(type)) {
                mvtCryptoService.tenterVente(userId, idCrypto, quantite, montantTransaction, email);
                Commission commission = commissionService.getCurrentCommission();
                if (commission != null) {
                    double pourcentVente = commission.getPourcentVente() / 100.0;
                    double montantTotal = montantTransaction * quantite;
                    double commissionMontant = montantTotal * pourcentVente;
                    double montantApresCommission = montantTotal - commissionMontant;
                    model.addAttribute("montant", montantApresCommission);
                }
            } else {
                throw new IllegalArgumentException("Type de transaction invalide.");
            }
            
            model.addAttribute("type", type);
            model.addAttribute("email", email);
            model.addAttribute("idCrypto", idCrypto);
            model.addAttribute("quantite", quantite);

            return "frontoffice/demandeTransaction";
        } catch (Exception e) {
            Crypto crypto = cryptoRepository.findById(idCrypto).orElse(null);
            model.addAttribute("crypto", crypto);
            model.addAttribute("type", type);
            model.addAttribute("errorMessage", e.getMessage());
            return "frontoffice/crypto-transaction"; 
        }
    }

    
    private Integer getCurrentUserId(HttpSession session) throws Exception {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user == null) {
            throw new Exception("Veuillez-vous connecter."); 
        }
        return user.getIdUser(); 
    }

    private String getCurrentUserEmail(HttpSession session) throws Exception {
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        if (user == null) {
            throw new Exception("Veuillez-vous connecter."); 
        }
        return user.getEmail(); 
    }
}
