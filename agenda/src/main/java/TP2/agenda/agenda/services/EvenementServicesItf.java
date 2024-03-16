package TP2.agenda.agenda.services;

import java.util.List;

import TP2.agenda.agenda.models.Evenement;

public interface EvenementServicesItf {
    public void add(Evenement evenement);

    public void remove(Long id);

    public List<Evenement> getAgendasEvents(Long agendaId);
}
