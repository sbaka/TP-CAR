package TP2.agenda.agenda.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TP2.agenda.agenda.models.Utilisateur;
import TP2.agenda.agenda.repositories.UtilisateurRepository;

@Service
public class UtilisateurServicesImpl implements UtilisateurServicesItf {
    @Autowired
    private UtilisateurRepository repo;

    @Override
    public void add(Utilisateur utilisateur) {
        if (utilisateur != null) {
            repo.save(utilisateur);
        }
    }

    @Override
    public Utilisateur estUnUtilisateur(String email) {
        return repo.findByEmail(email);
    }

}
