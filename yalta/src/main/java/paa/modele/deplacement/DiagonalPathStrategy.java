package paa.modele.deplacement;

import java.util.ArrayList;
import java.util.List;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class DiagonalPathStrategy implements PathStrategy {
    @Override
    public List<Case> getPath(Plateau plateau, int[] indexActuel, int direction,SwitchPartieStrategy switchPartie) {
    List<Case> diagonalCases = new ArrayList<>();
        Case temp;
        int[] newIndex;

        switch (direction) {
            case 0:
            for (int i = 0; i < 4; i++) {
                int j = indexActuel[1] + (indexActuel[0] - i);
                if (j >= 0 && j < 8) {
                    Case c = plateau.getCase(i, j, indexActuel[2]);
                    diagonalCases.add(c);
                }
            }
            if (diagonalCases.isEmpty()) {
                return diagonalCases;
            }
            int[] tempIndex=plateau.getIndexCase(diagonalCases.getLast());
            boolean transitionMiroir = isTransitionMiroir(tempIndex);
            int decalagePartie = transitionMiroir ? 1 : -1;
            temp=switchPartie.resolve(plateau,tempIndex,decalagePartie);
            if (temp == null) {
                return diagonalCases;
            }
            newIndex = plateau.getIndexCase(temp);
            if (newIndex == null) {
                return diagonalCases;
            }
            for (int i = 3; i >= 0; i--) {
                int j = transitionMiroir
                    ? newIndex[1] + (newIndex[0] - i)
                    : newIndex[1] - (newIndex[0] - i);
                if (j >= 0 && j < 8) {
                    Case c = plateau.getCase(i, j, newIndex[2]);
                    diagonalCases.add(c);
                }
            }
            break;
            case 1:
            for (int i = 0; i < 4; i++) {
                int j = indexActuel[1] - (indexActuel[0] - i);
                if (j >= 0 && j < 8) {
                    Case c = plateau.getCase(i, j, indexActuel[2]);
                    diagonalCases.add(c);
                }
            }
                if (diagonalCases.isEmpty()) {
                    return diagonalCases;
                }
                tempIndex=plateau.getIndexCase(diagonalCases.getLast());
                transitionMiroir = isTransitionMiroir(tempIndex);
                decalagePartie = transitionMiroir ? -1 : 1;
                temp=switchPartie.resolve(plateau,tempIndex,decalagePartie);
                if (temp == null) {
                    return diagonalCases;
                }
                newIndex = plateau.getIndexCase(temp);
                if (newIndex == null) {
                    return diagonalCases;
                }
                for (int i = 3; i >= 0; i--) {
                    int j = transitionMiroir
                        ? newIndex[1] - (newIndex[0] - i)
                        : newIndex[1] + (newIndex[0] - i);
                    if (j >= 0 && j < 8) {
                        Case c = plateau.getCase(i, j, newIndex[2]);
                        diagonalCases.add(c);
                    }
                }
                break;
            case 2: // cas centre plateau, même logique que cases 0/1 mais pour la diagonale centrale
                // On part du point de départ et on avance sur la diagonale centrale, puis on traverse les plateaux avec switchPartie
                for (int i = 0; i < 4; i++) {
                int j = indexActuel[1] - (indexActuel[0] - i);
                if (j >= 0 && j < 8) {
                    Case c = plateau.getCase(i, j, indexActuel[2]);
                    diagonalCases.add(c);
                }
            }
                if (diagonalCases.isEmpty()) {
                    return diagonalCases;
                }
                tempIndex=plateau.getIndexCase(diagonalCases.getLast());
                transitionMiroir = isTransitionMiroir(tempIndex);
                decalagePartie = transitionMiroir ? -1 : 1;
                temp=switchPartie.resolve(plateau,tempIndex,decalagePartie);
                if (temp == null) {
                    return diagonalCases;
                }
                newIndex = plateau.getIndexCase(temp);
                if (newIndex == null) {
                    return diagonalCases;
                }
                for (int i = 3; i >= 0; i--) {
                    int j = transitionMiroir
                        ? newIndex[1] - (newIndex[0] - i)
                        : newIndex[1] + (newIndex[0] - i);
                    if (j >= 0 && j < 8) {
                        Case c = plateau.getCase(i, j, newIndex[2]);
                        diagonalCases.add(c);
                    }
                }
                break;
            default:
                break;
        }

        return diagonalCases;
    }

    private boolean isTransitionMiroir(int[] indexActuel) {
        return (indexActuel[1] < 4 && indexActuel[2] == 2)
            || (indexActuel[1] >= 4 && indexActuel[2] == 0);
    }

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
