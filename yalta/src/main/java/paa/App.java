package paa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import paa.vue.GameView;
import paa.vue.Main;

public class App extends Application {

    private static final String APP_TITLE = "Echecs Yalta";
    private static final double DEFAULT_SCENE_WIDTH = 1000.0;
    private static final double DEFAULT_SCENE_HEIGHT = 800.0;

    private GameView gameRoot;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        createBoardRoot();
        Parent menuRoot = createMenuRoot();
        mainScene = new Scene(menuRoot, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void changeToBoard() {
        mainScene.setRoot(gameRoot);
        Platform.runLater(gameRoot::resizeBoard);
    }

    public void changeToMenu() {
        mainScene.setRoot(createMenuRoot());
    }

    private Parent createMenuRoot() {
        Main menu = new Main(this);
        return menu;
    }

    private void createBoardRoot() {
        gameRoot = new GameView(this);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void reinitialiser() {
        createBoardRoot();
    }
}
