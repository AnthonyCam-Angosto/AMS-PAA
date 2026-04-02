package paa.modele.plateau;


import paa.controler.ActionManger;
import paa.controler.ActionPiece;
import paa.controler.ActionVide;
import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Element.PieceFactoryStandard;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;
import paa.modele.Partie;

/**
 * Représente le plateau de jeu, composé de 3 parties de 4x8 cases chacune, et gère les pièces et les déplacements sur le plateau.
 * Le plateau est implémenté en tant que singleton pour garantir qu'il n'y ait qu'une seule instance de plateau dans le jeu.
 */
public class Plateau implements CaseComponent {
    private final Case[][][] cases;

    private static Plateau instance = null;

    private Plateau() {
        cases = new Case[4][8][3];
        for (int i = 1; i < 4; i++) {
            initCases(i);
        }
    }

    /**
     * Retourne l'instance unique du plateau de jeu, en la créant si elle n'existe pas encore.
     * @return L'instance du plateau de jeu
     */
    public static Plateau getInstance() {
        if (instance == null) {
            instance = new Plateau();
        }
        return instance;
    }

    /**
     * Initialise les cases d'une partie des 3 parties du plateau
     * @param partie Numéro de la partie à initialiser (1, 2 ou 3)
     */
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

    /**
     * Récupère la case du plateau correspondant aux coordonnées x, y et z spécifiées.
     * @param x Coordonnée x de la case (0 à 3)
     * @param y Coordonnée y de la case (0 à 7)
     * @param z Numéro de la partie (0 à 2)
     * @return La case correspondante aux coordonnées spécifiées, ou null si les coordonnées sont invalides
     */
    public Case getCase(int x, int y, int z) {
        return cases[x][y][z];
    }

    public int[] getIndexCase(Case c) {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if (cases[i][j][k].getId().equals(c.getId())) {
                        return new int[]{i, j, k};
                    }
                }
            }
        }
        return null;
    }

    /**
     * Récupère la case du plateau correspondant à l'identifiant spécifié.
     * @param id Identifiant de la case à récupérer
     * @return La case correspondante à l'identifiant spécifié, ou null si aucune case ne correspond à cet identifiant
     */
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


    /**
     * Crée les pièces sur le plateau pour chaque partie.
     */
    public void createPieces(){
        PieceFactoryStandard factory = new PieceFactoryStandard();
        Couleur[] couleurs = Partie.getInstance().getOrdre();
        SwitchPartieStrategy switchPartieStrategy2 = new DiagonalSwitchPartieStrategy();
        SwitchPartieStrategy switchPartieStrategy = new StandardSwitchPartieStrategy();

        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 8; i++) {
                Piece piece = factory.creerPiece("pion", couleurs[k], switchPartieStrategy);
                cases[1][i][k].setPiece(piece);
            }
            Piece piece = factory.creerPiece("tour", couleurs[k], switchPartieStrategy);
            cases[0][0][k].setPiece(piece);
            piece = factory.creerPiece("tour", couleurs[k], switchPartieStrategy);
            cases[0][7][k].setPiece(piece);

            piece = factory.creerPiece("cavalier", couleurs[k], switchPartieStrategy);
            cases[0][1][k].setPiece(piece);
            piece = factory.creerPiece("cavalier", couleurs[k], switchPartieStrategy);
            cases[0][6][k].setPiece(piece);

            piece = factory.creerPiece("fou", couleurs[k], switchPartieStrategy2);
            cases[0][2][k].setPiece(piece);
            piece = factory.creerPiece("fou", couleurs[k], switchPartieStrategy2);
            cases[0][5][k].setPiece(piece);
            
            piece = factory.creerPiece("reine", couleurs[k], switchPartieStrategy);
            cases[0][3][k].setPiece(piece);
            piece = factory.creerPiece("roi", couleurs[k], switchPartieStrategy);
            cases[0][4][k].setPiece(piece);
        }
        
    }

    /**
     * Déplace une pièce d'une case de départ vers une case d'arrivée, en mettant à jour les pièces sur les cases et en gérant les tours de jeu.
     * @param caseDepart Case de départ
     * @param caseArrivee Case d'arrivée
     */
    public void deplacementPiece(Case caseDepart, Case caseArrivee) {
        Piece piece = caseDepart.getPiece();
        if (piece != null) {
            caseArrivee.setPiece(piece);
            caseDepart.setPiece(null);
        }
        deselectionner();
        Partie.getInstance().tourSuivant();
    }

    @Override
    public void deselectionner() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if(cases[i][j][k].getAction() instanceof ActionPiece || cases[i][j][k].getAction() instanceof ActionVide){}
                    else if(cases[i][j][k].getAction() instanceof ActionManger){
                        cases[i][j][k].setAction(new ActionPiece(cases[i][j][k]));
                    }else{
                        cases[i][j][k].deselectionner();
                    }
                }
            }
        }
    }    
    

}
