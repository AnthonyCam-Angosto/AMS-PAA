package paa.modele;

import paa.modele.Utilisateur.Utilisateur;

/**
 * Représente un sujet de la partie, c'est-à-dire une entité qui peut être observée par des observateurs de la partie et qui peut les notifier en cas de changement de tour.
 */
public interface PartieSubject {
    /**
     * Ajoute un observateur à la partie.
     * @param o l'observateur à ajouter
     */
    void addObserver(PartieObserver o);

    /**
     * Supprime un observateur de la partie.
     * @param o l'observateur à supprimer
     */
    void removeObserver(PartieObserver o);

    /**
     * Notifie tous les observateurs d'un changement de tour, en leur fournissant le joueur dont c'est le tour.
     * @param joueur le joueur dont c'est le tour
     */
    void notifyTourChange(Utilisateur joueur);

    /**
     * Notifie tous les observateurs que la partie est terminee.
     * @param perdant le joueur declare perdant
     * @param typeFin le type de fin de partie
     */
    void notifyPartieFinie(Utilisateur perdant, TypeFin typeFin);
}
