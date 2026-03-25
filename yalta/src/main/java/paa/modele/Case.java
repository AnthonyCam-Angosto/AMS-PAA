package paa.modele;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import paa.controler.ActionCase;
import paa.controler.ActionPiece;
import paa.controler.ActionVide;
import paa.modele.Element.Piece;
import paa.vue.PieceView;

public class Case implements CaseComponent {
    private String id;
    private ActionCase action;
    private Piece piece;
    private Polygon polygon;

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
        Piece anciennePiece = this.piece;
        this.piece = piece;

        Parent parent = polygon.getParent();
        if (!(parent instanceof Group group)) {
            return;
        }

        if (anciennePiece != null && anciennePiece.getView() != null) {
            group.getChildren().remove(anciennePiece.getView());
        }

        PieceView pieceView = piece.getView();
        pieceView.setMouseTransparent(true);

        Bounds bounds = polygon.getBoundsInParent();
        double size = Math.min(bounds.getWidth(), bounds.getHeight()) * 0.56;
        pieceView.setPrefSize(size, size);
        pieceView.setMinSize(size, size);
        pieceView.setMaxSize(size, size);
        pieceView.setPieceSize(size);
        pieceView.setLayoutX(bounds.getMinX() + (bounds.getWidth() - size) / 2.0);
        pieceView.setLayoutY(bounds.getMinY() + (bounds.getHeight() - size) / 2.0);

        if (!group.getChildren().contains(pieceView)) {
            group.getChildren().add(pieceView);
        }
        setAction(new ActionPiece(this));
    }

    public void setAction(ActionCase action) {
        this.action = action;
        polygon.setOnMouseClicked(action);
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
        polygon.setOnMouseClicked(action);
    }

    @Override
    public void mettreAJour() {
        setPiece(piece);
    }
}
