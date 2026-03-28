package paa.vue;

import paa.modele.TypeAction;
import paa.modele.plateau.Case;

public interface CaseObserver {
    void onPieceChanged(Case c);
    void onActionChanged(Case c);
    void onSelected(TypeAction t);
    void onDeselected(Case c);
}