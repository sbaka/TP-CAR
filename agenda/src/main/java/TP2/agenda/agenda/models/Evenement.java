package TP2.agenda.agenda.models;

import jakarta.persistence.*;

/**
 * Evenements
 */
@Entity
public class Evenement {
    @Id
    @GeneratedValue
    private Long id;
    private String libelle;
    private String date;
    private String heurDebut;
    private String heurFin;

    @ManyToOne
    private Agenda agenda;

    public Evenement() {
    }

    public Evenement(String libelle, String date, String heurDebut, String heurFin, Agenda agenda) {
        this.libelle = libelle;
        this.date = date;
        this.heurDebut = heurDebut;
        this.heurFin = heurFin;
        this.agenda = agenda;
    }

    public Long getId() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeurFin() {
        return heurFin;
    }

    public void setHeurFin(String heurFin) {
        this.heurFin = heurFin;
    }

    public String getHeurDebut() {
        return heurDebut;
    }

    public void setHeurDebut(String heurDebut) {
        this.heurDebut = heurDebut;
    }
}
