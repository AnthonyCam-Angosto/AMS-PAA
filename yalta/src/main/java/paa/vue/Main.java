package paa.vue;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import paa.App;

public class Main extends VBox {

    private static final String MENU_FXML_PATH = "/paa/main.fxml";

    private final App app;

    @FXML
    private Spinner<Integer> playerCountSpinner;

    public Main(App app) {
        this.app = app;
        loadFromFxml();
    }

    private void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(MENU_FXML_PATH));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + MENU_FXML_PATH, e);
        }
    }

    @FXML
    private void initialize() {
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCellClick(MouseEvent event) {
        int playerCount = playerCountSpinner.getValue();
        System.out.println("Nombre de joueurs selectionne : " + playerCount);
        app.changeToBoard();
    }
}
