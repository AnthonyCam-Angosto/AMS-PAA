package paa.modele;

import java.util.ArrayList;
import java.util.List;

import paa.controler.ActionDeplacementPossible;
import paa.controler.ActionManger;
import paa.modele.Element.Piece;
import paa.modele.Element.Roi;
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
        return false; // TODO Implémentation de la logique d'échec et mat
    }

    public boolean pat(Utilisateur joueur) {
        if(echec(joueur)){//le pat ne peut pas arriver si le joueur est en échec
            return false;
        }

        Plateau plateau = Plateau.getInstance();
        List<Case> casesAvecPiece = plateau.getAllCasePiece();

        for (Case casePiece : casesAvecPiece) {
            if (casePiece.getPiece().getCouleur() != joueur.getCouleur()) {
                continue;
            }

            int[] indexPiece = plateau.getIndexCase(casePiece);
            List<Case> deplacements = casePiece.getPiece().deplacement(plateau, indexPiece);
            List<Case> captures = casePiece.getPiece().manger(plateau, indexPiece);

            if (!deplacements.isEmpty() || !captures.isEmpty()) {
                return false; // Le joueur a au moins un coup légal, donc ce n'est pas un pat
            }
        }

        return true;
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
