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

public abstract class IAEngine {
    protected final Couleur couleurIA;

    public IAEngine(Couleur couleurIA) {
        this.couleurIA = couleurIA;
    }

    abstract protected int evaluerCoup(Plateau plateau, Coup coup, int profondeur);

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

    private Coup.Type getTypeSpecial(Piece piece) {
        if (piece instanceof Roi) {
            return Coup.Type.ROQUE;
        }
        if (piece instanceof Pion) {
            return Coup.Type.PROMOTION;
        }
        return Coup.Type.NORMAL;
    }

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

    protected Couleur couleurSuivante(Couleur couleurActuelle) {
        Couleur[] ordre = Partie.getInstance().getOrdre();
        for (int i = 0; i < ordre.length; i++) {
            if (ordre[i] == couleurActuelle) {
                return ordre[(i + 1) % ordre.length];
            }
        }
        return ordre[0];
    }

    protected boolean estTerminal(Plateau plateau) {
        return !roiEnVie(plateau, Couleur.BLANC)
                || !roiEnVie(plateau, Couleur.NOIR)
                || !roiEnVie(plateau, Couleur.ROUGE);
    }

    private boolean roiEnVie(Plateau plateau, Couleur couleur) {
        for (Case c : plateau.getAllCasePiece()) {
            if (c.getPiece().getCouleur() == couleur && c.getPiece() instanceof Roi) {
                return true;
            }
        }
        return false;
    }

    abstract protected  int evaluateBoard(Plateau plateau);

}
