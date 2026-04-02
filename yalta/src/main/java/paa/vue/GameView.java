package paa.vue;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Représente la vue principale du jeu.
 */
public class GameView extends VBox{
    private static final double BOARD_BASE_WIDTH = 960.0;
    private static final double BOARD_BASE_HEIGHT = 831.3843876330611;
    private static final double FIT_MARGIN = 24.0;
    private static final String FXML_PATH = "/paa/gameView.fxml";

    @FXML
    private StackPane boardContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private PlateauView plateauView;

    public GameView() {
        super();
        this.setId("game-view");
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(28);
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3ec, #d9dfc8);");
        
        this.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
        this.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
        
        // Charger le contenu FXML after construction
        Platform.runLater(() -> {
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(getClass().getResource(FXML_PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (java.io.IOException e) {
                throw new IllegalStateException("Impossible de charger " + FXML_PATH, e);
            }

            VBox.setVgrow(boardContainer, Priority.ALWAYS);
            boardContainer.setTranslateY(-50);
            boardContainer.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
            boardContainer.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
            resizeBoard();
        });
    }


    /**
     * Redimensionne le plateau en fonction de la taille de la vue.
     */
    public void resizeBoard() {
        if (plateauView == null || boardContainer == null) {
            return;
        }

        double availableWidth = this.getWidth() - FIT_MARGIN * 2;

        double titleHeight = titleLabel != null ? titleLabel.getHeight() : 0.0;
        double reservedTop = titleHeight + getSpacing();
        double availableHeight = this.getHeight() - reservedTop - FIT_MARGIN * 2;

        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }

        double ratioWidth = availableWidth / BOARD_BASE_WIDTH;
        double ratioHeight = availableHeight / BOARD_BASE_HEIGHT;
        double scale = Math.min(ratioWidth, ratioHeight);
        scale = Math.min(scale, 1.0);

        if (scale <= 0) {
            return;
        }

        plateauView.setScaleX(scale);
        plateauView.setScaleY(scale);
    }
}