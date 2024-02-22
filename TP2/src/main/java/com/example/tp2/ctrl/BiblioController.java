package com.example.tp2.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tp2.service.*;
import com.example.tp2.model.Auteur;

@Controller
@RequestMapping("bib")
public class BiblioController {
    @Autowired
    AuteurServiceItf service;

    @PostMapping("/init")
    public String init() {
        service.init();
        return "redirect:/bib/home";
    }

    @PostMapping("/add")
    public String add(@RequestParam String nom, @RequestParam String prenom) {
        service.add(nom, prenom);
        return "redirect:/bib/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Iterable<Auteur> auteurs = service.findAllAuteur();
        model.addAttribute("auteurs", auteurs);
        return "home";
    }
}
