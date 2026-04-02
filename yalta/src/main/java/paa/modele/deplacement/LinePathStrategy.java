package paa.modele.deplacement;

import java.util.ArrayList;
import java.util.List;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class LinePathStrategy implements PathStrategy {

    @Override
    public List<Case> getPath(Plateau plateau, int[] indexActuel, int direction,SwitchPartieStrategy switchPartie) {
        List<Case> ligne = new ArrayList<>();
        Case temp;
        int[] newIndex;

        switch (direction) {
            case 0 -> {
                for (int i = 0; i < 8; i++) {
                    Case c = plateau.getCase(indexActuel[0], i, indexActuel[2]);
                    ligne.add(c);
                }
            }
            case 1 -> {
                for (int i = 0; i < 4; i++) {
                    Case c = plateau.getCase(i, indexActuel[1], indexActuel[2]);
                    ligne.add(c);
                }
                temp = switchPartie.resolve(plateau, indexActuel, 0);
                newIndex = plateau.getIndexCase(temp);
                for (int i = 3; i >= 0; i--) {
                    Case c = plateau.getCase(i, newIndex[1], newIndex[2]);
                    ligne.add(c);
                }
            }
        }
        return ligne;
    }

    @Override
    public List<List<Case>> splitLine(List<Case> line, Case separateur) {
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
