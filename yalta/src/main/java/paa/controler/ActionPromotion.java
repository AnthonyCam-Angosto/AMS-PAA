package paa.controler;

import paa.modele.plateau.Case;

public class ActionPromotion implements ActionCase {
    private Case casePromotion;
    private Case caseActuelle;

    public ActionPromotion(Case casePromotion, Case caseActuelle) {
        this.casePromotion = casePromotion;
        this.caseActuelle = caseActuelle;
    }

    @Override
    public void click() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'click'");
    }
    
}
