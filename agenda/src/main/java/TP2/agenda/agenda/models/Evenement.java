package TP2.agenda.agenda.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Evenements
 */
@Entity
public class Evenement {
    @Id
    @GeneratedValue
    private long id;
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @OneToMany(mappedBy = "evenement", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Creneau> creneaux = new ArrayList<>();

    public Evenement() {
    }

    public Evenement(String libelle) {
        this.libelle = libelle;
    }

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

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public List<Creneau> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }
}
