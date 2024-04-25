package com.example.TP_TDD_Eval_Antoine.Gestion;

import java.sql.Date;

import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Livre;

public class Reservation {
    private Adherent adherent;
    private Date dateDeReservation;
    private Livre livre;

    public Reservation(Adherent adherent, Date dateDeReservation, Livre livre) {
        this.adherent = adherent;
        this.dateDeReservation = dateDeReservation;
        this.livre = livre;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public Date getDateDeReservation() {
        return dateDeReservation;
    }

    public void setDateDeReservation(Date dateDeReservation) {
        this.dateDeReservation = dateDeReservation;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }
}
