package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;


import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Pion extends Piece {
    private boolean firstMove=true;

    public Pion(Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(1, couleur,switchPartieStrategy);
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
