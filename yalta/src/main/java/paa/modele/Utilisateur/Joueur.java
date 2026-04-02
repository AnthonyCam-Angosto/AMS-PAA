package paa.modele.Utilisateur;

import paa.modele.Couleur;

public class Joueur extends Utilisateur {

    public Joueur(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void onTourChange(Utilisateur joueur) {
        if (joueur == this) {
            debuterTour();
            jouer();
        } else {
            finirTour();
        }
    }
    
}
