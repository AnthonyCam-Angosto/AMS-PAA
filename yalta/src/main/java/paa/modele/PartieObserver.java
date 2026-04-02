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
}
