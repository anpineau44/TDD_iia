package com.example.TP_TDD_Eval_Antoine.Bibliotheque;

import java.util.List;

import com.example.TP_TDD_Eval_Antoine.Exception.InvalidIsbnEmptyException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalidIsbnException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideAuteurException;
import com.example.TP_TDD_Eval_Antoine.Exception.InvalideTitreException;
import com.example.TP_TDD_Eval_Antoine.Exception.LivreDejaExistantException;

public class Bibliotheque {

    private LivreDbDataService databaseLivreService;
    private LivreWebDataService webLivreService;
    private ISBNValidator isbnValidator;

    public LivreWebDataService getWebLivreService() {
        return webLivreService;
    }

    public void setWebLivreService(LivreWebDataService webLivreService) {
        this.webLivreService = webLivreService;
    }

    public LivreDbDataService getDatabaseLivreService() {
        return databaseLivreService;
    }

    public void setDatabaseLivreService(LivreDbDataService databaseLivreService) {
        this.databaseLivreService = databaseLivreService;
    }

    public void setISBNValidator(ISBNValidator isbnValidator) {
        this.isbnValidator = isbnValidator;
    }

    public Livre creationLivre(String isbn, String titre, String auteur, String editeur, boolean disponible,
            Format format) throws InvalidIsbnEmptyException, LivreDejaExistantException, InvalidIsbnException {
        if (isbn == null || isbn == "") {
            throw new InvalidIsbnEmptyException("L'ISBN est vide.");
        }

        if (!this.isbnValidator.validateISBN(isbn)) {
            throw new InvalidIsbnException("ISBN invalide");
        }

        if (this.databaseLivreService.getLivreData(isbn) != null) {
            throw new LivreDejaExistantException("Le livre existe deja.");
        }

        Livre livre;
        if (titre == null || titre == "" || auteur == null || auteur == "" || editeur == null || editeur == ""
                || format == null) {
            livre = this.webLivreService.getLivreData(isbn);
            if (titre != null && titre != "") {
                livre.setTitre(titre);
            }
            if (auteur != null && auteur != "") {
                livre.setAuteur(auteur);
            }
            if (editeur != null && editeur != "") {
                livre.setEditeur(editeur);
            }
            if (format != null) {
                livre.setFormat(format);
            }
            livre.setDisponible(disponible);
        } else {
            livre = new Livre(isbn, titre, auteur, editeur, disponible, format);
        }

        return this.databaseLivreService.enregistrerLivreData(livre);
    }

    public Livre modifierLivre(Livre livreAvecModif) throws InvalidIsbnEmptyException {
        String isbn = livreAvecModif.getIsbn();
        if (isbn == null || isbn == "") {
            throw new InvalidIsbnEmptyException("L'ISBN est vide.");
        }

        this.isbnValidator.validateISBN(isbn);
        Livre livre = this.webLivreService.getLivreData(isbn);

        String titre = livreAvecModif.getTitre();
        if (titre != null && titre != "") {
            livre.setTitre(titre);
        }
        String auteur = livreAvecModif.getAuteur();
        if (auteur != null && auteur != "") {
            livre.setAuteur(auteur);
        }
        String editeur = livreAvecModif.getEditeur();
        if (editeur != null && editeur != "") {
            livre.setEditeur(editeur);
        }

        livre.setDisponible(livreAvecModif.getDisponible());

        Format format = livreAvecModif.getFormat();
        if (format != null) {
            livre.setFormat(format);
        }

        return this.databaseLivreService.enregistrerLivreData(livre);
    }

    public void supprimerLivre(String isbn) throws InvalidIsbnEmptyException, InvalidIsbnException {
        if (isbn == null || isbn == "") {
            throw new InvalidIsbnEmptyException("L'ISBN est vide.");
        }

        this.isbnValidator.validateISBN(isbn);

        if (this.webLivreService.getLivreData(isbn) == null) {
            throw new InvalidIsbnException("L'ISBN est invalide.");
        }

        this.databaseLivreService.SupprimerLivreData(isbn);
    }

    public Livre RechercheLivreParIsnb(String isbn) throws InvalidIsbnEmptyException {
        if (isbn == null || isbn == "") {
            throw new InvalidIsbnEmptyException("L'ISBN est vide.");
        }

        this.isbnValidator.validateISBN(isbn);

        return this.databaseLivreService.RechercheParIsbnLivreData(isbn);
    }

    public List<Livre> RechercheLivreParTitre(String titre) throws InvalidIsbnEmptyException, InvalideTitreException {
        if (titre == null || titre == "") {
            throw new InvalideTitreException("Le titre est vide.");
        }

        return this.databaseLivreService.RechercheParTitreLivreData(titre);
    }

    public List<Livre> RechercheLivreParAuteur(String auteur)
            throws InvalidIsbnEmptyException, InvalideAuteurException {
        if (auteur == null || auteur == "") {
            throw new InvalideAuteurException("L'auteur est vide.");
        }

        return this.databaseLivreService.RechercheParAuteurLivreData(auteur);
    }
}
