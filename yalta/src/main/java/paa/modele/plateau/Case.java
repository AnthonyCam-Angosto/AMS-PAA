package paa.modele.plateau;

import java.util.ArrayList;
import java.util.List;

import paa.CaseObserver;
import paa.controler.ActionCase;
import paa.controler.ActionPiece;
import paa.controler.ActionVide;
import paa.modele.Element.Piece;
import paa.modele.Prototype;
import paa.modele.TypeAction;

/**
 * Représente une case du plateau de jeu
 */
public class Case implements CaseComponent, CaseSubject,Prototype<Case> {
    private String id;
    private ActionCase action;
    private Piece piece;

    private final List<CaseObserver> observers=new ArrayList<>();

    public Case(String id) {
        this.id = id;
        this.id = id;
        action = new ActionVide(this);
        piece = null;
    }

    public String getId() {
        return id;
    }
    public ActionCase getAction() {
        return action;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * Place une pièce sur la case et met à jour l'action en conséquence, puis notifie les observateurs de la vue du changement de pièce.
     * @param piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        if(piece != null){
            this.setAction(new ActionPiece(this));
        }else{
            this.setAction(new ActionVide(this));
        }
        notifyPieceChanged();

    }

    /**
     * Met à jour l'action de la case en fonction de l'action passée en paramètre, 
     * puis notifie les observateurs de la vue du changement d'action et de la sélection/désélection de la case en fonction du type d'action.
     * @param action L'action à assigner à la case
     */
    public void setAction(ActionCase action) {
        this.action = action;
        notifyActionChanged();
        switch (action.getClass().getSimpleName()) {
            case "ActionManger" -> notifySelected(TypeAction.MANGER);
            case "ActionDeplacementPossible" -> notifySelected(TypeAction.DEPLACEMENT);
            case "ActionSpecial" -> notifySelected(TypeAction.SPECIAL);
            default -> notifyDeselected();
        }
    }
    

    @Override
    public void ajouterObservateur(CaseObserver observer) {
        observers.add(observer);
    }

    @Override
    public void supprimerObservateur(CaseObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyPieceChanged() {
        for (CaseObserver observer : observers) {
            observer.onPieceChanged(this);
        }
    }

    @Override
    public void notifyActionChanged() {
        for (CaseObserver observer : observers) {
            observer.onActionChanged(this);
        }
    }

    @Override
    public void notifySelected(TypeAction t) {
        for (CaseObserver observer : observers) {
            observer.onSelected(t);
        }
    }

    @Override
    public void notifyDeselected() {
        for (CaseObserver observer : observers) {
            observer.onDeselected(this);
        }
    }

    @Override
    public void notifyPromotion(Case casePromotion) {
         for (CaseObserver observer : observers) {
            observer.onPromotion(casePromotion);
        }
    }

    @Override
    public void deselectionner() {
        this.setAction(new ActionVide(this));
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Case copy() {
        Case copie = new Case(this.id);
        if (this.piece != null) {
            copie.setPiece(this.piece.copy());
        }
        return copie;
    }

    
}
