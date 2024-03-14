package TP2.agenda.agenda.services;

import java.util.List;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;

public interface AgendaServicesItf {
    public void add(Agenda agenda);

    public List<Agenda> getUserAgenda(Utilisateur utilisateur);
}
