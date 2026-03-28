package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;


import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Pion extends Piece {
    private boolean firstMove=true;

    public Pion(Couleur couleur) {
        super(1, couleur);
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        if (firstMove) {
            Case c = plateau.getCase(indexActuel[0] + 2, indexActuel[1], indexActuel[2]);
            if (c.isEmpty()) {
                deplacements.add(c);
            }
        }

        if(indexActuel[0]+1<4 && !hasChangedPartie(plateau, indexActuel)){
            Case c = plateau.getCase(indexActuel[0] + 1, indexActuel[1], indexActuel[2]);
            if (c.isEmpty()) {
                deplacements.add(c);
            }
        }else if(!hasChangedPartie(plateau, indexActuel)){
            Case c=switchPartie(plateau,indexActuel,0);
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

                Case c = switchPartie(plateau, indexActuel,i); //TODO manque le millieu du plateau changer de plusieur partie
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


    protected Case switchPartie(Plateau plateau, int[] indexActuel,int isManger){
        if(indexActuel[1]<4){
            Case c =switch (indexActuel[2]) {
                case 0 ->plateau.getCase(indexActuel[0], indexActuel[1]+isManger, 1);
                case 1 ->plateau.getCase(indexActuel[0], indexActuel[1]+isManger, 0);
                case 2 ->plateau.getCase(indexActuel[0], 7-indexActuel[1]+isManger, 0);
                default->null;
            };
            return c;
        }else{
                Case c =switch (indexActuel[2]) {
                case 0 ->plateau.getCase(indexActuel[0], 7-indexActuel[1]+isManger, 2);
                case 1 ->plateau.getCase(indexActuel[0], indexActuel[1]+isManger, 2);
                case 2 ->plateau.getCase(indexActuel[0], indexActuel[1]+isManger, 1);
                default->null;
            };
            return c;
        }
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

    @Override
    protected Case promotion(Plateau plateau, int[] indexActuel) {
        if(hasChangedPartie(plateau, indexActuel)){
            if((indexActuel[0]==0 && indexActuel[2]==0) || (indexActuel[0]==3 && indexActuel[2]==1) || (indexActuel[0]==3 && indexActuel[2]==2)){
                return plateau.getCase(indexActuel[0]+1, indexActuel[1], indexActuel[2]);
            }
        }
        return null;
    }

}
