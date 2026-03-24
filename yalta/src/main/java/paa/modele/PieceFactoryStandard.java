package paa.modele;

import paa.modele.Element.Cavalier;
import paa.modele.Element.Fous;
import paa.modele.Element.Piece;
import paa.modele.Element.Pion;
import paa.modele.Element.Reine;
import paa.modele.Element.Roi;
import paa.modele.Element.Tour;

public class PieceFactoryStandard extends PieceFactory {
    @Override
    public Piece creerPiece(String type, Couleur couleur) {
        return switch(type.toLowerCase()) {
            case "pion" -> new Pion(couleur);
            case "tour" -> new Tour(couleur);
            case "cavalier" -> new Cavalier(couleur);
            case "fous" -> new Fous(couleur);
            case "reine" -> new Reine(couleur);
            case "roi" -> new Roi(couleur);
            default -> throw new IllegalArgumentException("Type de pièce inconnu: " + type);
        };
    }   
    
}
