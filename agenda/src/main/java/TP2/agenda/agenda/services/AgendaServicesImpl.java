package TP2.agenda.agenda.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.repositories.AgendaRepository;

@Service
public class AgendaServicesImpl implements AgendaServicesItf {
    @Autowired
    private AgendaRepository repo;

    @Override
    public void add(Agenda agenda) {
        if (agenda != null) {
            repo.save(agenda);
        }
    }

}
