package paa.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import paa.modele.Partie;
import paa.vue.Main;

/**
 * Contrôleur pour le menu principal.
 */
public class MenuControlleur{
    private final Main main;

    @FXML
    protected Spinner<Integer> playerCountSpinner;

    public MenuControlleur(Main main) {
        this.main = main;
    }

    /**
     * Initialise le spinner pour le nombre de joueurs avec une plage de 0 à 3 et une valeur par défaut de 1.
     */
    @FXML
    protected void initialize() {
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, 1));
    }

    /**
     * Gère le clic sur le bouton de démarrage de la partie. Récupère le nombre de joueurs sélectionné, 
     * initialise la partie et change la vue pour afficher le plateau de jeu.
     * @param event L'événement de clic sur le bouton de démarrage de la partie
     */
    @FXML
    protected void handleCellClick(ActionEvent event) {
        int playerCount = playerCountSpinner.getValue();
        System.out.println("Nombre de joueurs selectionne : " + playerCount);
        main.getApp().changeToBoard();
        Partie partie = Partie.getInstance();
        partie.initialiserPartie(playerCount);

    }

    /**
     * Passe la fenetre principale en plein ecran.
     * @param event l'evenement de clic sur le bouton plein ecran
     */
    @FXML
    protected void handleFullscreenClick(ActionEvent event) {
        if (main.getScene() == null || main.getScene().getWindow() == null) {
            return;
        }

        Stage stage = (Stage) main.getScene().getWindow();
        stage.setFullScreen(true);
    }
    
}
