package paa.modele.Utilisateur;

import paa.modele.Couleur;
import paa.modele.PartieObserver;

/**
 * Représente un utilisateur du jeu, pouvant être un joueur humain ou une intelligence artificielle.
 */
public abstract class Utilisateur implements PartieObserver {
    protected boolean tour;
    protected Couleur couleur;
    protected int score;
    protected boolean echec;

    public Utilisateur(Couleur couleur) {
        this.couleur = couleur;
        this.score = 0;
        this.tour = false;
        this.echec = false;
    }

    public boolean isTour() {
        return tour;
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

    public boolean isEchec() {
        return echec;
    }

    public void setEchec(boolean echec) {
        this.echec = echec;
    }

    /**
     * Template Method: flux unique de gestion d'un changement de tour.
     * Les sous-classes ne définissent que les etapes variables via jouer().
     */
    @Override
    public final void onTourChange(Utilisateur joueur) {
        if (joueur != this) {
            tour = false;
            return;
        }

        tour=true;
        jouer();
    }


    /**
     * Permet à l'utilisateur de jouer un coup.
     */
    public abstract void jouer();


    /**
     * Permet a la partie de signaler à l'utilisateur qu'il est en échec, c'est à dire que son roi est menacé et doit être protégé au prochain coup.
     * utiliser seulement par les IA,les joeurs utilise leur interface pour jouer, et cette méthode est appelé par l'interface pour les IA
    */
    public void echec(){}

}
