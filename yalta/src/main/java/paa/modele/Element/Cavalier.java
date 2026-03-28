package paa.modele.Element;

import java.util.List;

import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Cavalier extends Piece {
    public Cavalier( Couleur couleur) {
        super(3, couleur);
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        // Implémentation du déplacement du cavalier
        return List.of();
    }

    @Override
    protected List<Case> manger(Plateau plateau, int[] indexActuel) {
        // Implémentation de la capture du cavalier
        return List.of();
    }

    @Override
    protected Case promotion(Plateau plateau, int[] indexActuel) {
        return null;
    }

    @Override
    protected Case switchPartie(Plateau plateau, int[] indexActuel, int isManger) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'switchPartie'");
    }
    
}
