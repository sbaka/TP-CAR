package TP2.agenda.agenda.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;
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

    @Override
    public void remove(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }

    @Override
    public List<Agenda> getUserAgenda(Utilisateur utilisateur) {
        return repo.findByUtilisateur(utilisateur);
    }

    @SuppressWarnings("null")
    @Override
    public Optional<Agenda> getAgendaById(Long id) {
        return repo.findById(id);
    }

}
