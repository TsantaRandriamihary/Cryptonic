package itu.p16.crypto.controller;

import itu.p16.crypto.model.param.Parametre;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.connection.LoginService;
import itu.p16.crypto.service.param.ParameterService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ParameterService parameterService;

    @GetMapping
    public String loginPage(Model model) {
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            Utilisateur response = loginService.loginUser(email, password);
            Parametre parametre = parameterService.getAllParametres().get(0);
            session.setMaxInactiveInterval(parametre.getDureeSession());
            session.setAttribute("user", response);
            return "redirect:/crypto/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la connexion : " + e.getMessage());
            return "login";
        }
    }

    
    @PostMapping("/validate-pin")
    public String validatePin(@RequestParam("pin") String pin,
                              HttpSession session,
                              Model model) {
        try {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            if (user == null) {
                throw new Exception("Vous devez vous connecter.");
            }
            Utilisateur response = loginService.validatePin(user.getEmail(), pin);
            Parametre parametre = parameterService.getAllParametres().get(0);
            session.setMaxInactiveInterval(parametre.getDureeSession());
            session.setAttribute("user", response);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la validation du PIN : " + e.getMessage());
            model.addAttribute("action", "validate-pin");
            return "confirm-pin"; 
        }
    }
}
