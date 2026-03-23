package paa.vue;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import paa.App;

public class Menu extends VBox {

    private static final String MENU_FXML_PATH = "/paa/menu.fxml";

    private final App app;

    public Menu(App app) {
        this.app = app;
        loadFromFxml();
    }

    private void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(Menu.class.getResource(MENU_FXML_PATH));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + MENU_FXML_PATH, e);
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCellClick(MouseEvent event) {
        app.changeToBoard();
    }
}
