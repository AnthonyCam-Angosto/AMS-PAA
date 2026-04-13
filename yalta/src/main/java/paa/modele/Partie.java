package paa.modele;

import java.util.ArrayList;
import java.util.List;

import paa.modele.Element.Roi;
import paa.modele.Element.Pion;
import paa.modele.Element.Piece;
import paa.modele.Utilisateur.IA;
import paa.modele.Utilisateur.Joueur;
import paa.modele.Utilisateur.Utilisateur;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

/**
 * Représente la partie de jeu.
 * c'est une classe singleton qui gère les joueurs, le tour actuel et les observateurs de la partie.
 * Elle permet d'initialiser la partie avec un nombre de joueurs humains et d'IA, 
 * de faire avancer le tour et de notifier les observateurs à chaque changement de tour.
 */
public class Partie implements PartieSubject {
    private int tour;
    private final Utilisateur[] joueurs;
    private final List<PartieObserver> observers=new ArrayList<>();

    private final Couleur[] ordre= {Couleur.BLANC, Couleur.NOIR, Couleur.ROUGE};//ordre de jeu des joueurs

    private static Partie instance = null;

    private Partie() {
        this.tour = -1;
        this.joueurs = new Utilisateur[3];
    }

    /**
     * Retourne l'instance unique de la classe Partie, en la créant si elle n'existe pas encore.
     * @return L'instance de la classe Partie
     */
    public static Partie getInstance() {
        if (instance == null) {
            instance = new Partie();
        }
        return instance;
    }

    public Couleur[] getOrdre() {
        return ordre;
    }

    public int getTour() {
        return tour;
    }

    public Utilisateur[] getJoueurs() {
        return joueurs;
    }

    /**
     * Passe au tour suivant et notifie les joueurs du changement de tour.
     */
    public void tourSuivant() {
        if(tour!=-1){
            Utilisateur joueurActuel=joueurs[tour%3];
            for(Utilisateur joueur : joueurs){
                Boolean enEchec=echec(joueur);

                if(joueur.isEchec() && joueurActuel.equals(joueur)){
                    if(enEchec){
                        System.out.println("Le joueur "+joueur.getCouleur()+" est echec et mat ! il n'a pas fait de coup pour sortir de l'échec");
                        finPartie(joueur, TypeFin.ECHEC_ET_MAT);
                        return;
                    }
                }
                if(enEchec){
                    joueur.setEchec(enEchec);
                    if(enEchec){
                        System.out.println("Le joueur "+joueur.getCouleur()+" est en échec !");
                    }
                    if(EchecEtMat(joueur)){
                        System.out.println("Le joueur "+joueur.getCouleur()+" est en échec et mat !");
                        finPartie(joueurActuel, TypeFin.ECHEC_ET_MAT);
                        return;
                    }
                }else{
                    joueur.setEchec(enEchec);
                    if(pat(joueur)){
                        System.out.println("Le joueur "+joueur.getCouleur()+" est en pat !");
                        finPartie(joueur, TypeFin.PAT);
                        return;
                    }
                }
            }
        }

        tour++;
        Utilisateur joueurActuel=joueurs[tour%3];
        notifyTourChange(joueurActuel);
    }

    public void finPartie(Utilisateur perdant, TypeFin typeFin) {
        System.out.println("La partie est terminée !");
        notifyPartieFinie(perdant, typeFin);
    }

    /**
     * Retourne la couleur du joueur actuel en fonction du tour.
     * @return La couleur du joueur actuel
     */
    public Couleur getCouleurJoueurActuel() {
        return joueurs[tour % 3].getCouleur();
    }
    
    /**
     * Réinitialise la partie.
     */
    public void reset() {
        tour = -1;
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i] = null;
        }
    }


    public void addJoueur(Utilisateur joueur) {
        if (joueurs[0] == null) {
            joueurs[0] = joueur;
        } else if (joueurs[1] == null) {
            joueurs[1] = joueur;
        } else if (joueurs[2] == null) {
            joueurs[2] = joueur;
        } else {
            throw new IllegalStateException("Il y a déjà 3 joueurs dans la partie");
        }
    }

    public void initialiserPartie(int nbNoIA) {
        reset();

        Utilisateur joueur;
        for (int i = 0; i < nbNoIA; i++) {
            joueur = new Joueur(ordre[i]);
            addJoueur(joueur);
            addObserver(joueur);
        }
        for (int i = nbNoIA; i < 3; i++) {
            joueur = new IA(ordre[i]);
            addJoueur(joueur);
            addObserver(joueur);
        }
        tourSuivant();
    }

    /**
     * Vérifie si le joueur spécifié est en échec en vérifiant si son roi est attaqué par une pièce adverse.
     * @param joueur Le joueur à vérifier pour l'échec
     * @return true si le joueur est en échec, false sinon
     */
    public boolean echec(Utilisateur joueur) {
        Plateau plateau = Plateau.getInstance();
        Case caseRoi = null;
        List<Case> casesAvecPiece = plateau.getAllCasePiece();

        for (Case c : casesAvecPiece) {
            if (c.getPiece() instanceof Roi && c.getPiece().getCouleur() == joueur.getCouleur()) {
                caseRoi = c;
                break;
            }
        }

        // On génère les captures des pièces adverses pour savoir si le roi est attaqué.
        boolean enEchec = false;
        for (Case casePiece : casesAvecPiece) {
            if (casePiece.getPiece().getCouleur() == joueur.getCouleur()) {
                continue;
            }

            List<Case> captures = casePiece.getPiece().manger(plateau, plateau.getIndexCase(casePiece));
            if (captures.contains(caseRoi)) {
                enEchec = true;
                break;
            }
        }

        return enEchec;
    }

    /**
     * Vérifie si le joueur spécifié est en échec et mat en vérifiant s'il est en échec et s'il n'a aucun coup légal pour sortir de l'échec.
     * @param joueur Le joueur à vérifier pour l'échec et mat
     * @return true si le joueur est en échec et mat, false sinon
     */
    public boolean EchecEtMat(Utilisateur joueur) {
        if (!echec(joueur)) {
            return false;
        }
        return !hasAnyLegalMove(joueur, Plateau.getInstance());
    }

    public boolean pat(Utilisateur joueur) {
        if(echec(joueur)){//le pat ne peut pas arriver si le joueur est en échec
            return false;
        }

        return !hasAnyLegalMove(joueur, Plateau.getInstance());
    }

    /**
     * Vérifie si le joueur possède au moins un coup légal (déplacement, capture, ou coup spécial).
     */
    private boolean hasAnyLegalMove(Utilisateur joueur, Plateau plateau) {

        List<Case> casesAvecPiece = plateau.getAllCasePiece();

        for (Case casePiece : casesAvecPiece) {
            if (casePiece.getPiece().getCouleur() != joueur.getCouleur()) {
                continue;
            }

            int[] indexPiece = plateau.getIndexCase(casePiece);
            List<Case> deplacements = casePiece.getPiece().deplacement(plateau, indexPiece);
            List<Case> captures = casePiece.getPiece().manger(plateau, indexPiece);

            for (Case destination : deplacements) {
                if (isLegalMove(joueur, casePiece, destination, plateau)) {
                    return false;
                }
            }

            for (Case destination : captures) {
                if (isLegalMove(joueur, casePiece, destination, plateau)) {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Vérifie si un coup candidat est légal en simulant le déplacement puis en contrôlant
     * que le roi du joueur n'est pas en échec après le coup.
     */
    private boolean isLegalMove(Utilisateur joueur, Case depart, Case arrivee, Plateau plateau) {
        Piece pieceDepart = depart.getPiece();
        if (pieceDepart == null) {
            return false;
        }

        Piece pieceArrivee = arrivee.getPiece();
        Case caseCaptureEnPassant = null;
        Piece pieceCaptureEnPassant = null;

        int[] indexDepart = plateau.getIndexCase(depart);
        int[] indexArrivee = plateau.getIndexCase(arrivee);

        // Simule la capture en passant pour un pion qui capture sur une case vide en diagonale.
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

        boolean legal = !echec(joueur);

        // Rollback de la simulation.
        arrivee.setPiece(pieceArrivee);
        depart.setPiece(pieceDepart);
        if (caseCaptureEnPassant != null) {
            caseCaptureEnPassant.setPiece(pieceCaptureEnPassant);
        }

        return legal;
    }

    @Override
    public void addObserver(PartieObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(PartieObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyTourChange(Utilisateur joueur) {
        for (PartieObserver observer : observers) {
            observer.onTourChange(joueur);
        }
    }

    @Override
    public void notifyPartieFinie(Utilisateur perdant, TypeFin typeFin) {
        for (PartieObserver observer : observers) {
            observer.onPartieFinie(perdant, typeFin);
        }
    }

    
}
