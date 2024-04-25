package com.example.TP_TDD_Eval_Antoine.Gestion;

import java.util.Date;

public class Adherent {
    private String codeAdherent;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String civilite;

    public Adherent(String codeAdherent, String nom, String prenom, Date dateNaissance, String civilite) {
        this.codeAdherent = codeAdherent;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.civilite = civilite;
    }

    public String getCodeAdherent() {
        return codeAdherent;
    }

    public String getCodeNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCodeAdherent(String codeAdherent) {
        this.codeAdherent = codeAdherent;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }
}
