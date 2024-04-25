package com.example.TP_TDD_Eval_Antoine.Gestion;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Livre;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.LivreDbDataService;
import com.example.TP_TDD_Eval_Antoine.Exception.AdherentDejaExistantException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidAdherentEmptyException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidCodeAdherentEmptyException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidDateResaEmptyException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidLivreException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidNbreReservation;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidReservationEmpty;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideDateDepasse4Mois;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideLivreDisponible;

public class Gestion {
    private AdherentDbDataService databaseAdherentService;
    private ReservationDbDataService databaseReservationService;
    private LivreDbDataService databaseLivreService;
    @SuppressWarnings("deprecation")
    private Date dateActuelle = new Date(2024, 04, 26);

    public AdherentDbDataService getDatabaseAdherentService() {
        return databaseAdherentService;
    }

    public void setDatabaseAdherentService(AdherentDbDataService databaseAdherentService) {
        this.databaseAdherentService = databaseAdherentService;
    }

    public ReservationDbDataService getDatabaseReservationService() {
        return databaseReservationService;
    }

    public void setDatabaseReservationService(ReservationDbDataService databaseReservationService) {
        this.databaseReservationService = databaseReservationService;
    }

    public LivreDbDataService getDatabaseLivreService() {
        return databaseLivreService;
    }

    public void setDatabaseLivreService(LivreDbDataService databaseLivreService) {
        this.databaseLivreService = databaseLivreService;
    }

    public Date getDateActuelle() {
        return dateActuelle;
    }

    public void setDateActuelle(Date dateActuelle) {
        this.dateActuelle = dateActuelle;
    }

    public Adherent creationAdherent(String codeAdherent, String nom, String prenom, Date dateNaissance,
            String civilite)
            throws InvalidCodeAdherentEmptyException, AdherentDejaExistantException {
        if (codeAdherent == null || codeAdherent == "") {
            throw new InvalidCodeAdherentEmptyException("CodeAdherent est vide.");
        }

        if (this.databaseAdherentService.getAdherentData(codeAdherent) != null) {
            throw new AdherentDejaExistantException("Le CodeAdherent existe deja.");
        }

        Adherent adherent = new Adherent(codeAdherent, nom, prenom, dateNaissance, civilite);

        return this.databaseAdherentService.enregistrerAdherentData(adherent);
    }

    public Adherent recupAdherentParCode(String codeAdherent)
            throws InvalidCodeAdherentEmptyException {
        if (codeAdherent == null || codeAdherent == "") {
            throw new InvalidCodeAdherentEmptyException("CodeAdherent est vide.");
        }

        return this.databaseAdherentService.getAdherentData(codeAdherent);
    }

    public Reservation creationReservation(Adherent adherent, Date dateDeReservation, Livre livre)
            throws InvalidAdherentEmptyException, InvalidLivreException, InvalideDateDepasse4Mois,
            InvalidNbreReservation, InvalidDateResaEmptyException, InvalideLivreDisponible {
        if (this.databaseAdherentService.getAdherentData(adherent.getCodeAdherent()) == null) {
            throw new InvalidAdherentEmptyException("L'adherent existe pas.");
        }

        if (this.databaseLivreService.getLivreData(livre.getIsbn()) == null) {
            throw new InvalidLivreException("Livre existe pas.");
        }

        if (dateDeReservation == null) {
            throw new InvalidDateResaEmptyException("Date de Reservation non renseigné");
        }

        if (!livre.getDisponible()) {
            throw new InvalideLivreDisponible("Livre non disponible");
        }

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(this.dateActuelle);
        calendar2.setTime(dateDeReservation);
        calendar1.add(Calendar.MONTH, 4);

        if (calendar2.after(calendar1)) {
            throw new InvalideDateDepasse4Mois("Date de reservation depasse 4 mois");
        }

        if (this.databaseReservationService.RechercheNbreReservationAdherentData(adherent) >= 4) {
            throw new InvalidNbreReservation("Nombre de reservation maximum atteint");
        }

        Reservation reservation = new Reservation(adherent, dateDeReservation, livre);

        return this.databaseReservationService.enregistrerReservationData(reservation);
    }

    public void finReservation(Reservation reservation) throws InvalidReservationEmpty {
        if (this.databaseReservationService.getReservationData(reservation.getLivre()) == null) {
            throw new InvalidReservationEmpty("La reservation n'existe pas.");
        }

        this.databaseReservationService.supprReservationData(reservation);
    }

    public List<Reservation> getReservationOuvert() {
        return this.databaseReservationService.getReservationsOuvertData();
    }

    public List<Reservation> getReservationHistorique(Adherent adherent) throws InvalidAdherentEmptyException {
        if (this.databaseAdherentService.getAdherentData(adherent.getCodeAdherent()) == null) {
            throw new InvalidAdherentEmptyException("L'adherent existe pas.");
        }

        return this.databaseReservationService.getReservationsAdherentData(adherent);
    }

    public void RappelReservationDepasse() {
        List<Reservation> reservations = this.databaseReservationService.getToutesReservationsData();

        List<Adherent> adhérentsAvecReservationsDepassees = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(this.dateActuelle);
            calendar2.setTime(reservation.getDateDeReservation());
            calendar1.add(Calendar.MONTH, 4);

            if (calendar2.after(calendar1)) {
                if (!adhérentsAvecReservationsDepassees.contains(reservation.getAdherent())) {
                    adhérentsAvecReservationsDepassees.add(reservation.getAdherent());
                }
            }
        }

        for (Adherent adherent : adhérentsAvecReservationsDepassees) {
            List<Reservation> reservationsDepassees = new ArrayList<>();
            for (Reservation reservation : reservations) {
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar1.setTime(this.dateActuelle);
                calendar2.setTime(reservation.getDateDeReservation());
                calendar1.add(Calendar.MONTH, 4);
                if (reservation.getAdherent().equals(adherent)
                        && calendar2.after(calendar1)) {
                    reservationsDepassees.add(reservation);
                }
            }
            this.databaseReservationService.envoyerEmailRappel(adherent, reservationsDepassees);
        }
    }
}
