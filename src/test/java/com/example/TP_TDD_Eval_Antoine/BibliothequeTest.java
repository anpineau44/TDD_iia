package com.example.TP_TDD_Eval_Antoine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Bibliotheque;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Format;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.ISBNValidator;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.Livre;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.LivreDbDataService;
import com.example.TP_TDD_Eval_Antoine.Bibliotheque.LivreWebDataService;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidIsbnEmptyException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidIsbnException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideAuteurException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideTitreException;
import com.example.TP_TDD_Eval_Antoine.Exception.LivreDejaExistantException;

public class BibliothequeTest {

        LivreDbDataService mockDbService;
        LivreWebDataService mockWebService;
        ISBNValidator mockIsbnValidator;

        Bibliotheque bibliotheque;

        @BeforeEach
        public void initMocks() {
                mockDbService = mock(LivreDbDataService.class);

                mockWebService = mock(LivreWebDataService.class);

                mockIsbnValidator = mock(ISBNValidator.class);

                bibliotheque = new Bibliotheque();
                bibliotheque.setDatabaseLivreService(mockDbService);
                bibliotheque.setWebLivreService(mockWebService);
                bibliotheque.setISBNValidator(mockIsbnValidator);
        }

        @Test
        public void creationLivreOk()
                        throws InvalidIsbnEmptyException, LivreDejaExistantException, InvalidIsbnException {
                String isbn = "2100852345";
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);

                Livre livre = bibliotheque.creationLivre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                "antoine", true, Format.GRAND_FORMAT);
                verify(mockDbService).enregistrerLivreData(livre);
                verifyNoInteractions(mockWebService);
        }

        @Test
        public void creationLivreOkInfoManquant()
                        throws InvalidIsbnEmptyException, LivreDejaExistantException, InvalidIsbnException {
                String isbn = "2100852345";
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);
                when(mockWebService.getLivreData(isbn))
                                .thenReturn(new Livre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));

                Livre livre = bibliotheque.creationLivre(isbn, null, "",
                                "", true, Format.GRAND_FORMAT);

                assertEquals(livre.getTitre(), "Concours Assistant medico-administratif");
                assertEquals(livre.getAuteur(), "Poppe");
                assertEquals(livre.getEditeur(), "antoine");
                assertEquals(livre.getDisponible(), true);
                assertEquals(livre.getFormat(), Format.GRAND_FORMAT);
        }

        @Test
        public void creationLivreSansISBN() throws InvalidIsbnEmptyException, LivreDejaExistantException {
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN("")).thenReturn(true);

                assertThrows(InvalidIsbnEmptyException.class,
                                () -> bibliotheque.creationLivre("", "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));
                assertThrows(InvalidIsbnEmptyException.class,
                                () -> bibliotheque.creationLivre(null, "Concours Assistant medico-administratif",
                                                "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));
        }

        @Test
        public void creationLivreISBNnonValide() throws InvalidIsbnEmptyException, LivreDejaExistantException {
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN("")).thenReturn(false);

                assertThrows(InvalidIsbnException.class,
                                () -> bibliotheque.creationLivre("2100852345",
                                                "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));
        }

        @Test
        public void creationLivreISBExisteDejaEnBDD() throws InvalidIsbnEmptyException, LivreDejaExistantException {
                String isbn = "2100852345";
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);
                when(mockDbService.getLivreData(isbn))
                                .thenReturn(new Livre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));

                assertThrows(LivreDejaExistantException.class,
                                () -> bibliotheque.creationLivre(isbn, "Concours Assistant medico-administratif",
                                                "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));
        }

        @Test
        public void ModifLivre() throws InvalidIsbnEmptyException {
                String isbn = "2100852345";
                when(mockDbService.enregistrerLivreData(any(Livre.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);
                when(mockWebService.getLivreData(isbn))
                                .thenReturn(new Livre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));

                Livre livre = bibliotheque.modifierLivre(new Livre(isbn, null, "pineau",
                                "", false, Format.BROCHE));

                assertEquals(livre.getTitre(), "Concours Assistant medico-administratif");
                assertEquals(livre.getAuteur(), "pineau");
                assertEquals(livre.getEditeur(), "antoine");
                assertEquals(livre.getDisponible(), false);
                assertEquals(livre.getFormat(), Format.BROCHE);
        }

        @Test
        public void SupprLivre() throws InvalidIsbnEmptyException, InvalidIsbnException {
                String isbn = "2100852345";
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);
                when(mockWebService.getLivreData(isbn))
                                .thenReturn(new Livre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));

                bibliotheque.supprimerLivre(isbn);
                verify(mockDbService).SupprimerLivreData(isbn);
        }

        @Test
        public void rechercheLivreParIsbn() throws InvalidIsbnEmptyException {
                String isbn = "2100852345";
                when(mockIsbnValidator.validateISBN(isbn)).thenReturn(true);
                when(mockDbService.RechercheParIsbnLivreData(isbn))
                                .thenReturn(new Livre(isbn, "Concours Assistant medico-administratif", "Poppe",
                                                "antoine", true, Format.GRAND_FORMAT));

                Livre livre = bibliotheque.RechercheLivreParIsnb(isbn);
                assertEquals(livre.getTitre(), "Concours Assistant medico-administratif");
                assertEquals(livre.getAuteur(), "Poppe");
                assertEquals(livre.getEditeur(), "antoine");
                assertEquals(livre.getDisponible(), true);
                assertEquals(livre.getFormat(), Format.GRAND_FORMAT);
        }

        @Test
        public void rechercheLivreParTitre() throws InvalidIsbnEmptyException, InvalideTitreException {
                String isbn1 = "2100852485";
                String isbn2 = "2100852345";
                String titre = "Concours Assistant medico-administratif";
                when(mockDbService.RechercheParTitreLivreData(titre))
                                .thenReturn(Arrays.asList(new Livre(isbn1, titre, "Momo",
                                                "antoine", false, Format.BROCHE),
                                                new Livre(isbn2, titre, "Poppe",
                                                                "florian", true, Format.GRAND_FORMAT)));

                List<Livre> livres = bibliotheque.RechercheLivreParTitre(titre);
                for (Livre livre : livres) {
                        assertEquals(livre.getTitre(), titre);
                }

                assertEquals(isbn1, livres.get(0).getIsbn());
                assertEquals(isbn2, livres.get(1).getIsbn());
        }

        @Test
        public void rechercheLivreParAuteur() throws InvalidIsbnEmptyException, InvalideAuteurException {
                String isbn1 = "2100852485";
                String isbn2 = "2100852345";
                String auteur = "Poppe";
                when(mockDbService.RechercheParAuteurLivreData(auteur))
                                .thenReturn(Arrays.asList(new Livre(isbn1, "Le lion", auteur,
                                                "antoine", false, Format.BROCHE),
                                                new Livre(isbn2, "Le loup", auteur,
                                                                "florian", true, Format.GRAND_FORMAT)));

                List<Livre> livres = bibliotheque.RechercheLivreParAuteur(auteur);
                for (Livre livre : livres) {
                        assertEquals(livre.getAuteur(), auteur);
                }

                assertEquals(isbn1, livres.get(0).getIsbn());
                assertEquals(isbn2, livres.get(1).getIsbn());
        }
}
