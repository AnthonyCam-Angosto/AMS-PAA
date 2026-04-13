package paa.controler;


import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class ActionManger implements ActionCase {
        private final Case actuel;
    private final Case casePiece;

    public ActionManger(Case actuel, Case casePiece) {
        this.actuel = actuel;
        this.casePiece = casePiece;
    }

    @Override
    public void click() {
        Plateau.getInstance().deplacementPiece(casePiece, actuel,false);
    }
    
}
