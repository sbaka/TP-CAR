package TP2.agenda.agenda.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;

public interface AgendaRepository extends CrudRepository<Agenda, Long> {
    // Find all agendas by a specific user
    public List<Agenda> findByUtilisateur(Utilisateur utilisateur);
}
