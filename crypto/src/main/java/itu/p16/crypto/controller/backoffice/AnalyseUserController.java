package itu.p16.crypto.controller.backoffice;


import itu.p16.crypto.model.user.UtilisateurWithProfil;
import itu.p16.crypto.service.views.AnalyseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/backoffice/analyseuser")
public class AnalyseUserController {

    @Autowired
    private AnalyseUserService analyseUserService;

    @GetMapping
    public String afficherAnalyseUtilisateur(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMax,
            Model model) {
            
            Timestamp timestampMax = (dateMax != null) ? Timestamp.valueOf(dateMax) : null;
           

        Map<UtilisateurWithProfil, Map<String, Double>> resultatsAnalyse =
                analyseUserService.analyserUtilisateurPortefeuille(timestampMax);

        model.addAttribute("resultatsAnalyse", resultatsAnalyse);
        model.addAttribute("dateMax", dateMax);

        return "backoffice/statistique_user";
    }
}
