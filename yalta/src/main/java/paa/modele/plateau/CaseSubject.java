package paa.modele.plateau;

import paa.modele.TypeAction;
import paa.vue.CaseObserver;

/**
 * Représente un sujet de la case du plateau de jeu
 */
public interface CaseSubject {
    /**
     * Ajoute un observateur à la liste des observateurs de la case.
     * @param observer L'observateur à ajouter
     */
    void ajouterObservateur(CaseObserver observer);

    /**
     * Supprime un observateur de la liste des observateurs de la case.
     * @param observer L'observateur à supprimer
     */
    void supprimerObservateur(CaseObserver observer);

    /**
     * Notifie les observateurs de la vue du changement de pièce.
     */
    void notifyPieceChanged();

    /**
     * Notifie les observateurs de la vue du changement d'action.
     */
    void notifyActionChanged();

    /**
     * Notifie les observateurs de la vue de la sélection de la case, en précisant le type d'action associé à la sélection.
     * @param t Le type d'action associé à la sélection de la case
     */
    void notifySelected(TypeAction t);

    /**
     * Notifie les observateurs de la vue de la désélection de la case.
     */
    void notifyDeselected();

}
