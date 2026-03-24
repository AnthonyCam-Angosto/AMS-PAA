package paa.modele.Element;

import paa.modele.Couleur;

public class Pion extends Piece {
    public Pion(Couleur couleur) {
        super(1, couleur);
    }

    @Override
    public void deplacement() {
        // Implémentation du déplacement du pion
    }

    @Override
    public void manger() {
        // Implémentation de la capture du pion
    }

    @Override
    public void coup_disponible() {
        // Implémentation des coups disponibles pour le pion
    }
    
}
