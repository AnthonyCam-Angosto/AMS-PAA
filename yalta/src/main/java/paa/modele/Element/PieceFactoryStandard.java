package paa.modele.Element;

import paa.modele.Couleur;
import paa.vue.PieceView;

public class PieceFactoryStandard extends PieceFactory {
    @Override
    public Piece creerPiece(String type, Couleur couleur) {
        Piece piece =switch(type.toLowerCase()) {
            case "pion" -> new Pion(couleur);
            case "tour" -> new Tour(couleur);
            case "cavalier" -> new Cavalier(couleur);
            case "fou" -> new Fou(couleur);
            case "reine" -> new Reine(couleur);
            case "roi" -> new Roi(couleur);
            default -> throw new IllegalArgumentException("Type de pièce inconnu: " + type);
        };
        piece.createView(new PieceView(type, couleur));
        return piece;
    }   
    
}
