package paa.modele.Utilisateur;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/**
 * Moteur de calcul Minimax mutualise entre l'IA et les threads d'evaluation.
 */
public class MinimaxEngine extends  IAEngine {
    //TODO:  manque les coups spéciaux (promotion, roque, prise en passant)

    public MinimaxEngine(Couleur couleurIA) {
        super(couleurIA);
    }

    @Override
    public int evaluerCoup(Plateau plateau, Coup coup, int profondeur) {
        Plateau prochainEtat = plateau.copy();
        appliquerCoup(prochainEtat, coup);
        return minimax(prochainEtat, profondeur - 1, couleurSuivante(couleurIA));
    }

    private int minimax(Plateau plateau, int profondeur, Couleur joueurCourant) {
        if (profondeur <= 0 || estTerminal(plateau)) {
            return evaluateBoard(plateau);
        }
        List<Coup> coups = getLegalMoves(plateau, joueurCourant);
        if (coups.isEmpty()) {
            return evaluateBoard(plateau);
        }

        boolean estNoeudMax = joueurCourant == this.couleurIA;
        int meilleurScore = estNoeudMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Coup coup : coups) {
            Plateau prochainEtat = plateau.copy();
            appliquerCoup(prochainEtat, coup);
            int valeurCoup = minimax(prochainEtat, profondeur - 1, couleurSuivante(joueurCourant));

            if (estNoeudMax) {
                meilleurScore = Math.max(meilleurScore, valeurCoup);
            } else {
                meilleurScore = Math.min(meilleurScore, valeurCoup);
            }
        }

        return meilleurScore;
    }
    

    @Override
    protected int evaluateBoard(Plateau plateau) {
        Map<Couleur, Integer> scoresParCouleur = new EnumMap<>(Couleur.class);
        for (Couleur c : Couleur.values()) {
            scoresParCouleur.put(c, 0);
        }
        for (Case c : plateau.getAllCasePiece()) {
            Piece piece = c.getPiece();
            int valeurMaterielle = piece.getValeur() * 100;

            int[] indexPiece = plateau.getIndexCase(c);
            int mobilite = 0;
            if (indexPiece != null) {
                mobilite = piece.deplacement(plateau, indexPiece).size()
                        + piece.manger(plateau, indexPiece).size();
            }
            int bonusMobilite = Math.min(8, mobilite) * 5;
            int scorePiece = valeurMaterielle + bonusMobilite;
            Couleur couleurPiece = piece.getCouleur();
            scoresParCouleur.put(couleurPiece, scoresParCouleur.get(couleurPiece) + scorePiece);
        }
        int evaluation = scoresParCouleur.get(this.couleurIA);
        int totalAdverses = 0;
        int nombreAdverses = 0;

        for (Couleur c : Couleur.values()) {
            if (c == this.couleurIA) {
                continue;
            }
            totalAdverses += scoresParCouleur.get(c);
            nombreAdverses++;
        }
        int moyenneAdverse = nombreAdverses == 0 ? 0 : totalAdverses / nombreAdverses;

        for (Couleur c : Couleur.values()) {
            if (c == this.couleurIA) {
                continue;
            }

            int scoreAdverse = scoresParCouleur.get(c);

            int delta = (scoreAdverse - moyenneAdverse) / 2;
            int poidsMillieme = 1000 + Math.max(-250, Math.min(500, delta));
            evaluation -= (scoreAdverse * poidsMillieme) / 1000;
        }
        return evaluation;
    }
}