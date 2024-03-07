package TP2.agenda.agenda.repositories;

import org.springframework.data.repository.CrudRepository;

import TP2.agenda.agenda.models.Agenda;

public interface AgendaRepository extends CrudRepository<Agenda, Long> {
}
