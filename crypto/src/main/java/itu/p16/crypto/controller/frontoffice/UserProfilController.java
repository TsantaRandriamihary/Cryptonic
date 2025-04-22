package itu.p16.crypto.controller.frontoffice;

import itu.p16.crypto.model.genre.Genre;
import itu.p16.crypto.model.user.UserProfil;
import itu.p16.crypto.model.user.Utilisateur;
import itu.p16.crypto.service.cloud.CloudinaryService;
import itu.p16.crypto.service.genre.GenreService;
import itu.p16.crypto.service.user.UserProfilService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/userProfil")
public class UserProfilController {

     @Autowired
    private UserProfilService userProfilService;

    @Autowired
    private CloudinaryService cdnService;

    @Autowired
    private GenreService genreService;

    @GetMapping
    public String profilPage(Model model) {
        try {
            List<Genre> genres = genreService.getAllGenre();
            model.addAttribute("genres", genres); 
            return "profil"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "profil"; 
        }
    }

    @PostMapping("/profil")
    public String saveProfil(HttpSession session,
                            @RequestParam("nom") String nom, 
                            @RequestParam("prenom") String prenom, 
                            @RequestParam("image") MultipartFile image, 
                            @RequestParam("naissance") String naissance, 
                            @RequestParam("idGenre") int idGenre, 
                            Model model) {
        try {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            if (user == null) {
                model.addAttribute("errorMessage", "Vous devez vous connecter.");
                return "frontoffice/login";
            }

            String imageName = saveImage(image); 
            System.out.println("ILAY ANARANY VE : "+imageName);
            System.out.println("ILAY ANARANY VE : "+imageName); 
            System.out.println("ILAY ANARANY VE : "+imageName);
            System.out.println("ILAY ANARANY VE : "+imageName);
            System.out.println("ILAY ANARANY VE : "+imageName);
            System.out.println("ILAY ANARANY VE : "+imageName);
            System.out.println("ILAY ANARANY VE : "+imageName);
            UserProfil userProfil = new UserProfil();
            userProfil.setNom(nom);
            userProfil.setPrenom(prenom);
            userProfil.setImage(imageName);  
            System.out.println("ILAY ANARANY VwwwwE : "+userProfil.getImage());
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage()); 
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage());
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage());
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage());
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage());
            System.out.println("ILAY ANARANY VE : "+userProfil.getImage());
            userProfil.setNaissance(LocalDate.parse(naissance));
            userProfil.setIdGenre(idGenre);
            userProfil.setIdUser(user.getIdUser());
            userProfilService.createUserProfil(userProfil);  
            return "redirect:/frontoffice/crypto/list";  
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "profil"; 
        }
    }


    @GetMapping("/edit")
    public String editProfil(HttpSession session, Model model) {
        try {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            if (user == null) {
                model.addAttribute("errorMessage", "Vous devez vous connecter.");
                return "frontoffice/login";
            }
            UserProfil userProfil = new UserProfil();
            userProfil = (userProfilService.getUserProfilByIdUser(user.getIdUser())).get(0);
            if (userProfil != null) {
                model.addAttribute("userProfil", userProfil);
                model.addAttribute("genres", genreService.getAllGenre());
                return "frontoffice/modif_profil";
            }
            model.addAttribute("errorMessage", "Pas d'utilisateur avec cet iduser.");
            return "frontoffice/modif_profil";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "frontoffice/modif_profil";
        }
    }

    @PostMapping("/update")
    public String updateProfil(HttpSession session,
                               @RequestParam("nom") String nom,
                               @RequestParam("prenom") String prenom,
                               @RequestParam("image") MultipartFile image,
                               @RequestParam("naissance") String naissance,
                               @RequestParam("idGenre") int idGenre,
                               @RequestParam("currentImage") String currentImage,
                               Model model) {
        try {
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            if (user == null) {
                throw new Exception("Vous devez vous connecter.");
            }

         
            UserProfil userProfil = new UserProfil();
            userProfil = userProfilService.getUserProfilByIdUser(user.getIdUser()).get(0);
            if (image != null && !image.isEmpty()) {
                String newImageUrl = saveImage(image); 
                userProfil.setImage(newImageUrl);
            } else {
                userProfil.setImage(currentImage);
            }
            userProfil.setNom(nom);
            userProfil.setPrenom(prenom);
            userProfil.setNaissance(LocalDate.parse(naissance));
            userProfil.setIdGenre(idGenre);
            userProfilService.updateUserProfil(user.getIdUser(), userProfil);

            return "redirect:/frontoffice/crypto/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "frontoffice/modif_profil";
        }
    }



    private String saveImage(MultipartFile image) throws IOException {
        return cdnService.uploadImage(image);
    }
}
