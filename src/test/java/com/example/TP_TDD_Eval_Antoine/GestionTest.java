package com.example.TP_TDD_Eval_Antoine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Format;
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
import com.example.TP_TDD_Eval_Antoine.Gestion.Adherent;
import com.example.TP_TDD_Eval_Antoine.Gestion.AdherentDbDataService;
import com.example.TP_TDD_Eval_Antoine.Gestion.Gestion;
import com.example.TP_TDD_Eval_Antoine.Gestion.Reservation;
import com.example.TP_TDD_Eval_Antoine.Gestion.ReservationDbDataService;

public class GestionTest {

        AdherentDbDataService mockDbAdherentService;
        ReservationDbDataService mockDbReservationService;
        LivreDbDataService mockDbLivreService;

        Gestion reservation;

        @BeforeEach
        public void initMocks() {
                mockDbAdherentService = mock(AdherentDbDataService.class);
                mockDbReservationService = mock(ReservationDbDataService.class);
                mockDbLivreService = mock(LivreDbDataService.class);

                reservation = new Gestion();
                reservation.setDatabaseAdherentService(mockDbAdherentService);
                reservation.setDatabaseReservationService(mockDbReservationService);
                reservation.setDatabaseLivreService(mockDbLivreService);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void creationAdherent() throws InvalidCodeAdherentEmptyException, AdherentDejaExistantException {
                String codeAdherent = "156481561";
                when(mockDbAdherentService.getAdherentData(codeAdherent))
                                .thenReturn(null);
                when(mockDbAdherentService.enregistrerAdherentData(any(Adherent.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Adherent adherent = reservation.creationAdherent(codeAdherent, "antoine", "pineau",
                                new Date(2002, 04, 26), "Monsieur");
                verify(mockDbAdherentService).enregistrerAdherentData(adherent);
                verifyNoInteractions(mockDbReservationService);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void recupAdherentParcode() throws InvalidCodeAdherentEmptyException {
                String codeAdherent = "156481561";
                Adherent adherent = new Adherent(codeAdherent, "antoine", "pineau",
                                new Date(2002, 04, 26), "Monsieur");
                when(mockDbAdherentService.getAdherentData(codeAdherent))
                                .thenReturn(adherent);

                Adherent adherentRecup = reservation.recupAdherentParCode(codeAdherent);
                assertEquals(adherent, adherentRecup);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void creationReservation() throws InvalidAdherentEmptyException, InvalidLivreException,
                        InvalideDateDepasse4Mois, InvalidNbreReservation, InvalidDateResaEmptyException,
                        InvalideLivreDisponible {

                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date date = new Date(2024, 4, 28);
                Date dateResa = new Date(2024, 5, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);

                when(mockDbAdherentService.getAdherentData(adherent.getCodeAdherent()))
                                .thenReturn(adherent);
                when(mockDbLivreService.getLivreData(livre.getIsbn()))
                                .thenReturn(livre);
                when(mockDbReservationService.RechercheNbreReservationAdherentData(adherent))
                                .thenReturn(2);
                when(mockDbReservationService.enregistrerReservationData(any(Reservation.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Reservation reservation = this.reservation.creationReservation(adherent, dateResa, livre);
                verify(mockDbReservationService).enregistrerReservationData(reservation);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void creationReservationLivreNonDispo() {

                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date date = new Date(2024, 4, 28);
                Date dateResa = new Date(2024, 5, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", false, Format.GRAND_FORMAT);

                when(mockDbAdherentService.getAdherentData(adherent.getCodeAdherent()))
                                .thenReturn(adherent);
                when(mockDbLivreService.getLivreData(livre.getIsbn()))
                                .thenReturn(livre);
                when(mockDbReservationService.RechercheNbreReservationAdherentData(adherent))
                                .thenReturn(2);
                when(mockDbReservationService.enregistrerReservationData(any(Reservation.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                assertThrows(InvalideLivreDisponible.class,
                                () -> this.reservation.creationReservation(adherent, dateResa, livre));
        }

        @SuppressWarnings("deprecation")
        @Test
        public void creationLimiteNbreReservations() throws InvalidAdherentEmptyException, InvalidLivreException,
                        InvalideDateDepasse4Mois, InvalidNbreReservation, InvalidDateResaEmptyException {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date date = new Date(2024, 4, 28);
                Date dateResa = new Date(2024, 5, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);

                when(mockDbAdherentService.getAdherentData(adherent.getCodeAdherent()))
                                .thenReturn(adherent);
                when(mockDbLivreService.getLivreData(livre.getIsbn()))
                                .thenReturn(livre);
                when(mockDbReservationService.RechercheNbreReservationAdherentData(adherent))
                                .thenReturn(4);
                when(mockDbReservationService.enregistrerReservationData(any(Reservation.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                assertThrows(InvalidNbreReservation.class,
                                () -> this.reservation.creationReservation(adherent, dateResa, livre));
        }

        @SuppressWarnings("deprecation")
        @Test
        public void creationLimiteDateReservation() throws InvalidAdherentEmptyException, InvalidLivreException,
                        InvalideDateDepasse4Mois, InvalidNbreReservation, InvalidDateResaEmptyException {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date date = new Date(2024, 4, 28);
                Date dateResa = new Date(2024, 9, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);

                when(mockDbAdherentService.getAdherentData(adherent.getCodeAdherent()))
                                .thenReturn(adherent);
                when(mockDbLivreService.getLivreData(livre.getIsbn()))
                                .thenReturn(livre);
                when(mockDbReservationService.RechercheNbreReservationAdherentData(adherent))
                                .thenReturn(1);
                when(mockDbReservationService.enregistrerReservationData(any(Reservation.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                assertThrows(InvalideDateDepasse4Mois.class,
                                () -> this.reservation.creationReservation(adherent, dateResa, livre));
        }

        @SuppressWarnings("deprecation")
        @Test
        public void FinDeReservation() throws InvalidReservationEmpty {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date dateResa = new Date(2024, 9, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);

                Reservation reservation = new Reservation(adherent, dateResa, livre);

                when(mockDbReservationService.getReservationData(livre))
                                .thenReturn(reservation);

                this.reservation.finReservation(reservation);
                verify(mockDbReservationService).supprReservationData(reservation);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void FinDeReservationQuiExistePas() throws InvalidReservationEmpty {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date dateResa = new Date(2024, 9, 28);
                Livre livre = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);

                Reservation reservation = new Reservation(adherent, dateResa, livre);

                assertThrows(InvalidReservationEmpty.class,
                                () -> this.reservation.finReservation(reservation));
        }

        @SuppressWarnings("deprecation")
        @Test
        public void RecupListeReservationOuvert() {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date dateResa = new Date(2024, 9, 28);
                Livre livre1 = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);
                Livre livre2 = new Livre("8119044510", "Scientologie", "Poppers",
                                "florian", true, Format.POCHE);

                Reservation reservation1 = new Reservation(adherent, dateResa, livre1);
                Reservation reservation2 = new Reservation(adherent, dateResa, livre2);

                when(mockDbReservationService.getReservationsOuvertData())
                                .thenReturn(Arrays.asList(reservation1, reservation2));

                List<Reservation> reservations = this.reservation.getReservationOuvert();
                assertEquals(reservations.get(0), reservation1);
                assertEquals(reservations.get(1), reservation2);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void HistoriqueReservationPourAdhrent() throws InvalidAdherentEmptyException {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date dateResa = new Date(2024, 9, 28);
                Livre livre1 = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);
                Livre livre2 = new Livre("8119044510", "Scientologie", "Poppers",
                                "florian", true, Format.POCHE);
                Livre livre3 = new Livre("5847544510", "Architecture", "Lili",
                                "alexandre", true, Format.BROCHE);

                Reservation reservation1 = new Reservation(adherent, dateResa, livre1);
                Reservation reservation2 = new Reservation(adherent, dateResa, livre2);
                Reservation reservation3 = new Reservation(adherent, dateResa, livre3);

                when(mockDbAdherentService.getAdherentData(adherent.getCodeAdherent()))
                                .thenReturn(adherent);
                when(mockDbReservationService.getReservationsAdherentData(adherent))
                                .thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

                List<Reservation> reservations = this.reservation.getReservationHistorique(adherent);
                assertEquals(reservations.get(0), reservation1);
                assertEquals(reservations.get(1), reservation2);
                assertEquals(reservations.get(2), reservation3);
        }

        @SuppressWarnings("deprecation")
        @Test
        public void EnvoieRappelReservationDepasse() {
                Adherent adherent = new Adherent("0506018402", "antoine", "pineau",
                                new Date(2002, 4, 26), "Monsieur");
                Date dateResa = new Date(2024, 9, 28);
                Livre livre1 = new Livre("8229044589", "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);
                Livre livre2 = new Livre("8119044510", "Scientologie", "Poppers",
                                "florian", true, Format.POCHE);
                Livre livre3 = new Livre("5847544510", "Architecture", "Lili",
                                "alexandre", true, Format.BROCHE);
                Reservation reservation1 = new Reservation(adherent, dateResa, livre1);
                Reservation reservation2 = new Reservation(adherent, dateResa, livre2);
                Reservation reservation3 = new Reservation(adherent, dateResa, livre3);

                when(mockDbReservationService.getToutesReservationsData())
                                .thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

                this.reservation.RappelReservationDepasse();
                verify(mockDbReservationService).envoyerEmailRappel(adherent,
                                Arrays.asList(reservation1, reservation2, reservation3));
        }
}
