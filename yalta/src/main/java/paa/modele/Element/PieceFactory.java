package paa.modele.Element;

import paa.modele.Couleur;

public abstract class PieceFactory {
    abstract public Piece creerPiece(String type,Couleur couleur);
}
