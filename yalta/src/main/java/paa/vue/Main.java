package paa.vue;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import paa.App;
import paa.controler.MenuControlleur;

public class Main extends VBox {

    private static final String MENU_FXML_PATH = "/paa/main.fxml";

    private final App app;

    public Main(App app) {
        this.app = app;
        loadFromFxml();
    }

    private void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(MENU_FXML_PATH));
        loader.setRoot(this);
        loader.setController(new MenuControlleur(this));

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + MENU_FXML_PATH, e);
        }
    }

    public App getApp() {
        return app;
    }
}
