package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.deplacement.LinePathStrategy;
import paa.modele.deplacement.PathStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Tour extends Piece {
    private PathStrategy pathStrategy;

    public Tour( Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(5, couleur,switchPartieStrategy);
        this.pathStrategy = new LinePathStrategy();

    }

    @Override
    protected List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        for(int i=0;i<2;i++){
            List<Case> line = pathStrategy.getPath(plateau, indexActuel, i, switchPartieStrategy);

            List<List<Case>> splitLine = pathStrategy.splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

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
            List<Case> line = pathStrategy.getPath(plateau, indexActuel, i, switchPartieStrategy);

            List<List<Case>> splitLine = pathStrategy.splitLine(line, plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]));

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
}
