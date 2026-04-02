package paa.modele;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Utilisateur.IA;
import paa.modele.Utilisateur.Joueur;
import paa.modele.Utilisateur.Utilisateur;

/**
 * Représente la partie de jeu.
 * c'est une classe singleton qui gère les joueurs, le tour actuel et les observateurs de la partie.
 * Elle permet d'initialiser la partie avec un nombre de joueurs humains et d'IA, 
 * de faire avancer le tour et de notifier les observateurs à chaque changement de tour.
 */
public class Partie implements PartieSubject {
    private int tour;
    private final Utilisateur[] joueurs;
    private final List<PartieObserver> observers=new ArrayList<>();

    private final Couleur[] ordre= {Couleur.BLANC, Couleur.NOIR, Couleur.ROUGE};//ordre de jeu des joueurs

    private static Partie instance = null;

    private Partie() {
        this.tour = -1;
        this.joueurs = new Utilisateur[3];
    }

    /**
     * Retourne l'instance unique de la classe Partie, en la créant si elle n'existe pas encore.
     * @return L'instance de la classe Partie
     */
    public static Partie getInstance() {
        if (instance == null) {
            instance = new Partie();
        }
        return instance;
    }

    public Couleur[] getOrdre() {
        return ordre;
    }

    public int getTour() {
        return tour;
    }

    /**
     * Passe au tour suivant et notifie les joueurs du changement de tour.
     */
    public void tourSuivant() {
        tour++;
        Utilisateur joueurActuel=joueurs[tour%3];
        System.out.println("C'est au joueur " + joueurActuel.getCouleur() + " de jouer.");
        notifyTourChange(joueurActuel);
    }

    /**
     * Retourne la couleur du joueur actuel en fonction du tour.
     * @return La couleur du joueur actuel
     */
    public Couleur getCouleurJoueurActuel() {
        return joueurs[tour % 3].getCouleur();
    }
    
    /**
     * Réinitialise la partie.
     */
    public void reset() {
        tour = -1;
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i] = null;
        }
    }


    public void addJoueur(Utilisateur joueur) {
        if (joueurs[0] == null) {
            joueurs[0] = joueur;
        } else if (joueurs[1] == null) {
            joueurs[1] = joueur;
        } else if (joueurs[2] == null) {
            joueurs[2] = joueur;
        } else {
            throw new IllegalStateException("Il y a déjà 3 joueurs dans la partie");
        }
    }

    public void initialiserPartie(int nbNoIA) {
        reset();

        Utilisateur joueur;
        for (int i = 0; i < nbNoIA; i++) {
            joueur = new Joueur(ordre[i]);
            addJoueur(joueur);
            addObserver(joueur);
        }
        for (int i = nbNoIA; i < 3; i++) {
            joueur = new IA(ordre[i]);
            addJoueur(joueur);
            addObserver(joueur);
        }
        tourSuivant();
    }

    public boolean Echec() {
        // TODO Implémentation de la logique d'échec
        return false; 
    }

    public boolean EchecEtMat() {
        // TODO Implémentation de la logique d'échec et mat
        return false;
    }

    public boolean Pat() {
        // TODO Implémentation de la logique de pat
        return false;
    }

    @Override
    public void addObserver(PartieObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(PartieObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyTourChange(Utilisateur joueur) {
        for (PartieObserver observer : observers) {
            observer.onTourChange(joueur);
        }
    }

    
}
