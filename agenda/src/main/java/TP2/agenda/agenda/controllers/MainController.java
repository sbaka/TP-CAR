package TP2.agenda.agenda.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Evenement;
import TP2.agenda.agenda.models.Utilisateur;
import TP2.agenda.agenda.services.AgendaServicesItf;
import TP2.agenda.agenda.services.EvenementServicesItf;
import TP2.agenda.agenda.services.UtilisateurServicesItf;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("agenda")
public class MainController {
    @Autowired
    UtilisateurServicesItf userService;

    @Autowired
    AgendaServicesItf agendaService;

    @Autowired
    EvenementServicesItf evenementService;

    @PostMapping("/signup")
    public String signup(
            HttpSession session,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String pwd,
            @RequestParam String email,
            Model model) {

        // verifier si l'utilisateur a ete ajouter
        if (userService.estUnUtilisateur(email) == null) {
            userService.add(new Utilisateur(prenom, nom, email, pwd));
            session.setAttribute("currentUser", userService.estUnUtilisateur(email));
            return "redirect:/agenda/home";
        } else {
            model.addAttribute("error", "L'adresse email est déjà utilisée");
            return "signup";
        }
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/agenda/signin";
    }

    @PostMapping("/signin")
    public String signin(
            HttpSession session,
            @RequestParam String pwd,
            @RequestParam String email,
            Model model) {

        // verifier si l'utilisateur a ete ajouter
        Utilisateur user = userService.estUnUtilisateur(email);
        if (user != null) {
            // check si c'est le bon email et mot de passe
            if (user.getPwd().trim().equals(pwd.trim())) {
                session.setAttribute("currentUser", userService.estUnUtilisateur(email));
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

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(Model model) {
        return "signin";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/agenda/signin";
        } else {
            List<Agenda> agendas = agendaService.getUserAgenda(currentUser);
            model.addAttribute("agendas", agendas);
            return "home";
        }
    }

    @PostMapping("/addagenda")
    public String addAgenda(HttpSession session, Model model, @RequestParam String libelle) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            agendaService.add(new Agenda(libelle, currentUser));
            return "redirect:/agenda/home";
        } else {
            model.addAttribute("error", "Problème avec utilisateur = null");
            return "home";
        }
    }

    @GetMapping("/details/{id}")
    public String detailAgendaById(HttpSession session, Model model, @PathVariable Long id) {
        Optional<Agenda> agenda = agendaService.getAgendaById(id);

        model.addAttribute("currentAgenda", agenda.get());
        List<Evenement> events = evenementService.getAgendasEvents(id);
        model.addAttribute("events", events);

        return "AgendaDetails";
    }
}
