package paa.controler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import paa.App;
import paa.modele.Partie;

public class EndGameController implements EventHandler<ActionEvent> {
    public enum typeAction {
        RESTART,
        QUIT,
        MENU
    }
    private final typeAction action;
    private final App app;

    public EndGameController(App app, typeAction action) {
        this.app = app;
        this.action = action;
    }

    @Override
    public void handle(ActionEvent arg0) {
        switch (action) {
            case RESTART:
                if (app != null) {
                    System.out.println("Redémarrage de la partie...");
                    Partie.getInstance().reinitialiser();
                    app.reinitialiser();
                    app.changeToBoard();
                }
                break;
            case QUIT:
                System.exit(0);
                break;
            case MENU:
                if (app != null) {
                    System.out.println("revient au menu");
                    Partie.getInstance().reinitialiser();
                    app.reinitialiser();
                    app.changeToMenu();
                }
                break;
        }
    }
    
}
