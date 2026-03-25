package paa.modele.Utilisateur;

import paa.modele.Couleur;

public abstract class Utilisateur {
    protected boolean tour;
    protected Couleur couleur;
    protected int score;

    public Utilisateur(Couleur couleur) {
        this.couleur = couleur;
        this.score = 0;
        this.tour = false;
    }

    public boolean isTour() {
        return tour;
    }

    public void debuterTour() {
        this.tour = true;
    }

    public void finirTour() {
        this.tour = false;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int points) {
        this.score += points;
    }

    abstract public void jouer();
    abstract public void deplacer();
    abstract public void evoluer();
    abstract public void mat();
}
