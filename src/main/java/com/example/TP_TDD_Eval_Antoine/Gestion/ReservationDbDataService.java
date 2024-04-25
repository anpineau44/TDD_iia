package com.example.TP_TDD_Eval_Antoine.Gestion;

import java.util.List;

import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Livre;

public interface ReservationDbDataService {
    Reservation enregistrerReservationData(Reservation reservation);

    Reservation getReservationData(Livre livre);

    int RechercheNbreReservationAdherentData(Adherent adherent);

    void supprReservationData(Reservation reservation);

    List<Reservation> getReservationsOuvertData();

    List<Reservation> getReservationsAdherentData(Adherent adherent);

    List<Reservation> getToutesReservationsData();

    void envoyerEmailRappel(Adherent adherent, List<Reservation> listResa);

}
