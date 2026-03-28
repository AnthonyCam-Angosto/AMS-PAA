package paa.controler;

import paa.modele.Element.Piece;
import paa.modele.Element.Pion;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class ActionDeplacementPossible implements ActionCase {
    private final Case actuel;
    private final Case casePiece;

    public ActionDeplacementPossible(Case actuel, Case casePiece) {
        this.actuel = actuel;
        this.casePiece = casePiece;
    }

    @Override
    public void click() {
        Piece piece = casePiece.getPiece();
        if(piece instanceof Pion){
            Pion pion = (Pion) piece;
            pion.setFirstMove(false);
        }
        Plateau.getInstance().deplacementPiece(casePiece, actuel);
    }
    
}
