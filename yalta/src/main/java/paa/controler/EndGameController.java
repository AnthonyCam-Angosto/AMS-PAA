package paa.controler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import paa.App;

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
                break;
            case QUIT:
                System.exit(0);
                break;
            case MENU:
                if (app != null) {
                    app.changeToMenu();
                }
                break;
        }
    }
    
}
