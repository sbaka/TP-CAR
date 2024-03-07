package TP2.agenda.agenda.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Personnes
 */
@Entity
public class Utilisateur {

    private String nom;
    private String prenom;
    // pour dire que cette colonne est unique
    @Column(unique = true)
    private String email;
    private String pwd;

    private long id;

    @OneToMany
    private List<Agenda> agendas;

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Utilisateur(String nom, String prenom, String email, String pwd) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
    }

    public Utilisateur() {
    }
}