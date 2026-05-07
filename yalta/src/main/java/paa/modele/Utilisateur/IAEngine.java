package paa.modele.Utilisateur;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Element.PieceFactory;
import paa.modele.Element.Pion;
import paa.modele.Element.Roi;
import paa.modele.Partie;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/** Représente un moteur d'intelligence artificielle pour le jeu. */
public abstract class IAEngine {
    protected final Couleur couleurIA;

    public IAEngine(Couleur couleurIA) {
        this.couleurIA = couleurIA;
    }

    /**
     * Évalue un coup donné sur le plateau à une certaine profondeur de recherche.
     * @param plateau Le plateau de jeu actuel
     * @param coup Le coup à évaluer
     * @param profondeur La profondeur de recherche pour l'évaluation du coup
     * @return Un score représentant la qualité du coup pour l'IA (plus élevé est meilleur pour l'IA)
     */
    abstract protected int evaluerCoup(Plateau plateau, Coup coup, int profondeur);

    /**
     * Génère une liste de tous les coups légaux possibles pour un joueur sur le plateau donné.
     * @param plateau Le plateau de jeu actuel
     * @param couleurJoueur La couleur du joueur pour whom générer les coups
     * @return Une liste de tous les coups légaux possibles
     */
    public List<Coup> getLegalMoves(Plateau plateau, Couleur couleurJoueur) {
        List<Coup> coups = new ArrayList<>();
        for (Case casePiece : plateau.getAllCasePiece()) {
            Piece piece = casePiece.getPiece();
            if (piece.getCouleur() != couleurJoueur) {
                continue;
            }
            int[] indexPiece = plateau.getIndexCase(casePiece);
            List<Case> deplacements = piece.deplacement(plateau, indexPiece);
            List<Case> captures = piece.manger(plateau, indexPiece);
            List<Case> specials = piece.specials(plateau, indexPiece);

            for (Case action : deplacements) {
                Coup coup = new Coup(casePiece.getId(), action.getId(), Coup.Type.NORMAL);
                if (isLegalMoveOnPlateau(couleurJoueur, casePiece, action, plateau, coup.type)) {
                    coups.add(coup);
                }
            }

            for (Case action : captures) {
                Coup coup = new Coup(casePiece.getId(), action.getId(), Coup.Type.NORMAL);
                if (isLegalMoveOnPlateau(couleurJoueur, casePiece, action, plateau, coup.type)) {
                    coups.add(coup);
                }
            }

            Coup.Type typeSpecial = getTypeSpecial(piece);
            for (Case action : specials) {
                Coup coup = new Coup(casePiece.getId(), action.getId(), typeSpecial);
                if (isLegalMoveOnPlateau(couleurJoueur, casePiece, action, plateau, coup.type)) {
                    coups.add(coup);
                }
            }
        }
        return coups;
    }

    /**
     * Détermine le type de coup spécial (roque ou promotion) en fonction de la pièce impliquée.
     * @param piece La pièce pour laquelle déterminer le type de coup spécial
     * @return Le type de coup spécial associé à la pièce (ROQUE pour un roi, PROMOTION pour un pion, NORMAL sinon)
     */
    private Coup.Type getTypeSpecial(Piece piece) {
        if (piece instanceof Roi) {
            return Coup.Type.ROQUE;
        }
        if (piece instanceof Pion) {
            return Coup.Type.PROMOTION;
        }
        return Coup.Type.NORMAL;
    }

    /**
     * Vérifie si un coup est légal sur le plateau en tenant compte des règles du jeu, y compris les échecs et les coups spéciaux.
     * @param couleurJoueur La couleur du joueur pour whom vérifier le coup
     * @param depart La case de départ du coup
     * @param arrivee La case d'arrivée du coup
     * @param plateau Le plateau de jeu actuel
     * @param typeCoup Le type de coup à vérifier
     * @return true si le coup est légal, false sinon
     */
    protected boolean isLegalMoveOnPlateau(Couleur couleurJoueur, Case depart, Case arrivee, Plateau plateau, Coup.Type typeCoup) {
        if (typeCoup == Coup.Type.ROQUE) {
            return isLegalCastlingOnPlateau(couleurJoueur, depart, arrivee, plateau);
        }

        Piece pieceDepart = depart.getPiece();
        if (pieceDepart == null) {
            return false;
        }

        Piece pieceArrivee = arrivee.getPiece();
        Case caseCaptureEnPassant = null;
        Piece pieceCaptureEnPassant = null;

        int[] indexDepart = plateau.getIndexCase(depart);
        int[] indexArrivee = plateau.getIndexCase(arrivee);

        if (pieceDepart instanceof Pion && pieceArrivee == null && indexDepart != null && indexArrivee != null
                && indexDepart[0] != indexArrivee[0] && indexDepart[1] != indexArrivee[1]) {
            caseCaptureEnPassant = plateau.getCase(indexDepart[0], indexArrivee[1], indexDepart[2]);
            if (caseCaptureEnPassant != null) {
                pieceCaptureEnPassant = caseCaptureEnPassant.getPiece();
                caseCaptureEnPassant.setPiece(null);
            }
        }

        depart.setPiece(null);
        arrivee.setPiece(pieceDepart);
        boolean legal = !echecSurPlateau(couleurJoueur, plateau);
        arrivee.setPiece(pieceArrivee);
        depart.setPiece(pieceDepart);
        if (caseCaptureEnPassant != null) {
            caseCaptureEnPassant.setPiece(pieceCaptureEnPassant);
        }
        return legal;
    }

    /**
     * Vérifie si un roque est légal sur le plateau en tenant compte des règles du jeu, y compris les échecs et les cases intermédiaires.
     * @param couleurJoueur La couleur du joueur pour whom vérifier le roque
     * @param caseRoi La case du roi
     * @param caseTour La case de la tour
     * @param plateau Le plateau de jeu actuel
     * @return true si le roque est légal, false sinon
     */
    private boolean isLegalCastlingOnPlateau(Couleur couleurJoueur, Case caseRoi, Case caseTour, Plateau plateau) {
        if (echecSurPlateau(couleurJoueur, plateau)) {
            return false;
        }

        Piece roi = caseRoi.getPiece();
        Piece tour = caseTour.getPiece();
        if (!(roi instanceof Roi) || tour == null) {
            return false;
        }

        int[] indexRoi = plateau.getIndexCase(caseRoi);
        int[] indexTour = plateau.getIndexCase(caseTour);
        if (indexRoi == null || indexTour == null) {
            return false;
        }

        int direction = Integer.compare(indexTour[1], indexRoi[1]);
        Case caseIntermediaireRoi = plateau.getCase(indexRoi[0], indexRoi[1] + direction, indexRoi[2]);
        Case caseArriveeRoi = plateau.getCase(indexRoi[0], indexRoi[1] + (2 * direction), indexRoi[2]);
        Case caseArriveeTour = plateau.getCase(indexRoi[0], indexRoi[1] + direction, indexRoi[2]);
        if (caseIntermediaireRoi == null || caseArriveeRoi == null || caseArriveeTour == null) {
            return false;
        }

        caseRoi.setPiece(null);
        caseTour.setPiece(null);
        caseArriveeRoi.setPiece(roi);
        caseArriveeTour.setPiece(tour);

        boolean legal = !caseEstAttaquee(caseIntermediaireRoi, couleurJoueur, plateau)
                && !echecSurPlateau(couleurJoueur, plateau);

        caseArriveeTour.setPiece(null);
        caseArriveeRoi.setPiece(null);
        caseTour.setPiece(tour);
        caseRoi.setPiece(roi);

        return legal;
    }

    /**
     * Vérifie si une case est attaquée par une pièce adverse sur le plateau.
     * @param caseCible La case à vérifier
     * @param couleurJoueur La couleur du joueur pour whom vérifier l'attaque
     * @param plateau Le plateau de jeu actuel
     * @return true si la case est attaquée par une pièce adverse, false sinon
     */
    private boolean caseEstAttaquee(Case caseCible, Couleur couleurJoueur, Plateau plateau) {
        for (Case casePiece : plateau.getAllCasePiece()) {
            Piece piece = casePiece.getPiece();
            if (piece.getCouleur() == couleurJoueur) {
                continue;
            }
            int[] indexPiece = plateau.getIndexCase(casePiece);
            if (indexPiece == null) {
                continue;
            }
            List<Case> captures = piece.manger(plateau, indexPiece);
            if (captures.contains(caseCible)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si le roi du joueur est en échec sur le plateau.
     * @param couleurJoueur La couleur du joueur pour whom vérifier l'échec
     * @param plateau Le plateau de jeu actuel
     * @return true si le roi du joueur est en échec, false sinon
     */
    protected boolean echecSurPlateau(Couleur couleurJoueur, Plateau plateau) {
        Case caseRoi = null;
        List<Case> casesAvecPiece = plateau.getAllCasePiece();
        for (Case c : casesAvecPiece) {
            if (c.getPiece() instanceof Roi && c.getPiece().getCouleur() == couleurJoueur) {
                caseRoi = c;
                break;
            }
        }
        for (Case casePiece : casesAvecPiece) {
            if (casePiece.getPiece().getCouleur() == couleurJoueur) {
                continue;
            }
            int[] indexPiece = plateau.getIndexCase(casePiece);
            List<Case> captures = casePiece.getPiece().manger(plateau, indexPiece);
            if (captures.contains(caseRoi)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applique un coup sur le plateau en tenant compte des règles du jeu, y compris les échecs et les coups spéciaux.
     * @param plateau Le plateau de jeu actuel
     * @param coup Le coup à appliquer
     * @param notTurn true si ce n'est pas le tour du joueur, false sinon
     */
    protected void appliquerCoup(Plateau plateau, Coup coup, boolean notTurn) {
        Case depart = plateau.getCaseById(coup.departId);
        Case arrivee = plateau.getCaseById(coup.arriveeId);

        if (depart == null || arrivee == null || depart.getPiece() == null) {
            return;
        }

        switch (coup.type) {
            case ROQUE -> appliquerRoque(plateau, depart, arrivee, notTurn);
            case PROMOTION -> appliquerPromotion(plateau, depart, arrivee, notTurn);
            case NORMAL -> plateau.deplacementPiece(depart, arrivee, notTurn);
        }
    }

    /**
     * Applique un roque sur le plateau en tenant compte des règles du jeu, y compris les échecs et les cases intermédiaires.
     * @param plateau Le plateau de jeu actuel
     * @param caseRoi La case du roi
     * @param caseTour La case de la tour
     * @param notTurn true si ce n'est pas le tour du joueur, false sinon
     */
    private void appliquerRoque(Plateau plateau, Case caseRoi, Case caseTour, boolean notTurn) {
        Piece roi = caseRoi.getPiece();
        Piece tour = caseTour.getPiece();

        if (!(roi instanceof Roi) || tour == null) {
            return;
        }

        int[] indexRoi = plateau.getIndexCase(caseRoi);
        int[] indexTour = plateau.getIndexCase(caseTour);
        if (indexRoi == null || indexTour == null) {
            return;
        }

        int direction = Integer.compare(indexTour[1], indexRoi[1]);
        Case caseArriveeRoi = plateau.getCase(indexRoi[0], indexRoi[1] + (2 * direction), indexRoi[2]);
        Case caseArriveeTour = plateau.getCase(indexRoi[0], indexRoi[1] + direction, indexRoi[2]);
        if (caseArriveeRoi == null || caseArriveeTour == null) {
            return;
        }

        roi.setHasMoved(true);
        tour.setHasMoved(true);

        caseRoi.setPiece(null);
        caseTour.setPiece(null);
        caseArriveeRoi.setPiece(roi);
        caseArriveeTour.setPiece(tour);
        plateau.deselectionner();

        if (!notTurn) {
            Partie.getInstance().tourSuivant();
        }
    }

    /**
     * Applique une promotion sur le plateau en tenant compte des règles du jeu, y compris les échecs et les coups spéciaux.
     * @param plateau Le plateau de jeu actuel
     * @param casePion La case du pion à promouvoir
     * @param casePromotion La case d'arrivée du pion promu
     * @param notTurn true si ce n'est pas le tour du joueur, false sinon
     */
    private void appliquerPromotion(Plateau plateau, Case casePion, Case casePromotion, boolean notTurn) {
        plateau.deplacementPiece(casePion, casePromotion, true);

        Piece piecePromue = new PieceFactory().createPiece(
                "reine",
                casePromotion.getPiece().getCouleur(),
                new StandardSwitchPartieStrategy()
        );
        casePromotion.setPiece(piecePromue);
        plateau.deselectionner();

        if (!notTurn) {
            Partie.getInstance().tourSuivant();
        }
    }

    /**
     * Détermine la couleur du joueur suivant dans l'ordre de jeu.
     * @param couleurActuelle La couleur du joueur actuel
     * @return La couleur du joueur suivant dans l'ordre de jeu
     */
    protected Couleur couleurSuivante(Couleur couleurActuelle) {
        Couleur[] ordre = Partie.getInstance().getOrdre();
        for (int i = 0; i < ordre.length; i++) {
            if (ordre[i] == couleurActuelle) {
                return ordre[(i + 1) % ordre.length];
            }
        }
        return ordre[0];
    }

    /**
     * Vérifie si le plateau est dans une position terminale, c'est-à-dire si l'un des rois n'est plus en vie.
     * @param plateau Le plateau de jeu actuel
     * @return true si le plateau est dans une position terminale, false sinon
     */
    protected boolean estTerminal(Plateau plateau) {
        return !roiEnVie(plateau, Couleur.BLANC)
                || !roiEnVie(plateau, Couleur.NOIR)
                || !roiEnVie(plateau, Couleur.ROUGE);
    }

    /**
     * Vérifie si le roi d'une couleur donnée est en vie sur le plateau.
     * @param plateau Le plateau de jeu actuel
     * @param couleur La couleur du roi à vérifier
     * @return true si le roi de la couleur donnée est en vie, false sinon
     */
    private boolean roiEnVie(Plateau plateau, Couleur couleur) {
        for (Case c : plateau.getAllCasePiece()) {
            if (c.getPiece().getCouleur() == couleur && c.getPiece() instanceof Roi) {
                return true;
            }
        }
        return false;
    }

    /**
     * Évalue la position du plateau pour l'IA en attribuant un score basé sur les pièces présentes, les menaces, les contrôles de cases, etc.
     * @param plateau Le plateau de jeu actuel
     * @return Un score représentant la position du plateau pour l'IA (plus élevé est meilleur pour l'IA)
     */
    abstract protected  int evaluateBoard(Plateau plateau);

}
