package itu.p16.crypto.controller.fragment;

import itu.p16.crypto.model.user.UserProfil;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.user.UserProfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
public class HeaderController {

    private final UserProfilService userProfilService;

    public HeaderController(UserProfilService userProfilService) {
        this.userProfilService = userProfilService;
    }

    @GetMapping("/header/data")
    public ResponseEntity<?> getUserHeaderData(HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user != null) {
            List<UserProfil> profils = userProfilService.getUserProfilByIdUser(user.getIdUser());
            
            if (!profils.isEmpty()) {
                UserProfil userProfil = profils.get(0);
                
                return ResponseEntity.ok(userProfil);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
