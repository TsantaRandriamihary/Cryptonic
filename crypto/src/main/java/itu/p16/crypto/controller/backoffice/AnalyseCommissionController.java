package itu.p16.crypto.controller.backoffice;

import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.service.views.AnalyseCommissionService;
import itu.p16.crypto.repository.crypto.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backoffice/analysecommission")
public class AnalyseCommissionController {

    @Autowired
    private AnalyseCommissionService analyseCommissionService;

    @Autowired
    private CryptoRepository cryptoRepository;

    /**
     * Affichage initial de la page d'analyse des commissions
     */
    @GetMapping
    public String afficherAnalyseCommission(Model model) {
        List<Crypto> cryptos = cryptoRepository.findAll();
        model.addAttribute("analyses", null);
        model.addAttribute("cryptos", cryptos);
        return "backoffice/graph_commission";
    }

    /**
     * Filtrage des analyses de commissions en fonction des paramètres fournis.
     */
    @PostMapping("/filtrer")
    public String filtrerAnalyse(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer idCrypto,
            @RequestParam String analyseType,
            Model model) {

        // Convertir les LocalDateTime en Timestamp
        Timestamp startTimestamp = (startDate != null) ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = (endDate != null) ? Timestamp.valueOf(endDate) : null;

        // Récupérer les mouvements filtrés et analyser les commissions
        Map<String, List<itu.p16.crypto.model.views.ViewMouvementCrypto>> resultatsAnalyse =
                analyseCommissionService.analyserCommissions(startTimestamp, endTimestamp, idCrypto);

        Map<String, Object> analyses =
                analyseCommissionService.analyserCommissionsParType(resultatsAnalyse, analyseType);

        // Ajouter les attributs pour affichage dans la vue
        model.addAttribute("analyses", analyses);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("analyseType", analyseType);
        model.addAttribute("selectedCrypto", idCrypto);
        model.addAttribute("cryptos", cryptoRepository.findAll());

        return "backoffice/graph_commission";
    }
}
