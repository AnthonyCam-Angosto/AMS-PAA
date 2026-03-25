package paa.modele;

import java.util.List;

import javafx.scene.shape.Polygon;
import paa.modele.Element.Piece;

public class Plateau implements CaseComponent {
    private Case[][][] cases;

    private static Plateau instance = null;

    private Plateau() {
        cases = new Case[4][8][3];
        for (int i = 1; i < 4; i++) {
            initCases(i);
        }
        printCases();
    }

    private void printCases() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if(cases[i][j][k] != null){
                        System.out.print(cases[i][j][k].getId() + " ");
                    }else{
                        System.out.print("null ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static Plateau getInstance() {
        if (instance == null) {
            instance = new Plateau();
        }
        return instance;
    }

    //plateau1
    private void initCases(int partie) {
        String lettre = switch (partie) {
            case 1 -> "ABCDEFGH";
            case 2 -> "ABCDIJKL";
            case 3 -> "HGFEIJKL";
            default -> "";
        };
        int ecart= switch (partie) {
            case 2 -> 8;
            case 3 -> 12;
            default -> 0;
        };
        if(partie==1){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    cases[i][j][partie - 1] = new Case(lettre.charAt(j)+""+(i+1+ecart));
                }
            }
        }else{
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    cases[i][j][partie - 1] = new Case(lettre.charAt(j)+""+(ecart-i));
                }
            }
        }


    }

    public Case getCase(int x, int y, int z) {
        return cases[x][y][z];
    }

    public void setPolygons(List<Polygon> cellPolygons) {
        for (Polygon cell : cellPolygons) {
            String id = cell.getId();
            Case c = getCaseById(id);
            c.setPolygon(cell);
        }
    }

    public Case getCaseById(String id) {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if (cases[i][j][k] != null && cases[i][j][k].getId().equals(id)) {
                        return cases[i][j][k];
                    }
                }
            }
        }
        return null;
    }


    public void createPieces(){
        PieceFactoryStandard factory = new PieceFactoryStandard();
        Couleur[] couleurs = {Couleur.BLANC, Couleur.NOIR, Couleur.BLANC};

        String type = "pion";
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 8; i++) {
                Piece piece = factory.creerPiece(type, couleurs[k]);
                cases[1][i][k].setPiece(piece);
            }
            Piece piece = factory.creerPiece("tour", couleurs[k]);
            cases[0][0][k].setPiece(piece);
            piece = factory.creerPiece("tour", couleurs[k]);
            cases[0][7][k].setPiece(piece);

            piece = factory.creerPiece("cavalier", couleurs[k]);
            cases[0][1][k].setPiece(piece);
            piece = factory.creerPiece("cavalier", couleurs[k]);
            cases[0][6][k].setPiece(piece);

            piece = factory.creerPiece("fou", couleurs[k]);
            cases[0][2][k].setPiece(piece);
            piece = factory.creerPiece("fou", couleurs[k]);
            cases[0][5][k].setPiece(piece);
            
            piece = factory.creerPiece("reine", couleurs[k]);
            cases[0][3][k].setPiece(piece);
            piece = factory.creerPiece("roi", couleurs[k]);
            cases[0][4][k].setPiece(piece);
        }
        
    }


    @Override
    public void mettreAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
    }

    public static void main(String[] args) {
        Plateau plateau = Plateau.getInstance();
        plateau.printCases();
    }
}
