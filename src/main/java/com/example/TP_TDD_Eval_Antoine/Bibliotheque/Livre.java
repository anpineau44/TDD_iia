package com.example.TP_TDD_Eval_Antoine.Bibliotheque;

public class Livre {
    private String isbn;
    private String titre;
    private String auteur;
    private String editeur;
    private boolean disponible;
    private Format format;

    public Livre(String isbn, String titre, String auteur, String editeur, boolean disponible, Format format) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.editeur = editeur;
        this.disponible = disponible;
        this.format = format;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getEditeur() {
        return editeur;
    }

    public boolean getDisponible() {
        return disponible;
    }

    public Format getFormat() {
        return format;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
