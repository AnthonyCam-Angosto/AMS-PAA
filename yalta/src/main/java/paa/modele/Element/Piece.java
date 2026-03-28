package paa.modele.Element;

import java.util.List;

import paa.controler.ActionDeplacementPossible;
import paa.controler.ActionManger;
import paa.controler.ActionPromotion;
import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;
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

    public void coup_disponible(Case caseActuelle){
        Plateau plateau = Plateau.getInstance();
        int[] indexActuel = plateau.getIndexCase(caseActuelle);

        List<Case> deplacements = deplacement(plateau, indexActuel);
        for (Case c : deplacements) {
            c.setAction(new ActionDeplacementPossible(c, caseActuelle));
        }

        List<Case> captures = manger(plateau, indexActuel);
        for (Case c : captures) {
            c.setAction(new ActionManger(c,caseActuelle));
        }

        Case promotionCase = promotion(plateau, indexActuel);
        if (promotionCase != null) {
            promotionCase.setAction(new ActionPromotion(promotionCase,caseActuelle));
        }
    } 

    

    abstract protected List<Case> deplacement(Plateau plateau, int[] indexActuel);
    abstract protected List<Case> manger(Plateau plateau, int[] indexActuel);
    abstract protected Case promotion(Plateau plateau, int[] indexActuel);
    abstract protected Case switchPartie(Plateau plateau, int[] indexActuel,int isManger);
    
}
