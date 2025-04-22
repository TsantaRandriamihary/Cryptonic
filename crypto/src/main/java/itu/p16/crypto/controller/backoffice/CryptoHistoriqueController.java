package itu.p16.crypto.controller.backoffice;

import itu.p16.crypto.model.crypto.Crypto;
import itu.p16.crypto.model.views.ViewCryptoHistorique;
import itu.p16.crypto.service.crypto.CryptoHistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;



@Controller
public class CryptoHistoriqueController {

    @Autowired
    private CryptoHistoriqueService cryptoHistoriqueService;

    @GetMapping("/backoffice/crypto/graph")
    public String goToGraph(Model model) {
        return "backoffice/graph"; 
    } 

    
    @GetMapping("/backoffice/crypto/historique")
    public ResponseEntity<List<ViewCryptoHistorique>> getCryptoHistorique(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        List<ViewCryptoHistorique> historique;

        if (startDate != null && endDate != null) {
            Timestamp start = Timestamp.valueOf(startDate + " 00:00:00");
            Timestamp end = Timestamp.valueOf(endDate + " 23:59:59");
            historique = cryptoHistoriqueService.getHistoriqueEntreDates(start, end);
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp oneYearAgo = new Timestamp(now.getTime() - (365L * 24 * 60 * 60 * 1000));
            historique = cryptoHistoriqueService.getHistoriqueDepuisDate(oneYearAgo);
        }
        return ResponseEntity.ok(historique);
    }
}
