package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Tour extends Piece {
    public Tour( Couleur couleur) {
        super(5, couleur);
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        for(int i=0;i<2;i++){
            List<Case> line = plateau.getLine(indexActuel, i);

            List<List<Case>> splitLine = splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

            for (int j = 0; j < 2; j++) {
                for (Case c : splitLine.get(j)) {
                    if (c.isEmpty()) {
                        deplacements.add(c);
                    } else {
                        break;
                    }
                }
            }
        }
        return deplacements;
    }

    @Override
    protected List<Case> manger(Plateau plateau, int[] indexActuel) {
        List<Case> captures = new ArrayList<>();

        for(int i=0;i<2;i++){
            List<Case> line = plateau.getLine(indexActuel, i);

            List<List<Case>> splitLine = splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

            for (int j = 0; j < 2; j++) {
                for (Case c : splitLine.get(j)) {
                    if (!c.isEmpty() && c.getPiece().getCouleur() != this.getCouleur()) {
                        captures.add(c);
                        break;
                    } else if (!c.isEmpty()) {
                        break;
                    }
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
    protected Case switchPartie(Plateau plateau, int[] indexActuel, int isManger) {
        return null;
    }

    private List<List<Case>> splitLine(List<Case> line, Case separateur) {
        List<Case> line1 = new ArrayList<>();
        List<Case> line2 = new ArrayList<>();
        boolean firstPart = true;

        for (Case c : line) {
            if (c.equals(separateur)) {
                firstPart = false;
                continue;
            }
            if(firstPart){
                line1.add(c);
            }else{
                line2.add(c);
            }
        }
        line1=line1.reversed();

        List<List<Case>> result = new ArrayList<>(2);
        result.add(line1);
        result.add(line2);
        return result;
    }
    
}
