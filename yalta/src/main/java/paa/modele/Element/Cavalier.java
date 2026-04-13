package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Cavalier extends Piece {
    // Les 8 positions possibles du cavalier en forme de L
    private static final int[][] POSITIONS_CAVALIER = {
        {2, 1},   // +2 en x, +1 en y
        {2, -1},  // +2 en x, -1 en y
        {-2, 1},  // -2 en x, +1 en y
        {-2, -1}, // -2 en x, -1 en y
        {1, 2},   // +1 en x, +2 en y
        {1, -2},  // +1 en x, -2 en y
        {-1, 2},  // -1 en x, +2 en y
        {-1, -2}  // -1 en x, -2 en y
    };
    
    public Cavalier( Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(3, couleur,switchPartieStrategy);
    }

    @Override
    public List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();
        for (int[] position : POSITIONS_CAVALIER) {
            ajouterCoupCavalier(plateau, indexActuel, position[0], position[1], deplacements, false);
        }
        return deplacements;
    }

    @Override
    public List<Case> manger(Plateau plateau, int[] indexActuel) {
        List<Case> captures = new ArrayList<>();
        for (int[] position : POSITIONS_CAVALIER) {
            ajouterCoupCavalier(plateau, indexActuel, position[0], position[1], captures, true);
        }
        return captures;
    }

    private void ajouterCoupCavalier(Plateau plateau, int[] indexActuel, int dx, int dy, List<Case> resultats, boolean capture) {
        // Gestion spéciale des coups "2 en avant + 1 de côté" quand on approche de la transition de partie.
        if (dx == 1 && indexActuel[0] == 3) {
            Case caseTransition = switchPartieStrategy.resolve(plateau, indexActuel, 0);
            if (caseTransition != null) {
                int[] indexTransition = plateau.getIndexCase(caseTransition);
                int newX = indexTransition[0];
                int newY = indexTransition[1] + dy;
                if (dansPlateau(newX, newY)) {
                    Case cible = plateau.getCase(newX, newY, indexTransition[2]);
                    ajouterSiValide(cible, resultats, capture);
                }
            }

            boolean specialDx1 = estCaseE4(plateau, indexActuel)
                ? dy < 0
                : doitUtiliserTransitionSpeciale(plateau, indexActuel, dy);
            if (specialDx1) {
                int deltaSpecialDx1 = (estCaseE9(plateau, indexActuel) && dy < 0) ? 4 : 0;
                Case caseTransitionSpeciale = switchPartieStrategy.resolveSpecial(plateau, indexActuel, deltaSpecialDx1);
                if (caseTransitionSpeciale != null) {
                    int[] indexTransitionSpecial = plateau.getIndexCase(caseTransitionSpeciale);
                    int newXSpecial = indexTransitionSpecial[0];
                    int newYSpecial = indexTransitionSpecial[1] + dy;
                    if (dansPlateau(newXSpecial, newYSpecial)) {
                        Case cibleSpeciale = plateau.getCase(newXSpecial, newYSpecial, indexTransitionSpecial[2]);
                        ajouterSiValide(cibleSpeciale, resultats, capture);
                    }
                }
            }
            return;
        }

        if (dx == 2) {
            if (indexActuel[0] == 2) {
                int[] indexBord = {3, indexActuel[1], indexActuel[2]};
                Case caseTransition = switchPartieStrategy.resolve(plateau, indexBord, 0);
                if (caseTransition == null) {
                    return;
                }

                int[] indexTransition = plateau.getIndexCase(caseTransition);
                int newX = indexTransition[0];
                int newY = indexTransition[1] + dy;
                if (dansPlateau(newX, newY)) {
                    Case cible = plateau.getCase(newX, newY, indexTransition[2]);
                    ajouterSiValide(cible, resultats, capture);
                }

                if (doitUtiliserTransitionSpeciale(plateau, indexActuel, dy)) {
                    Case caseTransitionSpeciale = switchPartieStrategy.resolveSpecial(plateau, indexBord, 0);
                    if (caseTransitionSpeciale != null) {
                        int[] indexTransitionSpecial = plateau.getIndexCase(caseTransitionSpeciale);
                        int newXSpecial = indexTransitionSpecial[0];
                        int newYSpecial = indexTransitionSpecial[1] + dy;
                        if (dansPlateau(newXSpecial, newYSpecial)) {
                            Case cibleSpeciale = plateau.getCase(newXSpecial, newYSpecial, indexTransitionSpecial[2]);
                            ajouterSiValide(cibleSpeciale, resultats, capture);
                        }
                    }
                }
                return;
            }

            if (indexActuel[0] == 3) {
                Case caseTransition = switchPartieStrategy.resolve(plateau, indexActuel, 0);
                if (caseTransition == null) {
                    return;
                }

                int[] indexTransition = plateau.getIndexCase(caseTransition);
                int newX = indexTransition[0] - 1;
                int newY = indexTransition[1] + dy;
                if (dansPlateau(newX, newY)) {
                    Case cible = plateau.getCase(newX, newY, indexTransition[2]);
                    ajouterSiValide(cible, resultats, capture);
                }

                if (doitUtiliserTransitionSpeciale(plateau, indexActuel, dy)) {
                    int deltaSpecial = 0;
                    if (estCaseE4(plateau, indexActuel) && dy > 0) {
                        deltaSpecial = -2;
                    }
                    if (estCaseE9(plateau, indexActuel) && dy < 0) {
                        deltaSpecial = 2;
                    }
                    Case caseTransitionSpeciale = switchPartieStrategy.resolveSpecial(plateau, indexActuel, deltaSpecial);
                    if (caseTransitionSpeciale != null) {
                        int[] indexTransitionSpecial = plateau.getIndexCase(caseTransitionSpeciale);
                        int newXSpecial = indexTransitionSpecial[0] - 1;
                        int newYSpecial = indexTransitionSpecial[1] + dy;
                        if (dansPlateau(newXSpecial, newYSpecial)) {
                            Case cibleSpeciale = plateau.getCase(newXSpecial, newYSpecial, indexTransitionSpecial[2]);
                            ajouterSiValide(cibleSpeciale, resultats, capture);
                        }
                    }
                }
                return;
            }
        }

        int newX = indexActuel[0] + dx;
        int newY = indexActuel[1] + dy;
        if (!dansPlateau(newX, newY)) {
            return;
        }
        Case cible = plateau.getCase(newX, newY, indexActuel[2]);
        ajouterSiValide(cible, resultats, capture);
    }

    private boolean dansPlateau(int x, int y) {
        return x >= 0 && x < 4 && y >= 0 && y < 8;
    }

    private boolean doitUtiliserTransitionSpeciale(Plateau plateau, int[] indexActuel, int dy) {
        String id = plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]).getId();
        boolean caseCentreTroisParties = "D4".equals(id) || "D5".equals(id) || "I5".equals(id)
            || "E9".equals(id) || "E4".equals(id) || "E10".equals(id) || "I9".equals(id) || "I6".equals(id);
        if (!caseCentreTroisParties) {
            return false;
        }
        return (indexActuel[1] < 4 && dy < 0) || (indexActuel[1] >= 4 && dy > 0);
    }

    private boolean estCaseE4(Plateau plateau, int[] indexActuel) {
        return "E4".equals(plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]).getId());
    }

    private boolean estCaseE9(Plateau plateau, int[] indexActuel) {
        return "E9".equals(plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]).getId());
    }

    private void ajouterSiValide(Case cible, List<Case> resultats, boolean capture) {
        if (cible == null) {
            return;
        }

        if (capture) {
            if (!cible.isEmpty() && cible.getPiece().getCouleur() != this.getCouleur()) {
                resultats.add(cible);
            }
            return;
        }

        if (cible.isEmpty()) {
            resultats.add(cible);
        }
    }
    
}
