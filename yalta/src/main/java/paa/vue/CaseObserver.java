package paa.vue;

import paa.modele.TypeAction;
import paa.modele.plateau.Case;

/**
 * Représente un observateur d'une case du plateau.
 */
public interface CaseObserver {
    /**
     * Notifie l'observateur d'un changement de pièce sur la case.
     * @param c la case concernée
     */
    void onPieceChanged(Case c);
    /**
     * Notifie l'observateur d'un changement d'action sur la case.
     * @param c la case concernée
     */
    void onActionChanged(Case c);
    /**
     * Notifie l'observateur que cette case est utilisable pour une action sélectionnée.
     * @param t le type d'action sélectionnée
     */
    void onSelected(TypeAction t);
    /**
     * Notifie l'observateur que cette case n'a plus à être considérée comme utilisable pour une action sélectionnée.
     * @param c la case concernée
     */
    void onDeselected(Case c);

    void onPromotion(Case c);
}