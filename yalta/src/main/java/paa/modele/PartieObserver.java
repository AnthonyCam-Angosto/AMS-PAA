package paa.modele;

import paa.modele.Utilisateur.Utilisateur;

/**
 * Représente un observateur de la partie.
 */
public interface PartieObserver {
    /**
     * Notifie l'observateur d'un changement de tour, en lui fournissant le joueur dont c'est le tour.
     * @param joueur le joueur dont c'est le tour
     */
    void onTourChange(Utilisateur joueur);

    /**
     * Notifie l'observateur que la partie est terminee.
     * @param perdant le joueur declare perdant (null si non determine)
     * @param typeFin le type de fin de partie
     */
    default void onPartieFinie(Utilisateur perdant, TypeFin typeFin) {
    }
}
