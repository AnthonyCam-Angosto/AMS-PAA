package paa.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import paa.modele.Partie;
import paa.vue.Main;

/**
 * Contrôleur pour le menu principal.
 */
public class MenuControlleur{
    private final Main main;

    @FXML
    private Spinner<Integer> playerCountSpinner;

    public MenuControlleur(Main main) {
        this.main = main;
    }

    /**
     * Initialise le spinner pour le nombre de joueurs avec une plage de 1 à 3 et une valeur par défaut de 3.
     */
    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 3));
    }

    /**
     * Gère le clic sur le bouton de démarrage de la partie. Récupère le nombre de joueurs sélectionné, 
     * initialise la partie et change la vue pour afficher le plateau de jeu.
     * @param event L'événement de clic sur le bouton de démarrage de la partie
     */
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
