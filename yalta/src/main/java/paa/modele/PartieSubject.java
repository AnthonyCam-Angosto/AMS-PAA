package paa.modele;

import paa.modele.Utilisateur.Utilisateur;

public interface PartieSubject {
    void addObserver(PartieObserver o);
    void removeObserver(PartieObserver o);
    void notifyTourChange(Utilisateur joueur);
}
