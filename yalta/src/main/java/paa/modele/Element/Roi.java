package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Roi extends Piece {
    public Roi( Couleur couleur) {
        super(1000, couleur);
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i==0 && j==0) continue;
                int newX=indexActuel[0]+i;
                int newY=indexActuel[1]+j;
                if(newX>=0 && newX<4 && newY>=0 && newY<8){
                    Case c = plateau.getCase(newX, newY, indexActuel[2]);
                    if (c.isEmpty()) {
                        deplacements.add(c);
                    }
                }
            }
        }
        if(indexActuel[0]==3){
            for(int i=-1;i<=1;i++){
                Case c=switchPartie(plateau,indexActuel,i);
                if(c.isEmpty()){
                deplacements.add(c);
                }
            }
        }
        return deplacements;
    }

    @Override
    protected List<Case> manger(Plateau plateau, int[] indexActuel) {
        List<Case> captures = new ArrayList<>();

        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i==0 && j==0) continue;
                int newX=indexActuel[0]+i;
                int newY=indexActuel[1]+j;
                if(newX>=0 && newX<4 && newY>=0 && newY<8){
                    Case c = plateau.getCase(newX, newY, indexActuel[2]);
                    if (!c.isEmpty() && c.getPiece().getCouleur() != this.getCouleur()) {
                        captures.add(c);
                    }
                }
            }
        }
        if(indexActuel[0]==3){
            for(int i=-1;i<=1;i++){
                Case c=switchPartie(plateau,indexActuel,i);
                if(!c.isEmpty() && c.getPiece().getCouleur() != this.getCouleur()){
                captures.add(c);
                }
            }
        }
        return captures;
    }

    @Override
    protected Case promotion(Plateau plateau, int[] indexActuel) {
        return null;
    }

    @Override
    protected Case switchPartie(Plateau plateau, int[] indexActuel,int diagonal){
        if(indexActuel[1]<4){
            Case c =switch (indexActuel[2]) {
                case 0 ->plateau.getCase(indexActuel[0], indexActuel[1]+diagonal, 1);
                case 1 ->plateau.getCase(indexActuel[0], indexActuel[1]+diagonal, 0);
                case 2 ->plateau.getCase(indexActuel[0], 7-indexActuel[1]+diagonal, 0);
                default->null;
            };
            return c;
        }else{
                Case c =switch (indexActuel[2]) {
                case 0 ->plateau.getCase(indexActuel[0], 7-indexActuel[1]+diagonal, 2);
                case 1 ->plateau.getCase(indexActuel[0], indexActuel[1]+diagonal, 2);
                case 2 ->plateau.getCase(indexActuel[0], indexActuel[1]+diagonal, 1);
                default->null;
            };
            return c;
        }
    } 
}
