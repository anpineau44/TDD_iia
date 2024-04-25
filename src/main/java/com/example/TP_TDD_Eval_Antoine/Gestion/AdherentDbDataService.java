package com.example.TP_TDD_Eval_Antoine.Gestion;

public interface AdherentDbDataService {

    Adherent getAdherentData(String codeAdherent);

    Adherent enregistrerAdherentData(Adherent adherent);
}
