package paa.controler;

import paa.modele.Element.Piece;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class ActionSpecial implements ActionCase {
    private final Case caseSpecial;
    private final Case caseActuelle;

    public ActionSpecial(Case caseSpecial, Case caseActuelle) {
        this.caseSpecial = caseSpecial;
        this.caseActuelle = caseActuelle;
    }

    @Override
    public void click() {
        Piece piece = caseActuelle.getPiece();
        switch (piece.getValeur()) {
            case 1 -> //pion
                Plateau.getInstance().promotion(caseActuelle, caseSpecial);
            case 1000 -> //roi
                Plateau.getInstance().castling(caseActuelle, caseSpecial);
            default -> {
            }
        }
    }
    
}
