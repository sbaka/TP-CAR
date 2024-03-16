package TP2.agenda.agenda.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TP2.agenda.agenda.models.Evenement;
import TP2.agenda.agenda.repositories.EvenementRepository;

@Service
public class EvenementServicesImpl implements EvenementServicesItf {
    @Autowired
    private EvenementRepository repo;

    @Override
    public List<Evenement> getAgendasEvents(Long agendaId) {
        return repo.findByAgendaId(agendaId);
    }

    @Override
    public void add(Evenement evenement) {
        if (evenement != null) {
            repo.save(evenement);
        }
    }

    @Override
    public void remove(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }

}
