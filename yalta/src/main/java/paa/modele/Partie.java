package paa.modele;

import paa.modele.Utilisateur.Utilisateur;

public class Partie {
    private int tour;
    private Utilisateur[] joueurs;

    private static Partie instance = null;

    private Partie() {
        this.tour = 0;
        this.joueurs = new Utilisateur[3];
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
        int val=tour%3;
        if(val==0) {
            val=3;
        }
        joueurs[val-1].debuterTour();
        joueurs[(val+2)%3].finirTour();
    }
    public Utilisateur[] getJoueurs() {
        return joueurs;
    }
    
    public void reset() {
        tour = 0;
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

    public void initPartie() {
        Utilisateur[] tempsJ = new Utilisateur[3];
        for (Utilisateur joueur : joueurs) {
            switch (joueur.getCouleur()) {
                case Couleur.BLANC -> {
                    joueur.debuterTour();
                    tempsJ[0] = joueur;
                }
                case Couleur.NOIR -> tempsJ[1] = joueur;
                case Couleur.Rouge -> tempsJ[2] = joueur;
            }
        }
        this.joueurs = tempsJ;
    }

    
}
