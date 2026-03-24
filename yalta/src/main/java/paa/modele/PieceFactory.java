package paa.modele;

import paa.modele.Element.Piece;

public abstract class PieceFactory {
    abstract public Piece creerPiece(String type,Couleur couleur);
}
