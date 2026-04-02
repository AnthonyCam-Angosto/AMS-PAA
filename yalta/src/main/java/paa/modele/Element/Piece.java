package paa.modele.Element;

import java.util.List;

import paa.controler.ActionDeplacementPossible;
import paa.controler.ActionManger;
import paa.controler.ActionSpecial;
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
    protected boolean hasMoved;

    public Piece(int valeur, Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        this.valeur = valeur;
        this.couleur = couleur;
        this.switchPartieStrategy = switchPartieStrategy;
        this.hasMoved = false;
    }

    public void createView(PieceView view) {
        this.view = view;
    }

    public int getValeur() {
        return valeur;
    }

    public PieceView getView() {
        return view;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
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

        List<Case> specialCases = specials(plateau, indexActuel);
        for (Case specialCase : specialCases) {
            specialCase.setAction(new ActionSpecial(specialCase,caseActuelle));
        }
    } 

    protected List<Case> specials(Plateau plateau, int[] indexActuel){
        return List.of();
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
