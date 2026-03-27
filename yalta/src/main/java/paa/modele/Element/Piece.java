package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.vue.PieceView;

public abstract class Piece {
    protected  int valeur;
    protected Couleur couleur;
    protected PieceView view;
    
    public Piece(int valeur, Couleur couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
    }

    public void createView(PieceView view) {
        this.view = view;
    }

    public PieceView getView() {
        return view;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    

    abstract public void deplacement();
    abstract public void manger();
    abstract public void coup_disponible(Case caseActuelle);
}
