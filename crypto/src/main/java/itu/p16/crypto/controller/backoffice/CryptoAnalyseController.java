package itu.p16.crypto.controller.backoffice;


import itu.p16.crypto.service.views.ViewCryptoHistoriqueService;
import itu.p16.crypto.model.crypto.Crypto;
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
@RequestMapping("/backoffice/analysecrypto")
public class CryptoAnalyseController {

    @Autowired
    private ViewCryptoHistoriqueService historiqueService;

    @Autowired
    private CryptoRepository cryptoRepository;

    @GetMapping
    public String afficherAnalyseCrypto(Model model) {
        List<Crypto> cryptos = cryptoRepository.findAll();
        model.addAttribute("analyses", null);
        model.addAttribute("cryptos", cryptos);
        return "backoffice/analysecrypto";
    }

    @PostMapping("/filtrer")
    public String filtrerAnalyse(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam List<Integer> idCryptos,
            @RequestParam String analyseType,
            Model model) {

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        List<Map<String, Object>> analyses = historiqueService.filtrerAnalyse(startTimestamp, endTimestamp, idCryptos, analyseType);

        model.addAttribute("analyses", analyses);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("analyseType", analyseType);
        model.addAttribute("selectedCryptos", idCryptos);
        List<Crypto> cryptos = cryptoRepository.findAll();
        model.addAttribute("cryptos", cryptos);
        return "backoffice/analysecrypto";
    }

}
