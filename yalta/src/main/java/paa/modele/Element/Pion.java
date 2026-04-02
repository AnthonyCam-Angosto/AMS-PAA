package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;


import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Pion extends Piece {
    public Pion(Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(1, couleur,switchPartieStrategy);
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        if (!hasMoved()) {
            Case temp=plateau.getCase(indexActuel[0]+1, indexActuel[1], indexActuel[2]);
            if(temp.isEmpty()){
                Case c = plateau.getCase(indexActuel[0] + 2, indexActuel[1], indexActuel[2]);
                if (c.isEmpty()) {
                    deplacements.add(c);
                }
            }
        }

        if(indexActuel[0]+1<4 && !hasChangedPartie(plateau, indexActuel)){
            Case c = plateau.getCase(indexActuel[0] + 1, indexActuel[1], indexActuel[2]);
            if (c.isEmpty()) {
                deplacements.add(c);
            }
        }else if(!hasChangedPartie(plateau, indexActuel)){
            Case c=switchPartieStrategy.resolve(plateau,indexActuel,0);
            if(c.isEmpty()){
                deplacements.add(c);
            }
        }else{
            Case c = plateau.getCase(indexActuel[0] -1, indexActuel[1], indexActuel[2]);
            if (c.isEmpty()) {
                deplacements.add(c);
            }
        }
        return deplacements;
    }

    @Override
    protected List<Case> manger(Plateau plateau, int[] indexActuel) {
        List<Case> captures = new ArrayList<>();
        int[] range={-1,1};

        if(indexActuel[0]+1<4 && !hasChangedPartie(plateau, indexActuel)){
            for (int i : range) {
                if(indexActuel[1]+i<0 || indexActuel[1]+i>7) continue;
                Case c = plateau.getCase(indexActuel[0] + 1, indexActuel[1] + i, indexActuel[2]);

                if(c.getPiece()==null) continue;
                if (c.getPiece().couleur != this.couleur) {
                    captures.add(c);
                }
            }
        }else if(!hasChangedPartie(plateau, indexActuel)){
            for (int i : range) {
                if(indexActuel[1]+i<0 || indexActuel[1]+i>7) continue;

                Case c = switchPartieStrategy.resolve(plateau, indexActuel, i);
                if(c.getPiece()==null) continue;
                if (c.getPiece().couleur != this.couleur) {
                    captures.add(c);
                }
                c=switchPartieStrategy.resolveSpecial(plateau, indexActuel, i);
                System.out.println("c special "+c.getId());
                if(c.getPiece()==null) continue;
                if (c.getPiece().couleur != this.couleur) {
                    captures.add(c);
                }
            }
        }else{
            for (int i : range) {
                if(indexActuel[1]+i<0 || indexActuel[1]+i>7) continue;
                Case c = plateau.getCase(indexActuel[0] - 1, indexActuel[1] + i, indexActuel[2]);
                if(c.getPiece()==null) continue;
                if (c.getPiece().couleur != this.couleur) {
                    captures.add(c);
                }
            }
        }
        return captures;
    }
    

    private boolean hasChangedPartie(Plateau plateau, int[] indexActuel){
        Case c = plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]);
        Piece piece = c.getPiece();
        Couleur[] couleurs = Partie.getInstance().getOrdre();

        for (int i = 0; i < couleurs.length; i++) {
            if (piece.couleur == couleurs[i] && indexActuel[2] != i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère la case de promotion pour la pièce, en fonction de sa position actuelle et des règles de promotion du jeu.
     * par defaut, aucune promotion n'est disponible, mais les pièces spécifiques peuvent override cette méthode pour implémenter leurs propres règles de promotion.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver la case de promotion)
     * @return la case de promotion, ou null si aucune promotion n'est disponible
     */
    @Override
    protected List<Case> specials(Plateau plateau, int[] indexActuel) {
        boolean changedPartie = hasChangedPartie(plateau, indexActuel);
        List<Case> destinationsPromotion = new ArrayList<>();
        Case destinationPromotion = null;

        // La destination spéciale suit la même direction que le déplacement simple du pion.
        if(!changedPartie){
            if(indexActuel[0] + 1 < 4){
                destinationPromotion = plateau.getCase(indexActuel[0] + 1, indexActuel[1], indexActuel[2]);
            }else{
                destinationPromotion = switchPartieStrategy.resolve(plateau, indexActuel, 0);
            }
        }else if(indexActuel[0] - 1 >= 0){
            destinationPromotion = plateau.getCase(indexActuel[0] - 1, indexActuel[1], indexActuel[2]);
        }

        if(destinationPromotion != null && destinationPromotion.isEmpty()){
            int[] indexDestination = plateau.getIndexCase(destinationPromotion);
            if(indexDestination != null && changedPartie && indexDestination[0] == 0){
                destinationsPromotion.add(destinationPromotion);
            }
        }

        // Promotion en capture diagonale (ex: B7 -> A8/C8).
        if(changedPartie && indexActuel[0] - 1 >= 0){
            int xCapture = indexActuel[0] - 1;
            for (int deltaY : new int[]{-1, 1}) {
                int yCapture = indexActuel[1] + deltaY;
                if(yCapture < 0 || yCapture > 7){
                    continue;
                }

                Case c = plateau.getCase(xCapture, yCapture, indexActuel[2]);
                if(c.isEmpty() || c.getPiece().getCouleur() == this.getCouleur()){
                    continue;
                }

                int[] indexCapture = plateau.getIndexCase(c);
                if(indexCapture != null && indexCapture[0] == 0){
                    destinationsPromotion.add(c);
                }
            }
        }

        return destinationsPromotion;
    }


}
