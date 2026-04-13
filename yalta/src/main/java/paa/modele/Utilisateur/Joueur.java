package paa.modele.Utilisateur;

import paa.modele.Couleur;

public class Joueur extends Utilisateur {

    public Joueur(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void jouer() {
        // Le joueur humain joue via l'interface graphique.
    }
    
}
