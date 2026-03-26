package paa.modele.Utilisateur;

import paa.modele.Couleur;


public class IA extends Utilisateur {

    public IA(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void jouer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'jouer'");
    }

    @Override
    public void deplacer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deplacer'");
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
            System.out.println("C'est le tour de l'IA " + joueur.getCouleur() + "!");
        } else {
            finirTour();
        }
    }
    
}
