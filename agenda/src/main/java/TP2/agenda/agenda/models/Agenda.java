package TP2.agenda.agenda.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Agendas
 */
@Entity
public class Agenda {
    private String libelle;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToMany
    private List<Evenement> evenements;

    public Agenda() {
    }

    public Agenda(String libelle, Utilisateur utilisateur) {
        this.libelle = libelle;
        this.utilisateur = utilisateur;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
