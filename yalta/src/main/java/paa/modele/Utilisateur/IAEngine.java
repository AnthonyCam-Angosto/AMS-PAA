package paa.modele.Utilisateur;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Element.Pion;
import paa.modele.Element.Roi;
import paa.modele.Partie;
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
            if (casePiece.getPiece().getCouleur() != couleurJoueur) {
                continue;
            }
            int[] indexPiece = plateau.getIndexCase(casePiece);
            List<List<Case>> deplacementsCapturesSpecials = new ArrayList<>();
            deplacementsCapturesSpecials.add(casePiece.getPiece().deplacement(plateau, indexPiece));
            deplacementsCapturesSpecials.add(casePiece.getPiece().manger(plateau, indexPiece));
            deplacementsCapturesSpecials.add(casePiece.getPiece().specials(plateau, indexPiece));

            for (List<Case> actions : deplacementsCapturesSpecials) {
                for (Case action : actions) {
                    if (isLegalMoveOnPlateau(couleurJoueur, casePiece, action, plateau)) {
                        coups.add(new Coup(casePiece.getId(), action.getId()));
                    }
                }
            }
        }
        return coups;
    }

    protected boolean isLegalMoveOnPlateau(Couleur couleurJoueur, Case depart, Case arrivee, Plateau plateau) {
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

    protected  void appliquerCoup(Plateau plateau, Coup coup) {
        Case depart = plateau.getCaseById(coup.departId);
        Case arrivee = plateau.getCaseById(coup.arriveeId);
        plateau.deplacementPiece(depart, arrivee, true);
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
