package paa.modele;

import paa.modele.Utilisateur.Utilisateur;

public interface PartieObserver {
    void onTourChange(Utilisateur joueur);
}
