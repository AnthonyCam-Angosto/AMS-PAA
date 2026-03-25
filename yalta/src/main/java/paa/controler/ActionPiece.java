package paa.controler;

import paa.modele.Case;

public class ActionPiece implements ActionCase {
    private final Case cellCase;

    public ActionPiece(Case cellCase) {
        this.cellCase = cellCase;
    }

    @Override
    public void click() {
        System.out.println("ActionPiece: " + cellCase.getId() + " - pièce présente type:"+cellCase.getPiece().getClass().getSimpleName());
    }
}
