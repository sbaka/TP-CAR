package TP2.agenda.agenda.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import TP2.agenda.agenda.models.Utilisateur;
import TP2.agenda.agenda.services.UtilisateurServicesItf;
import jakarta.servlet.http.HttpSession;

// UserController.java
@Controller
@RequestMapping("/agenda/user")
public class UserController {

    @Autowired
    UtilisateurServicesItf userService;

    /*
     * Post Requests
     */

    @PostMapping("/signup")
    public String signup(
            HttpSession session,
            Model model,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String pwd,
            @RequestParam String email) {

        // si l'utilisateur n'existe pas il faut le créer sinon dire qu'il existe
        // (adresse mail deja utiliser)
        if (userService.estUnUtilisateur(email) == null) {
            Utilisateur currentUser = new Utilisateur(nom, prenom, email, pwd);
            userService.add(currentUser);
            session.setAttribute("currentUser", currentUser);
            return "redirect:/agenda/home";
        } else {
            model.addAttribute("error", "L'adresse email est déjà utilisée");
            return "signup";
        }
    }

    @PostMapping("/signin")
    public String signin(
            HttpSession session,
            Model model,
            @RequestParam String pwd,
            @RequestParam String email) {

        // verifier si l'utilisateur a ete ajouter
        Utilisateur currentUser = userService.estUnUtilisateur(email);
        if (currentUser != null) {
            // check si c'est le bon email et mot de passe
            if (currentUser.getPwd().trim().equals(pwd.trim())) {
                session.setAttribute("currentUser", currentUser);
                return "redirect:/agenda/home";
            } else {
                model.addAttribute("error", "mauvaise combinaison email/mot de passe");
                return "signin";
            }
        } else {
            model.addAttribute("error", "L'adresse email n'est associer a aucun compte");
            return "signin";
        }
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/agenda/user/signin";
    }

    /*
     * Get Requests
     */

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(Model model) {
        return "signin";
    }
}