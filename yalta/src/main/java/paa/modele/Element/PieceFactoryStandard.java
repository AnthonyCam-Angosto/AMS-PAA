package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.vue.PieceView;

public class PieceFactoryStandard extends PieceFactory {
    @Override
    public Piece creerPiece(String type, Couleur couleur, SwitchPartieStrategy switchPartieStrategy) {
        Piece piece =switch(type.toLowerCase()) {
            case "pion" -> new Pion(couleur,switchPartieStrategy);
            case "tour" -> new Tour(couleur,switchPartieStrategy);
            case "cavalier" -> new Cavalier(couleur,switchPartieStrategy);
            case "fou" -> new Fou(couleur,switchPartieStrategy);
            case "reine" -> new Reine(couleur,switchPartieStrategy,new DiagonalSwitchPartieStrategy());
            case "roi" -> new Roi(couleur,switchPartieStrategy);
            default -> throw new IllegalArgumentException("Type de pièce inconnu: " + type);
        };
        piece.createView(new PieceView(type, couleur));
        return piece;
    }   
    
}
