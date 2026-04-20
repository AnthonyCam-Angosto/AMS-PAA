package paa.controler;

import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Partie;
import paa.modele.Utilisateur.IA;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class ActionPiece implements ActionCase {
    private final Case cellCase;

    public ActionPiece(Case cellCase) {
        this.cellCase = cellCase;
    }

    @Override
    public void click() {
        Couleur couleurJ=Partie.getInstance().getCouleurJoueurActuel();

        if(Partie.getInstance().getJoueurByCouleur(couleurJ) instanceof IA){
            return;
        }

        Piece piece = cellCase.getPiece();
        if(piece.getCouleur() == couleurJ){
            Plateau.getInstance().deselectionner();
            piece.coup_disponible(cellCase);
        }
    }
}
