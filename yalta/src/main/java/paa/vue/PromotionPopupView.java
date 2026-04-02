package paa.vue;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vue de la popup de promotion du pion.
 */
public class PromotionPopupView extends VBox implements LoadFxml {
    private static final String PROMOTION_FXML_PATH = "/paa/promotion-popup.fxml";

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Button queenButton;

    @FXML
    private Button rookButton;

    @FXML
    private Button bishopButton;

    @FXML
    private Button knightButton;

    public PromotionPopupView() {
        loadFromFxml();
    }

    @Override
    public void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(PromotionPopupView.class.getResource(PROMOTION_FXML_PATH));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + PROMOTION_FXML_PATH, e);
        }
    }

    public Button getQueenButton() {
        return queenButton;
    }

    public Button getRookButton() {
        return rookButton;
    }

    public Button getBishopButton() {
        return bishopButton;
    }

    public Button getKnightButton() {
        return knightButton;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getSubtitleLabel() {
        return subtitleLabel;
    }
}
