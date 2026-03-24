package paa.modele.Element;

import paa.modele.Couleur;

public class Tour extends Piece {
    public Tour( Couleur couleur) {
        super(5, couleur);
    }

    @Override
    public void deplacement() {
        // Implémentation du déplacement de la tour
    }

    @Override
    public void manger() {
        // Implémentation de la capture de la tour
    }

    @Override
    public void coup_disponible() {
        // Implémentation des coups disponibles pour la tour
    }
    
}
