package TP2.agenda.agenda.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class AgendaController {
    @Autowired
    UtilisateurServicesItf userService;

    @Autowired
    AgendaServicesItf agendaService;

    @Autowired
    EvenementServicesItf evenementService;

    @GetMapping("/")
    public String first(Model model, HttpSession session) {
        return "redirect:/agenda/home";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEvenement(HttpSession session, Model model, @PathVariable Long id) {

        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            agendaService.remove(id);
            List<Agenda> agendas = agendaService.getUserAgenda(currentUser);
            model.addAttribute("agendas", agendas);
            return "redirect:/agenda/home";
        } else {
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";
        }

    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser == null) {
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";
        } else {
            List<Agenda> agendas = agendaService.getUserAgenda(currentUser);
            // ajouter le nom d'utilisateur dans le username
            model.addAttribute("username", currentUser.getNom());
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
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";

        }
    }

    @GetMapping("/details/{id}")
    public String detailAgendaById(HttpSession session, Model model, @PathVariable Long id) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            Optional<Agenda> agenda = agendaService.getAgendaById(id);
            if (agenda.isPresent()) {
                model.addAttribute("currentAgenda", agenda.get());
                session.setAttribute("currentAgenda", agenda.get());
                List<Evenement> events = evenementService.getAgendasEvents(id);
                model.addAttribute("events", events);
                return "agenda_details";
            } else {
                model.addAttribute("error", "Erreur avec Agenda");
                return "redirect:/agenda/home";
            }
        } else {
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";
        }
    }

    @PostMapping("/addEvent")
    public String addEvent(HttpSession session,
            Model model,
            @RequestParam String libelle,
            @RequestParam String date,
            @RequestParam String heurDebut,
            @RequestParam String heurFin) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            Agenda currentAgenda = (Agenda) session.getAttribute("currentAgenda");
            if (currentAgenda != null) {
                evenementService.add(new Evenement(libelle, date, heurDebut, heurFin, currentAgenda));
                List<Evenement> events = evenementService.getAgendasEvents(currentAgenda.getId());
                model.addAttribute("events", events);
                return "redirect:/agenda/details/" + currentAgenda.getId();
            } else {
                // TODO: this should display an error before redirecting
                model.addAttribute("error", "Erreur avec Agenda");
                return "redirect:/agenda/home";
            }
        } else {
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";
        }
    }

    @GetMapping("{id}/imprimer")
    public String printAgenda(HttpSession session, Model model, @PathVariable Long id) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            Agenda currentAgenda = (Agenda) session.getAttribute("currentAgenda");
            List<Evenement> events = evenementService.getAgendasEvents(currentAgenda.getId());
            model.addAttribute("events", events);
            return "agenda_imprimer";
        } else {
            // TODO: this should display an error before redirecting
            model.addAttribute("error", "Vous devez être connecté pour accéder aux détails de l'agenda.");
            return "redirect:/agenda/user/signin";
        }
    }

}
