package paa.modele.Element;

import java.util.List;

import paa.controler.ActionDeplacementPossible;
import paa.controler.ActionManger;
import paa.controler.ActionPromotion;
import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;
import paa.vue.PieceView;

/**
 * Classe abstraite représentant une pièce du jeu
 */
public abstract class Piece {
    protected  int valeur;
    protected Couleur couleur;
    protected PieceView view;
    protected SwitchPartieStrategy switchPartieStrategy;

    public Piece(int valeur, Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        this.valeur = valeur;
        this.couleur = couleur;
        this.switchPartieStrategy = switchPartieStrategy;
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

    /**
     * Met à jour les actions disponibles des cases  selon les actions possibles de la pièce sur la case actuelle, en tenant compte des déplacements, des captures et des promotions.
     * @param caseActuelle la case sur laquelle la pièce est actuellement positionnée
     */
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

    /**
     * Récupère la case de promotion pour la pièce, en fonction de sa position actuelle et des règles de promotion du jeu.
     * par defaut, aucune promotion n'est disponible, mais les pièces spécifiques peuvent override cette méthode pour implémenter leurs propres règles de promotion.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver la case de promotion)
     * @return la case de promotion, ou null si aucune promotion n'est disponible
     */
    protected Case promotion(Plateau plateau, int[] indexActuel){
        return null;
    }

    /**
     * recupere tout les cases sur lequel la piece peut se deplacer.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver les cases de deplacement)
     * @return la liste des cases de deplacement
     */
    abstract protected List<Case> deplacement(Plateau plateau, int[] indexActuel);

    /**
     * recupere tout les cases sur lequel la piece peut manger une piece adverse.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver les cases de capture)
     * @return la liste des cases de capture
     */
    abstract protected List<Case> manger(Plateau plateau, int[] indexActuel);    
}
