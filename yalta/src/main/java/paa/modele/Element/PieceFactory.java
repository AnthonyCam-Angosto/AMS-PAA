package paa.modele.Element;

import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;

public abstract class PieceFactory {
    abstract public Piece creerPiece(String type,Couleur couleur,SwitchPartieStrategy switchPartieStrategy);
}
