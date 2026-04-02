package paa.modele.Element;

import java.util.List;

import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Cavalier extends Piece {
    public Cavalier( Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(3, couleur,switchPartieStrategy);
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
    
}
