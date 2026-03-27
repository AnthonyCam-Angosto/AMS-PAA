package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.plateau.Case;

public class Fou extends Piece {
    public Fou(Couleur couleur) {
        super(3, couleur);
    }

    @Override
    public void deplacement() {
        // Implémentation du déplacement du fou
    }

    @Override
    public void manger() {
        // Implémentation de la capture du fou
    }

    @Override
    public void coup_disponible(Case caseActuelle) {
        // Implémentation des coups disponibles pour le fou
    }
    
}
