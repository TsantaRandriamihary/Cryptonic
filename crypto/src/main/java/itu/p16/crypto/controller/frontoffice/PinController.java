package itu.p16.crypto.controller.frontoffice;

import java.lang.reflect.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import itu.p16.crypto.model.param.Parametre;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.connection.SignUpService;
import itu.p16.crypto.service.param.ParameterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pin")
public class PinController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private ParameterService parameterService;

    @GetMapping("/confirm")
    public String confirmPinPageSignUp(Model model) {
        model.addAttribute("action", "confirm");
        return "frontoffice/confirm-pin"; 
    }

    @GetMapping("/confirmer")
    public String confirmPinPageLogin(Model model) {
        model.addAttribute("action", "validate-pin");
        return "frontoffice/pin"; 
    }

    @PostMapping("/confirm")
    public String confirmPin(@RequestParam("pin") String pin,
                              HttpServletRequest request,
                              HttpSession session,
                              Model model) {
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");

        if (email == null || password == null) {
            model.addAttribute("errorMessage", "Les informations de session sont manquantes.");
            return "frontoffice/confirm-pin";
        }

        try {
            Parametre parametre = parameterService.getAllParametres().get(0);
            session.setMaxInactiveInterval(parametre.getDureeSession());
            Utilisateur response = signUpService.confirmPin(email, password, pin, session);
            session.setAttribute("email", null);
            session.setAttribute("password", null);
            session.setAttribute("user", response);
            model.addAttribute("successMessage", "PIN confirmé avec succès !");
            return "redirect:/userProfil"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "frontoffice/confirm-pin"; 
        }
    }
}
