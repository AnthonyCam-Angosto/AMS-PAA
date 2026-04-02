package paa.modele.deplacement;

import java.util.ArrayList;
import java.util.List;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class DiagonalPathStrategy implements PathStrategy {

    @Override
    public List<Case> getPath(Plateau plateau, int[] indexActuel, int direction,SwitchPartieStrategy switchPartie) {
        return switch (direction) {
            case 0 -> getPathCase0(plateau, indexActuel, switchPartie);
            case 1 -> getPathCase1(plateau, indexActuel, switchPartie);
            case 2 -> getPathCase2(plateau, indexActuel, switchPartie);
            default -> new ArrayList<>();
        };
    }

    /**
     * Récupère les cases diagonales dans la direction 0, en tenant compte des transitions de partie.
     * @param plateau
     * @param indexActuel
     * @param switchPartie
     * @return
     */
    private List<Case> getPathCase0(Plateau plateau, int[] indexActuel, SwitchPartieStrategy switchPartie) {
        List<Case> diagonalCases = new ArrayList<>();
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
        int[] tempIndex = plateau.getIndexCase(diagonalCases.getLast());
        boolean transitionMiroir = isTransitionMiroir(tempIndex);
        int decalagePartie = transitionMiroir ? 1 : -1;
        Case temp = switchPartie.resolve(plateau, tempIndex, decalagePartie);
        if (temp == null) {
            return diagonalCases;
        }
        int[] newIndex = plateau.getIndexCase(temp);
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
        return diagonalCases;
    }

    /**
     * Récupère les cases diagonales dans la direction 1, en tenant compte des transitions de partie.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver la diagonale)
     * @param switchPartie Stratégie de résolution de transition de partie à utiliser pour trouver la suite de la diagonale après la transition
     * @return Liste des cases de la diagonale dans la direction 1
     */
    private List<Case> getPathCase1(Plateau plateau, int[] indexActuel, SwitchPartieStrategy switchPartie) {
        List<Case> diagonalCases = new ArrayList<>();
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
        int[] tempIndex = plateau.getIndexCase(diagonalCases.getLast());
        boolean transitionMiroir = isTransitionMiroir(tempIndex);
        int decalagePartie = transitionMiroir ? -1 : 1;
        Case temp = switchPartie.resolve(plateau, tempIndex, decalagePartie);
        if (temp == null) {
            return diagonalCases;
        }
        int[] newIndex = plateau.getIndexCase(temp);
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
        return diagonalCases;
    }

    /**
     * Récupère les cases diagonales dans la direction 2, le debut et le meme que la direction 0 ou 1 selon la position, mais la suite est différente et dépend de la transition de partie
     * a utiliser seulement si une des 2 autre passe par 2 cases du centre
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle(reference pour trouver la diagonale)
     * @param switchPartie Stratégie de résolution de transition de partie à utiliser pour trouver la suite de la diagonale après la transition
     * @return Liste des cases de la diagonale dans la direction 2
     */
    private List<Case> getPathCase2(Plateau plateau, int[] indexActuel, SwitchPartieStrategy switchPartie) {
        List<Case> diagonalCases = new ArrayList<>();
        if (indexActuel[2] == 0 || (indexActuel[2] > 0 && indexActuel[1] < 4)) {
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
            int[] tempIndex = plateau.getIndexCase(diagonalCases.getLast());
            boolean transitionMiroir = isTransitionMiroir(tempIndex);
            int decalagePartie = transitionMiroir ? -1 : 1;
            Case temp = switchPartie.resolve(plateau, tempIndex, decalagePartie);
            if (temp == null) {
                return diagonalCases;
            }
            int[] newIndex = plateau.getIndexCase(temp);
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
        } else {
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
            int[] tempIndex = plateau.getIndexCase(diagonalCases.getLast());
            boolean transitionMiroir = isTransitionMiroir(tempIndex);
            int decalagePartie = transitionMiroir ? -1 : 1;
            Case temp = switchPartie.resolveSpecial(plateau, tempIndex, decalagePartie);
            if (temp == null) {
                return diagonalCases;
            }
            int[] newIndex = plateau.getIndexCase(temp);
            if (newIndex == null) {
                return diagonalCases;
            }
            boolean isMiroir = isTransitionMiroir(newIndex);
            for (int i = 3; i >= 0; i--) {
                int j = !isMiroir
                    ? newIndex[1] - (newIndex[0] - i)
                    : newIndex[1] + (newIndex[0] - i);
                if (j >= 0 && j < 8) {
                    Case c = plateau.getCase(i, j, newIndex[2]);
                    diagonalCases.add(c);
                }
            }
        }
        return diagonalCases;
    }

    /**
     * Détermine si la transition de partie correspond à une transition miroir, 
     * c'est-à-dire si elle se produit entre les parties 0 et 2 ou entre les parties 1 et 3.
     * @param indexActuel index de la case après la transition
     * @return true si la transition est un miroir, false sinon
     */
    private boolean isTransitionMiroir(int[] indexActuel) {
        return (indexActuel[1] < 4 && indexActuel[2] == 2)
            || (indexActuel[1] >= 4 && indexActuel[2] == 0);
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
