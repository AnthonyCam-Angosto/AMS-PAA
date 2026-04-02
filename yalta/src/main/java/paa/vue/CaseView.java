package paa.vue;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.TypeAction;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class CaseView extends Polygon implements CaseObserver {

    /**
     * Met à jour la pièce affichée sur cette case.
     * @param piece la pièce à afficher
     */
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
    public void onSelected(TypeAction t) {
        switch (t) {
            case DEPLACEMENT -> this.setStyle("-fx-fill: rgba(56, 149, 255, 0.42); -fx-stroke: #0b63d8; -fx-stroke-width: 4; -fx-stroke-type: inside;");
            case MANGER -> this.setStyle("-fx-fill: rgba(255, 56, 56, 0.42); -fx-stroke: #d80b0b; -fx-stroke-width: 4; -fx-stroke-type: inside;");
            case SPECIAL -> this.setStyle("-fx-fill: rgba(255, 255, 56, 0.42); -fx-stroke: #d8d80b; -fx-stroke-width: 4; -fx-stroke-type: inside;");
        }
    }


    /**
     * Désélectionne cette case.
     */
    public void deselectionner() {
        this.setStyle("");
    }

    @Override
    public void onDeselected(Case c) {
        if (c.getId().equals(this.getId())) {
            this.setStyle("");
        }
    }


    @Override
    public void onPromotion(Case c) {
        if (!c.getId().equals(this.getId())) {
            return;
        }

        Parent parent = getParent();
        if (!(parent instanceof Group group) || c.getPiece() == null) {
            return;
        }

        group.getChildren().removeIf(node -> "promotion-popup".equals(node.getUserData()));

        PromotionPopupView popup = new PromotionPopupView();
        popup.setUserData("promotion-popup");
        popup.setMouseTransparent(false);
        popup.setPickOnBounds(true);

        Couleur pieceColor = c.getPiece().getCouleur();
        configurePromotionButton(popup.getQueenButton(), pieceColor, "reine");
        configurePromotionButton(popup.getRookButton(), pieceColor, "tour");
        configurePromotionButton(popup.getBishopButton(), pieceColor, "fou");
        configurePromotionButton(popup.getKnightButton(), pieceColor, "cavalier");

        popup.getQueenButton().setOnAction(event -> finaliserPromotion(group, popup, c, "reine"));
        popup.getRookButton().setOnAction(event -> finaliserPromotion(group, popup, c, "tour"));
        popup.getBishopButton().setOnAction(event -> finaliserPromotion(group, popup, c, "fou"));
        popup.getKnightButton().setOnAction(event -> finaliserPromotion(group, popup, c, "cavalier"));

        group.getChildren().add(popup);
        popup.applyCss();
        popup.autosize();
        popup.toFront();

        Bounds bounds = getBoundsInParent();
        double popupWidth = popup.getBoundsInLocal().getWidth();
        double popupHeight = popup.getBoundsInLocal().getHeight();
        double x = bounds.getMinX() + (bounds.getWidth() - popupWidth) / 2.0;
        double y = bounds.getMinY() - popupHeight - 12.0;

        popup.setLayoutX(Math.max(0.0, x));
        popup.setLayoutY(Math.max(0.0, y));
    }

    private void configurePromotionButton(Button button, paa.modele.Couleur color, String type) {
        String imagePath = "/images/" + color.name().toLowerCase() + "-" + type + ".png";
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setFitWidth(34.0);
        icon.setFitHeight(34.0);
        button.setGraphic(icon);
        button.setText("");
    }

    private void finaliserPromotion(Group group, PromotionPopupView popup, Case casePromotion, String type) {
        group.getChildren().remove(popup);
        Plateau.getInstance().finPromotion(casePromotion, type);
    }

}
