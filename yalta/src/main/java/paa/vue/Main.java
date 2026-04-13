package paa.vue;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import paa.App;
import paa.controler.MenuControlleur;

/**
 * Représente la vue de d'accueil de l'application.
 */
public class Main extends VBox implements LoadFxml {

    private static final String MENU_FXML_PATH = "/paa/main.fxml";

    private final App app;

    public Main(App app) {
        this.app = app;
        loadFromFxml();
    }

    @Override
    public String getFxmlPath() {
        return MENU_FXML_PATH;
    }

    @Override
    public void configureLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(new MenuControlleur(this));
    }

    public App getApp() {
        return app;
    }
}
