package paa.modele.Element;

import paa.modele.Couleur;

public abstract class Piece {
    protected  int valeur;
    protected Couleur couleur;
    
    public Piece(int valeur, Couleur couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
    }

    abstract public void deplacement();
    abstract public void manger();
    abstract public void coup_disponible();
}
