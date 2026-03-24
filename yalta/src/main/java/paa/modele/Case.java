package paa.modele;

import paa.controler.ActionCase;
import paa.controler.ActionVide;
import paa.modele.Element.Piece;

public class Case implements CaseComponent {
    private String id;
    private ActionCase action;
    private Piece piece;

    public Case(String id) {
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
    }
    public void setAction(ActionCase action) {
        this.action = action;
    }

    @Override
    public void mettreAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
    }
}
