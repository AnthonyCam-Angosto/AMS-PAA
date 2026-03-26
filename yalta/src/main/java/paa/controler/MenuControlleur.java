package paa.controler;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import paa.modele.Partie;
import paa.vue.Main;

public class MenuControlleur{
    private final Main main;

    @FXML
    private Spinner<Integer> playerCountSpinner;

    public MenuControlleur(Main main) {
        this.main = main;
    }

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCellClick(ActionEvent event) {
        int playerCount = playerCountSpinner.getValue();
        System.out.println("Nombre de joueurs selectionne : " + playerCount);
        Partie partie = Partie.getInstance();
        partie.initialiserPartie(playerCount);

        main.getApp().changeToBoard();
    }
    
}
