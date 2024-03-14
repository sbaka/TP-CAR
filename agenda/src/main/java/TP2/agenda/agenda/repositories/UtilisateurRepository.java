package TP2.agenda.agenda.repositories;

import org.springframework.data.repository.CrudRepository;

import TP2.agenda.agenda.models.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
}