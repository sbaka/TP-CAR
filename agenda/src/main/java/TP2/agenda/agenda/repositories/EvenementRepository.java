package TP2.agenda.agenda.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import TP2.agenda.agenda.models.Evenement;

public interface EvenementRepository extends CrudRepository<Evenement, Long> {
    public List<Evenement> findByAgendaId(Long agenda_id);
}