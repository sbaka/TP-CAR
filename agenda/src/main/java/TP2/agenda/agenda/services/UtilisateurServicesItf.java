package TP2.agenda.agenda.services;

import java.util.List;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;

public interface UtilisateurServicesItf {
    public void add(Utilisateur utilisateur);

    public Utilisateur estUnUtilisateur(String email);

    public List<Agenda> findAllAgenda();

}
