package TP2.agenda.agenda.services;

import java.util.List;

import TP2.agenda.agenda.models.Evenement;

public interface EvenementServicesItf {
    public List<Evenement> getAgendasEvents(Long agendaId);
}
