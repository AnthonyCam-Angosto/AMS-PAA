package paa.controler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Interface définissant une action à effectuer lors du clic sur une case baser sur le pattern State,
 * correspond à l'etat de la case
 */
public interface ActionCase extends EventHandler<MouseEvent>{

    /**
     * Méthode appelée lors du clic sur une case
     */
    public void click();

    @Override
    default void handle(MouseEvent event) {
        click();
    }
}
