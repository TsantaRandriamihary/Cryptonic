package itu.p16.crypto.controller.frontoffice;


import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.connection.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/frontoffice/login")
public class LoginFrontController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String loginPage(Model model) {
        return "frontoffice/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            Utilisateur response = loginService.loginUser(email, password);
            session.setAttribute("user", response);
            return "redirect:/frontoffice/crypto/list"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la connexion : " + e.getMessage());
            return "frontoffice/login"; 
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
            session.setAttribute("user", response);
            return "redirect:/frontoffice/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la validation du PIN : " + e.getMessage());
            model.addAttribute("action", "validate-pin");
            return "frontoffice/confirm-pin"; 
        }
    }
}

