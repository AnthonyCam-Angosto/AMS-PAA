package paa.vue;

import paa.modele.plateau.Case;

public interface CaseObserver {
    void onPieceChanged(Case c);
    void onActionChanged(Case c);
    void onSelected(Case c);
    void onDeselected(Case c);
}