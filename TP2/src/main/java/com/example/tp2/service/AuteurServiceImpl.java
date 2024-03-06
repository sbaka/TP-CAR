package com.example.tp2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tp2.model.Auteur;
import com.example.tp2.repositories.AuteurRepository;

@Service
public class AuteurServiceImpl implements AuteurServiceItf {
    @Autowired
    private AuteurRepository repo;

    public void add(String nom, String prenom) {
        repo.save(new Auteur(nom, prenom));
    }

    public void init() {
        repo.save(new Auteur("nom1", "prenom1"));
        repo.save(new Auteur("nom2", "prenom2"));
        repo.save(new Auteur("nom3", "prenom3"));

    }

    public Iterable<Auteur> findAllAuteur() {
        return repo.findAll();
    }

}
