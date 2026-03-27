package paa.modele.plateau;

import java.util.ArrayList;
import java.util.List;

import paa.controler.ActionCase;
import paa.controler.ActionPiece;
import paa.controler.ActionVide;
import paa.modele.Element.Piece;
import paa.vue.CaseObserver;

public class Case implements CaseComponent, CaseSubject {
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

    public void setPiece(Piece piece) {
        this.piece = piece;
        if(piece != null){
            this.setAction(new ActionPiece(this));
        }else{
            this.setAction(new ActionVide(this));
        }
        notifyPieceChanged();

    }

    public void setAction(ActionCase action) {
        this.action = action;
        notifyActionChanged();
        switch (action.getClass().getSimpleName()) {
            case "ActionDeplacementPossible":
                notifySelected();
                break;
            default:
                notifyDeselected();
                break;
        }
    }

    
    @Override
    public void mettreAJour() {
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
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
    public void notifySelected() {
        for (CaseObserver observer : observers) {
            observer.onSelected(this);
        }

    }

    @Override
    public void notifyDeselected() {
        for (CaseObserver observer : observers) {
            observer.onDeselected(this);
        }
    }

    @Override
    public void deselectionner() {
        this.setAction(new ActionVide(this));
    }
}
