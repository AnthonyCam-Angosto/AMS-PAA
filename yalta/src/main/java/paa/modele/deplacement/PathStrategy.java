package paa.modele.deplacement;

import java.util.List;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/**
 * Interface définissant une stratégie de récupération d'une ligne ou d'une diagonale de cases à partir d'une case donnée
 */
public interface PathStrategy {
    /**
     * recupere une ligne ou une diagonale de cases contenant une case voulue
     * @param plateau Plateau de jeu
     * @param indexActuel case indiquand la ligne ou la diagonale à récupérer
     * @param direction direction de la ligne ou de la diagonale à récupérer (0, 1 ou 2)
     * @param switchPartie Stratégie de résolution de transition de partie à utiliser pour trouver la suite de la ligne ou de la diagonale après la transition
     * @return Liste des cases de la ligne ou de la diagonale dans la direction spécifiée
     */
    List<Case> getPath(Plateau plateau, int[] indexActuel, int direction, SwitchPartieStrategy switchPartie);

    /**
     * Divise une ligne ou une diagonale en deux parties à partir de la case contenant la piece à déplacer.
     * @param line ligne ou diagonale à diviser
     * @param caseActuelle case contenant la piece à déplacer, utilisée comme séparateur pour diviser la ligne ou la diagonale
     * @return liste contenant les deux parties de la ligne ou de la diagonale, la première partie contenant les cases avant la case actuelle et la deuxième partie contenant les cases après la case actuelle
     */
    List<List<Case>> splitLine(List<Case> line, Case caseActuelle);
}
