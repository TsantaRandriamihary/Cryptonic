package itu.p16.crypto.controller;

import itu.p16.crypto.service.connection.LogoutService;
import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @GetMapping
    public String logout(HttpSession session,Model model) {
        try {
            String reponse = logoutService.logout(session);
            model.addAttribute("successMessage", reponse);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }
}
