package paa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import paa.vue.Main;
import paa.vue.YaltaBoard;

public class App extends Application {

    private static final String APP_TITLE = "Echecs Yalta";
    private static final double DEFAULT_SCENE_WIDTH = 1000.0;
    private static final double DEFAULT_SCENE_HEIGHT = 800.0;

    private static final double BOARD_BASE_WIDTH = 960.0;
    private static final double BOARD_BASE_HEIGHT = 831.3843876330611;
    private static final double FIT_MARGIN = 24.0;

    private StackPane boardRoot;
    private YaltaBoard boardView;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        createBoardRoot();
        Parent menuRoot = createMenuRoot(primaryStage);
        mainScene = new Scene(menuRoot, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void changeToBoard() {
        mainScene.setRoot(boardRoot);
        Platform.runLater(this::resizeBoard);
    }

    private Parent createMenuRoot(Stage primaryStage) {
        Main menu = new Main(this);
        return menu;
    }

    private void createBoardRoot() {
        boardRoot = new StackPane();
        boardRoot.setStyle("-fx-background-color: #f5f5f5;");
        boardRoot.setAlignment(Pos.CENTER);

        // Charger le plateau FXML une seule fois, puis ajuster l'echelle.
        boardView = new YaltaBoard();
        boardRoot.getChildren().add(boardView);

        boardRoot.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
        boardRoot.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
    }

    private void resizeBoard() {
        Bounds bounds = boardRoot.getBoundsInLocal();
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        // Calculer la taille optimale des cases avec les dimensions exactes du plateau source.
        double availableWidth = Math.max(0, width - FIT_MARGIN * 2);
        double availableHeight = Math.max(0, height - FIT_MARGIN * 2);

        double ratioWidth = availableWidth / BOARD_BASE_WIDTH;
        double ratioHeight = availableHeight / BOARD_BASE_HEIGHT;
        double scale = Math.min(ratioWidth, ratioHeight);

        boardView.setScaleX(scale);
        boardView.setScaleY(scale);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
