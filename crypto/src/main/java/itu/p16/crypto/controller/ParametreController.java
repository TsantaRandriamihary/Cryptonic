package itu.p16.crypto.controller;

import itu.p16.crypto.service.param.ParameterService;
import jakarta.servlet.http.HttpSession;
import itu.p16.crypto.model.genre.Genre;
import itu.p16.crypto.model.param.Parametre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parametre")
public class ParametreController {

    @Autowired
    private ParameterService parametreService;

    @GetMapping
    public String parametrePage(Model model) {
        try {
            return "parametre"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "parametre"; 
        }
    }

    @GetMapping("/edit")
    public String editParametre(HttpSession session, Model model) {
        try {
            Parametre parametre = parametreService.getAllParametres().get(0);
            if (parametre != null) {
                model.addAttribute("parametre", parametre);
                return "parametre";
            }
            model.addAttribute("errorMessage", "Pas de parametre dans la base.");
            return "parametre";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "parametre";
        }
    }

    @PostMapping("/update")
    public String updateProfil(HttpSession session,
                               @RequestParam("nbrTentative") int nbrTentative,
                               @RequestParam("dureeTentative") int dureeTentative,
                               @RequestParam("dureeSession") int dureeSession,
                               @RequestParam("dureePin") int dureePin,
                               Model model) {
        try {
            Parametre parametre = parametreService.getAllParametres().get(0);
            parametre.setDureePin(dureePin);
            parametre.setDureeSession(dureeSession);
            parametre.setDureeTentative(dureeTentative);
            parametre.setNbrTentative(nbrTentative);
            return "redirect:/crypto/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "parametre";
        }
    }
}
