package paa.vue;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vue de la popup de fin de partie.
 */
public class EndGamePopupView extends VBox implements LoadFxml {
    private static final String END_GAME_FXML_PATH = "/paa/end-game-popup.fxml";

    @FXML
    private Label titleLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private Label winnerLabel;

    @FXML
    private Label detailLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button closeButton;

    public EndGamePopupView() {
        loadFromFxml();
    }

    @Override
    public String getFxmlPath() {
        return END_GAME_FXML_PATH;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getResultLabel() {
        return resultLabel;
    }

    public Label getWinnerLabel() {
        return winnerLabel;
    }

    public Label getDetailLabel() {
        return detailLabel;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMenuButton() {
        return menuButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }
}