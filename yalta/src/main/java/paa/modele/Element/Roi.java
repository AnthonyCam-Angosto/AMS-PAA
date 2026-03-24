package paa.modele.Element;

import paa.modele.Couleur;

public class Roi extends Piece {
    public Roi( Couleur couleur) {
        super(1000, couleur);
    }

    @Override
    public void deplacement() {
        // Implémentation du déplacement du roi
    }

    @Override
    public void manger() {
        // Implémentation de la capture du roi
    }

    @Override
    public void coup_disponible() {
        // Implémentation des coups disponibles pour le roi
    }
    
}
