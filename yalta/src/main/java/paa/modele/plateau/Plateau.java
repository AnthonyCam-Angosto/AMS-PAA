package paa.modele.plateau;


import java.util.List;

import paa.controler.ActionManger;
import paa.controler.ActionPiece;
import paa.controler.ActionSpecial;
import paa.controler.ActionVide;
import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Element.PieceFactory;
import paa.modele.Element.Pion;
import paa.modele.Partie;
import paa.modele.Prototype;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;

/**
 * Représente le plateau de jeu, composé de 3 parties de 4x8 cases chacune, et gère les pièces et les déplacements sur le plateau.
 * Le plateau est implémenté en tant que singleton pour garantir qu'il n'y ait qu'une seule instance de plateau dans le jeu.
 */
public class Plateau implements CaseComponent, Prototype<Plateau> {
    private Case[][][] cases;

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

    public void reinitialiser(){
        cases = new Case[4][8][3];
        for (int i = 1; i < 4; i++) {
            initCases(i);
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
        PieceFactory factory = new PieceFactory();
        Couleur[] couleurs = Partie.getInstance().getOrdre();
        SwitchPartieStrategy switchPartieStrategy2 = new DiagonalSwitchPartieStrategy();
        SwitchPartieStrategy switchPartieStrategy = new StandardSwitchPartieStrategy();

        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 8; i++) {
                Piece piece = factory.createPiece("pion", couleurs[k], switchPartieStrategy);
                cases[1][i][k].setPiece(piece);
            }
            Piece piece = factory.createPiece("tour", couleurs[k], switchPartieStrategy);
            cases[0][0][k].setPiece(piece);
            piece = factory.createPiece("tour", couleurs[k], switchPartieStrategy);
            cases[0][7][k].setPiece(piece);

            piece = factory.createPiece("cavalier", couleurs[k], switchPartieStrategy);
            cases[0][1][k].setPiece(piece);
            piece = factory.createPiece("cavalier", couleurs[k], switchPartieStrategy);
            cases[0][6][k].setPiece(piece);

            piece = factory.createPiece("fou", couleurs[k], switchPartieStrategy2);
            cases[0][2][k].setPiece(piece);
            piece = factory.createPiece("fou", couleurs[k], switchPartieStrategy2);
            cases[0][5][k].setPiece(piece);
            
            
            piece = factory.createPiece("reine", couleurs[k], switchPartieStrategy);
            cases[0][3][k].setPiece(piece);
            piece = factory.createPiece("roi", couleurs[k], switchPartieStrategy);
            cases[0][4][k].setPiece(piece);
        }
    }

    /**
     * Crée une copie profonde du plateau, sans observateurs ni état graphique.
     * Cette copie est destinée aux simulations de l'IA.
     * @return un nouveau plateau indépendant du singleton courant
     */
    @Override
    public Plateau copy() {
        Plateau copie = new Plateau();

        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    copie.cases[i][j][k] = this.cases[i][j][k].copy();
                }
            }
        }
        return copie;
    }

    /**
     * Déplace une pièce d'une case de départ vers une case d'arrivée, en mettant à jour les pièces sur les cases et en gérant les tours de jeu.
     * @param caseDepart Case de départ
     * @param caseArrivee Case d'arrivée
     * @param notTurn Indique si le déplacement n'est pas un tour de jeu
     */
    public void deplacementPiece(Case caseDepart, Case caseArrivee,boolean notTurn) {
        Piece piece = caseDepart.getPiece();
        if (piece != null) {
            if (piece instanceof Pion && caseArrivee.isEmpty()) {
                capturerEnPassant(caseDepart, caseArrivee);
            }
            piece.setHasMoved(true);
            if(piece instanceof Pion pion){
                if(pion.getEnPassantId()!=null && pion.getEnPassantId().equals(caseArrivee.getId())){
                    pion.setEnPassant(true);
                }
            }
            caseArrivee.setPiece(piece);
            caseDepart.setPiece(null);
        }
        deselectionner();
        if(!notTurn){
            Partie.getInstance().tourSuivant();
        }
    }

    private void capturerEnPassant(Case caseDepart, Case caseArrivee) {
        int[] indexDepart = getIndexCase(caseDepart);
        int[] indexArrivee = getIndexCase(caseArrivee);

        if(indexDepart == null || indexArrivee == null) {
            return;
        }

        int deltaY = indexArrivee[1] - indexDepart[1];
        if(Math.abs(deltaY) != 1) {
            return;
        }

        Case caseCapture = getCase(indexDepart[0], indexArrivee[1], indexDepart[2]);
        if(caseCapture == null || caseCapture.getPiece() == null || !(caseCapture.getPiece() instanceof Pion pionCapture)) {
            return;
        }

        if(pionCapture.getCouleur() == caseDepart.getPiece().getCouleur()) {
            return;
        }

        if(pionCapture.isEnPassant()) {
            caseCapture.setPiece(null);
        }
    }

    @Override
    public void deselectionner() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if(cases[i][j][k].getAction() instanceof ActionPiece || cases[i][j][k].getAction() instanceof ActionVide){}
                    else if(cases[i][j][k].getAction() instanceof ActionManger || (cases[i][j][k].getAction() instanceof ActionSpecial && cases[i][j][k].getPiece()==null)){
                        cases[i][j][k].setAction(new ActionPiece(cases[i][j][k]));
                    }else{
                        cases[i][j][k].deselectionner();
                    }
                }
            }
        }
    }

    /**
     * Effectue le roque en déplaçant le roi et la tour concernés, en mettant à jour les pièces sur les cases et changeant les tours de jeu.
     * @param caseRoi Case du roi à déplacer
     * @param caseTour Case de la tour à déplacer
     */
    public void castling(Case caseRoi, Case caseTour){
        Piece roi = caseRoi.getPiece();
        Piece tour = caseTour.getPiece();

        int[] indexRoi = getIndexCase(caseRoi);
        int[] indexTour = getIndexCase(caseTour);

        int direction = Integer.compare(indexTour[1], indexRoi[1]);

        Case caseArriveeRoi = getCase(indexRoi[0], indexRoi[1]+(2*direction), indexRoi[2]);
        Case caseArriveeTour = getCase(indexRoi[0], indexRoi[1]+direction, indexRoi[2]);

        roi.setHasMoved(true);
        tour.setHasMoved(true);

        caseArriveeRoi.setPiece(roi);
        caseRoi.setPiece(null);
        caseArriveeTour.setPiece(tour);
        caseTour.setPiece(null);

        deselectionner();
        Partie.getInstance().tourSuivant();
    }

    /**
     * Effectue la promotion d'un pion en déplaçant le pion vers la case de promotion, en mettant à jour les pièces sur les cases et en gérant les tours de jeu.
     * @param casePion Case du pion à promouvoir
     * @param casePromotion Case de promotion vers laquelle déplacer le pion
     */
    public void promotion(Case casePion, Case casePromotion) {
        deplacementPiece(casePion, casePromotion,true);
        casePromotion.notifyPromotion(casePromotion);
    }

    /**
     * Effectue la fin de la promotion en remplaçant le pion promu par la pièce choisie par le joueur, en mettant à jour les pièces sur les cases et en gérant les tours de jeu.
     * @param casePromotion Case de promotion où le pion a été déplacé
     * @param type Type de pièce choisie pour la promotion (ex: "reine", "tour", "fou", "cavalier")
     */
    public void finPromotion(Case casePromotion,String type) {
        Piece pion = casePromotion.getPiece();
        PieceFactory factory = new PieceFactory();
        Piece piecePromue = factory.createPiece(type, pion.getCouleur(),new StandardSwitchPartieStrategy());
        casePromotion.setPiece(piecePromue);
        deselectionner();
        Partie.getInstance().tourSuivant();
    }

    /**
     * Récupère une liste de toutes les cases du plateau qui contiennent une pièce.
     * @return Une liste de toutes les cases du plateau qui contiennent une pièce
     */
    public List<Case> getAllCasePiece(){
        List<Case> casesPiece = new java.util.ArrayList<>();
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if (!cases[i][j][k].isEmpty()) {
                        casesPiece.add(cases[i][j][k]);
                    }
                }
            }
        }
        return casesPiece;
    }
    

}
