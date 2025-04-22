package itu.p16.crypto.controller.backoffice;

import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.repository.crypto.CryptoRepository;
import itu.p16.crypto.service.crypto.MvtCryptoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backoffice/crypto")
public class CryptoBackController {

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private MvtCryptoService mvtCryptoService;

    @GetMapping("/list")
    public String getCryptoList(Model model) {
        List<Crypto> cryptos = cryptoRepository.findAll();
        model.addAttribute("cryptos", cryptos);
        return "backoffice/liste_crypto"; 
    }

    @GetMapping("/prices")
    @ResponseBody
    public List<Crypto> getUpdatedPrices() {
        return cryptoRepository.findAll(); 
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
