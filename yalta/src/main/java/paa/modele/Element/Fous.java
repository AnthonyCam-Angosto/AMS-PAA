package paa.modele.Element;

import paa.modele.Couleur;

public class Fous extends Piece {
    public Fous(Couleur couleur) {
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
    public void coup_disponible() {
        // Implémentation des coups disponibles pour le fou
    }
    
}
