package itu.p16.crypto.controller.frontoffice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import itu.p16.crypto.service.connection.SignUpService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private SignUpService signUpService; 

    @GetMapping
    public String signUpPage(Model model) {
        return "register"; 
    }

    @PostMapping
    public String signUp(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        try {
            String reponse = signUpService.sendSignUpRequest(email, password,session);
            model.addAttribute("successMessage", reponse);
            return "redirect:/pin/confirm";
        } catch (Exception e) {
            model.addAttribute("errorMessage",e.getMessage());
        }

        return "register"; 
    }
}
