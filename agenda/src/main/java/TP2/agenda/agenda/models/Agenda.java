package TP2.agenda.agenda.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Agendas
 */
@Entity
public class Agenda {
    @Id
    @GeneratedValue
    private long id;
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "agenda", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Evenement> evenements = new ArrayList<>();

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // public List<Evenement> getEvenements() {
    // return evenements;
    // }

    // public void setEvenements(List<Evenement> evenements) {
    // this.evenements = evenements;
    // }
}
