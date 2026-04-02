package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;

/**
 * Classe abstraite pour fabriquer des pièces du jeu, implement le pattern abstract factory
 */
public abstract class PieceFactory {
    /**
     * Crée une pièce du type spécifié et de la couleur donnée, en utilisant la stratégie de transition de partie fournie.
     * @param type
     * @param couleur
     * @param switchPartieStrategy
     * @return
     */
    abstract public Piece creerPiece(String type,Couleur couleur,SwitchPartieStrategy switchPartieStrategy);
}
