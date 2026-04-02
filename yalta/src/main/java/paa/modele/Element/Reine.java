package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.deplacement.DiagonalPathStrategy;
import paa.modele.deplacement.LinePathStrategy;
import paa.modele.deplacement.PathStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Reine extends Piece {
    private final SwitchPartieStrategy diagonalSwitchPartie;

    private final PathStrategy linePath;
    private final PathStrategy diagonalPath;

    public Reine(Couleur couleur,SwitchPartieStrategy switchPartieStrategy,SwitchPartieStrategy diagonalSwitchPartie) {
        super(9, couleur,switchPartieStrategy);
        this.diagonalSwitchPartie = diagonalSwitchPartie;
        linePath=new LinePathStrategy();
        diagonalPath=new DiagonalPathStrategy();
    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();
        Case caseActuelle = plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]);

        for(int i=0;i<2;i++){
            List<Case> line = linePath.getPath(plateau, indexActuel, i, switchPartieStrategy);

            List<List<Case>> splitLine = linePath.splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

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
        List<Case> diagonaleClassique0 = diagonalPath.getPath(plateau, indexActuel, 0, diagonalSwitchPartie);
        List<Case> diagonaleClassique1 = diagonalPath.getPath(plateau, indexActuel, 1, diagonalSwitchPartie);

        for(int i=0;i<3;i++){
            System.out.println("test début getpath "+i);
            List<Case> line = diagonalPath.getPath(plateau, indexActuel, i, diagonalSwitchPartie);

            if (i == 2 && !(traverseCroisementMilieu(diagonaleClassique1, caseActuelle)||traverseCroisementMilieu(diagonaleClassique0, caseActuelle))) {
                continue;
            }

            List<List<Case>> splitLine = diagonalPath.splitLine(line, caseActuelle);

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
            List<Case> line = linePath.getPath(plateau, indexActuel, i, switchPartieStrategy);

            List<List<Case>> splitLine = linePath.splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

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


        Case caseActuelle = plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]);
        List<Case> diagonaleClassique0 = diagonalPath.getPath(plateau, indexActuel, 0, diagonalSwitchPartie);
        List<Case> diagonaleClassique1 = diagonalPath.getPath(plateau, indexActuel, 1, diagonalSwitchPartie);

        for(int i=0;i<3;i++){
            List<Case> line = diagonalPath.getPath(plateau, indexActuel, i, diagonalSwitchPartie);

            if (i == 2 && !(traverseCroisementMilieu(diagonaleClassique1, caseActuelle)||traverseCroisementMilieu(diagonaleClassique0, caseActuelle))) {
                continue;
            }

            List<List<Case>> splitLine = diagonalPath.splitLine(line, caseActuelle);

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

    private boolean traverseCroisementMilieu(List<Case> line, Case caseActuelle) {
        line.add(caseActuelle);
        java.util.Set<String> pivots = new java.util.HashSet<>();
        for (Case c : line) {
            String id = c.getId();
            if ("D4".equals(id) || "D5".equals(id) || "I5".equals(id) || "E9".equals(id) || "E4".equals(id) || "E10".equals(id) || "I9".equals(id)) {
                pivots.add(id);
            }
        }
        return pivots.size() >= 2;
    }
    
}
