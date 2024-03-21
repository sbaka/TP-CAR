package TP2.agenda.agenda.services;

import java.util.List;
import java.util.Optional;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;

public interface AgendaServicesItf {
    public void add(Agenda agenda);

    public void remove(Long id);

    public Optional<Agenda> getAgendaById(Long id);

    public List<Agenda> getUserAgenda(Utilisateur utilisateur);

}
