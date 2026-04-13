package paa.modele.Utilisateur;

import paa.modele.Couleur;


public class IA extends Utilisateur {

    public IA(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void jouer() {
        //TODO: implémenter une IA
        System.out.println("C'est le tour de l'IA " + getCouleur() + "!");
    }

    @Override
    public void echec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'echec'");
    }

}
