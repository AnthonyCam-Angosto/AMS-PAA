package paa.modele;

import paa.modele.Utilisateur.Joueur;

public class Partie {
    private int tour;
    private final Joueur[] joueurs;

    private static Partie instance = null;

    private Partie() {
        this.tour = 0;
        this.joueurs = new Joueur[3];
    }

    public static Partie getInstance() {
        if (instance == null) {
            instance = new Partie();
        }
        return instance;
    }

    public int getTour() {
        return tour;
    }
    public void tourSuivant() {
        tour++;
    }
    public Joueur[] getJoueurs() {
        return joueurs;
    }
    
    public void reset() {
        tour = 0;
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i] = null;
        }
    }


    public void addJoueur(Joueur joueur) {
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

    
}
