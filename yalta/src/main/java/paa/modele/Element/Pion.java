package paa.modele.Element;

import paa.controler.ActionDeplacementPossible;
import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Pion extends Piece {
    private boolean firstMove=true;

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
    public void coup_disponible(Case caseActuelle) {
        Plateau plateau = Plateau.getInstance();
        int[] indexActuel = plateau.getIndexCase(caseActuelle);

        if(firstMove){
            Case c=plateau.getCase(indexActuel[0]+2, indexActuel[1], indexActuel[2]);
            c.setAction(new ActionDeplacementPossible(c, caseActuelle));

        }
        Case c=plateau.getCase(indexActuel[0]+1, indexActuel[1], indexActuel[2]);
        c.setAction(new ActionDeplacementPossible(c, caseActuelle));
    }
    
}
