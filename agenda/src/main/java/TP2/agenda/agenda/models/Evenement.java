package TP2.agenda.agenda.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Evenements
 */
@Entity
public class Evenement {
    private long id;
    private String libelle;
    @ManyToOne
    private Agenda agenda;
    @OneToMany
    private List<Creneau> creneaux;

    public Evenement() {
    }

    public Evenement(String libelle) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
