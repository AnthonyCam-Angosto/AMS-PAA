package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.vue.PieceView;

/**
 * Fabrique concrete de pieces du jeu.
 */
public class PieceFactory {
    /**
     * Crée une pièce du type spécifié et de la couleur donnée, en utilisant la stratégie de transition de partie fournie.
     * @param type
     * @param couleur
     * @param switchPartieStrategy
     * @return
     */
    public Piece createPiece(String type, Couleur couleur, SwitchPartieStrategy switchPartieStrategy) {
        Piece piece = switch (type.toLowerCase()) {
            case "pion" -> new Pion(couleur, switchPartieStrategy);
            case "tour" -> new Tour(couleur, switchPartieStrategy);
            case "cavalier" -> new Cavalier(couleur, switchPartieStrategy);
            case "fou" -> new Fou(couleur, switchPartieStrategy);
            case "reine" -> new Reine(couleur, switchPartieStrategy, new DiagonalSwitchPartieStrategy());
            case "roi" -> new Roi(couleur, switchPartieStrategy);
            default -> throw new IllegalArgumentException("Type de piece inconnu: " + type);
        };
        piece.createView(new PieceView(type, couleur));
        return piece;
    }
}
