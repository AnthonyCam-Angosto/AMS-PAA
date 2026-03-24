package paa.controler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface ActionCase extends EventHandler<MouseEvent>{

    public void click();

    @Override
    default void handle(MouseEvent event) {
        click();
    }
}
