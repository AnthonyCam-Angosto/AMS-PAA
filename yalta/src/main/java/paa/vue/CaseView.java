package paa.vue;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import paa.modele.Element.Piece;
import paa.modele.plateau.Case;
import paa.modele.plateau.CaseComponent;

public class CaseView extends Polygon implements CaseObserver,CaseComponent {

    public void setPiece(Piece piece) {
        Parent parent = this.getParent();
        if (!(parent instanceof Group group)) {
            return;
        }

        group.getChildren().removeIf(node -> node instanceof PieceView && node.getUserData() != null && node.getUserData().equals(this.getId()));

        if (piece == null || piece.getView() == null) {
            return;
        }

        PieceView pieceView = piece.getView();
        pieceView.setUserData(this.getId());
        pieceView.setMouseTransparent(true);

        Bounds bounds = this.getBoundsInParent();
        double size = Math.min(bounds.getWidth(), bounds.getHeight()) * 0.56;
        pieceView.setPrefSize(size, size);
        pieceView.setMinSize(size, size);
        pieceView.setMaxSize(size, size);
        pieceView.setPieceSize(size);
        pieceView.setLayoutX(bounds.getMinX() + (bounds.getWidth() - size) / 2.0);
        pieceView.setLayoutY(bounds.getMinY() + (bounds.getHeight() - size) / 2.0);

        if (group.getChildren().contains(pieceView)) {
            group.getChildren().remove(pieceView);
        }
        group.getChildren().add(pieceView);
    }


    @Override
    public void onPieceChanged(Case c) {
        if (c.getId().equals(this.getId())) {
            setPiece(c.getPiece());
        }
    }

    @Override
    public void onActionChanged(Case c) {
        this.setOnMouseClicked(c.getAction());
    }


    @Override
    public void mettreAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
    }

}
