package TP2.agenda.agenda.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import TP2.agenda.agenda.models.Agenda;
import TP2.agenda.agenda.models.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);

    List<Agenda> findAllAgenda();
}