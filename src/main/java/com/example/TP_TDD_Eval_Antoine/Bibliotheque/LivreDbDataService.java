package com.example.TP_TDD_Eval_Antoine.Bibliotheque;

import java.util.List;

public interface LivreDbDataService {
    Livre getLivreData(String isbn);

    Livre enregistrerLivreData(Livre livre);

    Livre ModifierLivreData(Livre livre);

    void SupprimerLivreData(String isbn);

    Livre RechercheParIsbnLivreData(String isbn);

    List<Livre> RechercheParTitreLivreData(String titre);

    List<Livre> RechercheParAuteurLivreData(String titre);
}
