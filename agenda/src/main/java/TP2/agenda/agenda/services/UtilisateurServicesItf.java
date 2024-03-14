package TP2.agenda.agenda.services;

import TP2.agenda.agenda.models.Utilisateur;

public interface UtilisateurServicesItf {
    public void add(Utilisateur utilisateur);

    public Utilisateur estUnUtilisateur(String email);

}
