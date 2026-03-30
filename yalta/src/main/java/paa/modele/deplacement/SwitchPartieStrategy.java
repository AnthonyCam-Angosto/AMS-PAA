package paa.modele.deplacement;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public interface SwitchPartieStrategy {
    Case resolve(Plateau plateau,int[] indexCase,int delta);
}
