package paa.modele.deplacement;

import java.util.List;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public interface PathStrategy {
    List<Case> getPath(Plateau plateau, int[] indexActuel, int direction, SwitchPartieStrategy switchPartie);

    List<List<Case>> splitLine(List<Case> line, Case caseActuelle);
}
