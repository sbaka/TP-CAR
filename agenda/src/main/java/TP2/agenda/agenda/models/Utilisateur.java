package TP2.agenda.agenda.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "utilisateur", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Agenda> agenda = new ArrayList<>();

    public Utilisateur(String nom, String prenom, String email, String pwd) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
    }

    public Utilisateur() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Agenda> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Agenda> agendas) {
        this.agenda = agendas;
    }

}