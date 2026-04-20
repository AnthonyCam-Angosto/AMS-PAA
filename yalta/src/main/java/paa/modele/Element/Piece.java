package paa.modele.Element;

import java.util.List;

import paa.controler.ActionDeplacementPossible;
import paa.controler.ActionManger;
import paa.controler.ActionSpecial;
import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.Prototype;
import paa.modele.Utilisateur.Utilisateur;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/**
 * Classe abstraite représentant une pièce du jeu
 */
public abstract class Piece implements Prototype<Piece> {
    protected  int valeur;
    protected Couleur couleur;
    protected SwitchPartieStrategy switchPartieStrategy;
    protected boolean hasMoved;

    public Piece(int valeur, Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        this.valeur = valeur;
        this.couleur = couleur;
        this.switchPartieStrategy = switchPartieStrategy;
        this.hasMoved = false;
    }

    public int getValeur() {
        return valeur;
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
     * Crée une copie logique de la pièce sans la vue graphique.
     * @return une nouvelle pièce avec le même état utile pour les simulations
     */
    @Override
    public abstract Piece copy();

    /**
     * Met à jour les actions disponibles des cases  selon les actions possibles de la pièce sur la case actuelle, en tenant compte des déplacements, des captures et des promotions.
     * @param caseActuelle la case sur laquelle la pièce est actuellement positionnée
     */
    public void coup_disponible(Case caseActuelle){
        Plateau plateau = Plateau.getInstance();
        int[] indexActuel = plateau.getIndexCase(caseActuelle);

        List<Case> deplacements = deplacement(plateau, indexActuel);
        for (Case c : deplacements) {
            if (isLegalMove(caseActuelle, c, plateau)) {
                c.setAction(new ActionDeplacementPossible(c, caseActuelle));
            }
        }

        List<Case> captures = manger(plateau, indexActuel);
        for (Case c : captures) {
            if (isLegalMove(caseActuelle, c, plateau)) {
                c.setAction(new ActionManger(c,caseActuelle));
            }
        }

        List<Case> specialCases = specials(plateau, indexActuel);
        for (Case specialCase : specialCases) {
            specialCase.setAction(new ActionSpecial(specialCase,caseActuelle));
        }
    } 

    public List<Case> specials(Plateau plateau, int[] indexActuel){
        return List.of();
    }

    /**
     * Verifie qu'un coup conserve le roi du joueur en securite.
     * @param depart case de depart
     * @param arrivee case d'arrivee
     * @param plateau plateau de jeu
     * @return true si le coup est legal
     */
    public boolean isLegalMove(Case depart, Case arrivee, Plateau plateau) {
        Partie partie = Partie.getInstance();
        Utilisateur joueur = null;

        for (Utilisateur candidat : partie.getJoueurs()) {
            if (candidat != null && candidat.getCouleur() == this.couleur) {
                joueur = candidat;
                break;
            }
        }

        if (joueur == null) {
            return false;
        }

        return partie.isLegalMove(joueur, depart, arrivee, plateau);
    }

    /**
     * recupere tout les cases sur lequel la piece peut se deplacer.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver les cases de deplacement)
     * @return la liste des cases de deplacement
     */
    abstract public List<Case> deplacement(Plateau plateau, int[] indexActuel);

    /**
     * recupere tout les cases sur lequel la piece peut manger une piece adverse.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver les cases de capture)
     * @return la liste des cases de capture
     */
    abstract public List<Case> manger(Plateau plateau, int[] indexActuel);    
}
