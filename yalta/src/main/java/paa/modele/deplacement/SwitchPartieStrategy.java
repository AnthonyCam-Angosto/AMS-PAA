package paa.modele.deplacement;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/**
 * Interface définissant une stratégie de résolution de transition de partie
 */
public interface SwitchPartieStrategy {
    /**
     * Résout la transition de partie en fonction de la position actuelle et si voulue un deplacement en diagonal après la transition
     * @param plateau Plateau de jeu
     * @param indexCase index de la case actuelle (avant la transition)
     * @param delta indique si on veut faire un deplacement en diagonal après la transition (1 pour la direction 1, -1 pour la direction 2, 0 pour deplacement en ligne droite)
     * @return case résolue après la transition
     */
    Case resolve(Plateau plateau,int[] indexCase,int delta);

    /**
     * Résout une transition de partie spéciale, pour les cas où une piece peut changer de 2 parties apartir de la même case (ex: le pion qui peut manger en diagonal dans les 2 directions ou le fou qui peut se déplacer en diagonal dans les 2 directions)
     * @param plateau Plateau de jeu
     * @param indexCase index de la case actuelle (avant la transition)
     * @param delta indique si on veut faire un deplacement en diagonal après la transition (1 pour la direction 1, -1 pour la direction 2, 0 pour deplacement en ligne droite)
     * @return case résolue après la transition
     */
    Case resolveSpecial(Plateau plateau,int[] indexCase,int delta);
}
