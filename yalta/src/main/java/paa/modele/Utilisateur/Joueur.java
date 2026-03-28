package paa.modele.Utilisateur;

import paa.modele.Couleur;

public class Joueur extends Utilisateur {

    public Joueur(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void jouer() {
    }


    @Override
    public void evoluer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evoluer'");
    }

    @Override
    public void mat() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mat'");
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
