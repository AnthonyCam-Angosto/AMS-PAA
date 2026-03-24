package paa.controler;

import paa.modele.Case;

public class ActionVide implements ActionCase {
    private final Case cellCase;

    public ActionVide(Case cellCase) {
        this.cellCase = cellCase;
    }

    @Override
    public void click() {
        System.out.println("Clicked on an empty cell with ID: " + cellCase.getId());
    }
    
}
