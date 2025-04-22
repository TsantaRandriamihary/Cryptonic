package itu.p16.crypto.controller.backoffice;


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
@RequestMapping("/backoffice/login")
public class LoginBackController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String loginPage(Model model) {
        return "backoffice/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try {
            Utilisateur response = loginService.loginUser(email, password);
            if(response != null && response.getIdRole() != 2){
                System.out.println("Id user ve :"+response.getIdUser());
                System.out.println("Id etat ve :"+response.getIdEtat());
                System.out.println("Id role ve :"+response.getIdRole());
                throw new Exception("Cet utilisateur n'est pas administrateur et n'a donc pas de droit.");
            }
            session.setAttribute("user", response);
            return "redirect:/backoffice/crypto/list"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la connexion : " + e.getMessage());
            return "backoffice/login"; 
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
            return "redirect:/backoffice";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la validation du PIN : " + e.getMessage());
            model.addAttribute("action", "validate-pin");
            return "backoffice/confirm-pin"; 
        }
    }
}

