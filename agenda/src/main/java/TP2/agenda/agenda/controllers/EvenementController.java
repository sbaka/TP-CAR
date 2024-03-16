package TP2.agenda.agenda.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Evenement;
import TP2.agenda.agenda.models.Utilisateur;
import TP2.agenda.agenda.services.EvenementServicesItf;
import jakarta.servlet.http.HttpSession;

// UserController.java
@Controller
@RequestMapping("/agenda/{idAgenda}/evenement/")
public class EvenementController {
    @Autowired
    EvenementServicesItf evenementService;

    @DeleteMapping("/delete/{id}")
    public String deleteEvenement(HttpSession session, Model model, @PathVariable Long id) {

        Utilisateur currentUser = (Utilisateur) session.getAttribute("currentUser");
        if (currentUser != null) {
            Agenda currentAgenda = (Agenda) session.getAttribute("currentAgenda");
            if (currentAgenda != null) {
                evenementService.remove(id);
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
}
