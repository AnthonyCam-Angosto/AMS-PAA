package paa.modele.Utilisateur;

import paa.modele.Couleur;

/**
 * Représente un joueur humain du jeu.
 */
public class Joueur extends Utilisateur {

    public Joueur(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void jouer() {
        // Le joueur humain joue via l'interface graphique.
    }
    
}
