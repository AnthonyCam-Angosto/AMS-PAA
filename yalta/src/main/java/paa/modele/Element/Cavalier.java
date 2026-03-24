package paa.modele.Element;

import paa.modele.Couleur;

public class Cavalier extends Piece {
    public Cavalier( Couleur couleur) {
        super(3, couleur);
    }

    @Override
    public void deplacement() {
        // Implémentation du déplacement du cavalier
    }

    @Override
    public void manger() {
        // Implémentation de la capture du cavalier
    }

    @Override
    public void coup_disponible() {
        // Implémentation des coups disponibles pour le cavalier
    }
    
}
