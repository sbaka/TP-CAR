package TP2.agenda.agenda.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Personnes
 */
@Entity
public class Creneau {
    private long id;
    private Date date;

    @ManyToOne
    private Evenement evenement;

    public Creneau() {
    }

    public Creneau(Date date) {
        this.date = date;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
