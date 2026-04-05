package paa.modele.Element;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class Roi extends Piece {

    public Roi( Couleur couleur,SwitchPartieStrategy switchPartieStrategy) {
        super(1000, couleur,switchPartieStrategy);
    }

    @Override
    public List<Case> deplacement(Plateau plateau, int[] indexActuel) {
        List<Case> deplacements = new ArrayList<>();

        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i==0 && j==0) continue;
                int newX=indexActuel[0]+i;
                int newY=indexActuel[1]+j;
                if(newX>=0 && newX<4 && newY>=0 && newY<8){
                    Case c = plateau.getCase(newX, newY, indexActuel[2]);
                    if (c.isEmpty()) {
                        deplacements.add(c);
                    }
                }
            }
        }
        if(indexActuel[0]==3){
            for(int i=-1;i<=1;i++){
                Case c=switchPartieStrategy.resolve(plateau,indexActuel,i);
                if(c.isEmpty()){
                deplacements.add(c);
                }
            }
        }
        return deplacements;
    }

    @Override
    public List<Case> manger(Plateau plateau, int[] indexActuel) {
        List<Case> captures = new ArrayList<>();

        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i==0 && j==0) continue;
                int newX=indexActuel[0]+i;
                int newY=indexActuel[1]+j;
                if(newX>=0 && newX<4 && newY>=0 && newY<8){
                    Case c = plateau.getCase(newX, newY, indexActuel[2]);
                    if (!c.isEmpty() && c.getPiece().getCouleur() != this.getCouleur()) {
                        captures.add(c);
                    }
                }
            }
        }
        if(indexActuel[0]==3){
            for(int i=-1;i<=1;i++){
                Case c=switchPartieStrategy.resolve(plateau,indexActuel,i);
                if(!c.isEmpty() && c.getPiece().getCouleur() != this.getCouleur()){
                captures.add(c);
                }
            }
        }
        return captures;
    }

    /**
     * Cases de roque (castling) possibles, ou liste vide si aucun roque n'est disponible.
     * @param plateau Plateau de jeu
     * @param indexActuel index de la case actuelle (reference pour trouver les cases de roque)
     * @return la liste des cases de roque possibles
     */
    @Override
    public List<Case> specials(Plateau plateau, int[] indexActuel) {
        List<Case> roquesPossibles = new ArrayList<>();
        Case caseRoi = plateau.getCase(indexActuel[0], indexActuel[1], indexActuel[2]);
        if (!(caseRoi.getPiece() instanceof Roi roi) || roi.hasMoved()) {
            return roquesPossibles;
        }

        // Le roque est limité à la rangée d'origine du roi.
        if (indexActuel[0] != 0) {
            return roquesPossibles;
        }

        Case petitRoque = getRoqueCase(plateau, indexActuel, 1);
        if (petitRoque != null) {
            roquesPossibles.add(petitRoque);
        }

        Case grandRoque = getRoqueCase(plateau, indexActuel, -1);
        if (grandRoque != null) {
            roquesPossibles.add(grandRoque);
        }

        return roquesPossibles;
    }

    private Case getRoqueCase(Plateau plateau, int[] indexActuel, int direction) {
        int x = indexActuel[0];
        int y = indexActuel[1];
        int z = indexActuel[2];

        int yDestinationRoi = y + (2 * direction);
        if (yDestinationRoi < 0 || yDestinationRoi > 7) {
            return null;
        }

        int yTour = direction > 0 ? 7 : 0;
        Case caseTour = plateau.getCase(x, yTour, z);
        if (caseTour.isEmpty() || !(caseTour.getPiece() instanceof Tour tour)) {
            return null;
        }

        if (tour.getCouleur() != this.getCouleur() || tour.hasMoved()) {
            return null;
        }

        // Toutes les cases entre roi et tour doivent être vides.
        for (int col = y + direction; col != yTour; col += direction) {
            if (!plateau.getCase(x, col, z).isEmpty()) {
                return null;
            }
        }

        return caseTour;
    }

}
