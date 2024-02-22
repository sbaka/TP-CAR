package com.example.tp2.service;

import com.example.tp2.model.Auteur;

public interface AuteurServiceItf {
    public void add(String nom, String prenom);

    public Iterable<Auteur> findAllAuteur();

    public void init();
}
