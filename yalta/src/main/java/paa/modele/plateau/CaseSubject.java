package paa.modele.plateau;

import paa.modele.TypeAction;
import paa.vue.CaseObserver;

public interface CaseSubject {
    void ajouterObservateur(CaseObserver observer);
    void supprimerObservateur(CaseObserver observer);
    void notifyPieceChanged();
    void notifyActionChanged();
    void notifySelected(TypeAction t);
    void notifyDeselected();

}
