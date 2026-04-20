package paa.vue;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import paa.controler.EndGameController;
import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.PartieObserver;
import paa.modele.TypeFin;
import paa.modele.Utilisateur.IA;
import paa.modele.Utilisateur.Utilisateur;

/**
 * Représente la vue principale du jeu.
 */
public class GameView extends VBox implements PartieObserver {
    private static final double BOARD_BASE_WIDTH = 960.0;
    private static final double BOARD_BASE_HEIGHT = 831.3843876330611;
    private static final double FIT_MARGIN = 24.0;
    private static final double BOARD_VERTICAL_OFFSET = -14.0;
    private static final double SCREEN_RESERVED_VERTICAL_SPACE = 130.0;
    private static final String FXML_PATH = "/paa/gameView.fxml";
    private static final String CARD_INACTIVE_STYLE = "-fx-background-color: #1a2847; -fx-background-radius: 10; -fx-border-color: #4da6ff; -fx-border-width: 1; -fx-border-radius: 10; -fx-padding: 8 10 8 10;";
    private static final String CARD_ACTIVE_STYLE = "-fx-background-color: #254080; -fx-background-radius: 10; -fx-border-color: #ffff00; -fx-border-width: 3; -fx-border-radius: 10; -fx-padding: 8 10 8 10;";

    @FXML
    protected StackPane boardContainer;

    @FXML
    protected Label titleLabel;

    @FXML
    protected PlateauView plateauView;

    @FXML
    protected HBox whiteCard;

    @FXML
    protected HBox blackCard;

    @FXML
    protected HBox redCard;

    @FXML
    protected ImageView whiteKingIcon;

    @FXML
    protected ImageView blackKingIcon;

    @FXML
    protected ImageView redKingIcon;

    @FXML
    protected Label whiteCheckLabel;

    @FXML
    protected Label blackCheckLabel;

    @FXML
    protected Label redCheckLabel;

    @FXML
    protected Label whiteAiLabel;

    @FXML
    protected Label blackAiLabel;

    @FXML
    protected Label redAiLabel;

    private EndGamePopupView endGamePopup;

    private boolean observerRegistered;

    public GameView() {
        super();
        this.setId("game-view");
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(28);
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0e27, #1a1f3a);");
        
        this.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
        this.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
        
        // Charger le contenu FXML after construction
        Platform.runLater(() -> {
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(getClass().getResource(FXML_PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (java.io.IOException e) {
                throw new IllegalStateException("Impossible de charger " + FXML_PATH, e);
            }

            VBox.setVgrow(boardContainer, Priority.ALWAYS);
            boardContainer.setTranslateY(BOARD_VERTICAL_OFFSET);
            boardContainer.widthProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
            boardContainer.heightProperty().addListener((obs, oldVal, newVal) -> resizeBoard());
            resizeBoard();
        });
    }

    @FXML
    protected void initialize() {
        loadKingIcons();

        Partie partie = Partie.getInstance();
        if (!observerRegistered) {
            partie.addObserver(this);
            observerRegistered = true;
        }

        updateAiIndicators();

        try {
            updateTurnIndicator(partie.getCouleurJoueurActuel());
        } catch (Exception ignored) {
            // Vue chargee hors partie active: on garde les cartes dans leur etat neutre.
        }
    }


    /**
     * Redimensionne le plateau en fonction de la taille de la vue.
     */
    public void resizeBoard() {
        if (plateauView == null || boardContainer == null) {
            return;
        }

        double availableWidth = boardContainer.getWidth() - FIT_MARGIN * 2;
        double availableHeight = boardContainer.getHeight() - FIT_MARGIN * 2;

        if (availableWidth <= 0 || availableHeight <= 0) {
            availableWidth = this.getWidth() - FIT_MARGIN * 2;

            double titleHeight = titleLabel != null ? titleLabel.getHeight() : 0.0;
            double reservedTop = titleHeight + getSpacing();
            availableHeight = this.getHeight() - reservedTop - FIT_MARGIN * 2;
        }

        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }

        double ratioWidth = availableWidth / BOARD_BASE_WIDTH;
        double ratioHeight = availableHeight / BOARD_BASE_HEIGHT;
        double scale = Math.min(ratioWidth, ratioHeight);

        // Limite de taille selon la hauteur d'ecran utile (hors barre des taches).
        scale = Math.min(scale, getScreenConstrainedScale());
        scale = Math.min(scale, 1.0);

        if (scale <= 0) {
            return;
        }

        plateauView.setScaleX(scale);
        plateauView.setScaleY(scale);
    }

    private double getScreenConstrainedScale() {
        if (getScene() == null || getScene().getWindow() == null) {
            return 1.0;
        }

        Stage stage = (Stage) getScene().getWindow();
        Rectangle2D windowBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
        Screen screen = Screen.getScreensForRectangle(windowBounds)
                .stream()
                .findFirst()
                .orElse(Screen.getPrimary());

        double usableScreenHeight = screen.getVisualBounds().getHeight();
        double maxBoardHeight = Math.max(BOARD_BASE_HEIGHT * 0.55, usableScreenHeight - SCREEN_RESERVED_VERTICAL_SPACE);
        return maxBoardHeight / BOARD_BASE_HEIGHT;
    }

    private void loadKingIcons() {
        setKingIcon(whiteKingIcon, "/images/blanc-roi.png");
        setKingIcon(blackKingIcon, "/images/noir-roi.png");
        setKingIcon(redKingIcon, "/images/rouge-roi.png");
    }

    private void setKingIcon(ImageView iconView, String path) {
        if (iconView == null) {
            return;
        }
        iconView.setImage(new Image(getClass().getResourceAsStream(path)));
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
    }

    private void updateTurnIndicator(Couleur couleurActive) {
        if (whiteCard == null || blackCard == null || redCard == null) {
            return;
        }

        whiteCard.setStyle(CARD_INACTIVE_STYLE);
        blackCard.setStyle(CARD_INACTIVE_STYLE);
        redCard.setStyle(CARD_INACTIVE_STYLE);

        switch (couleurActive) {
            case BLANC:
                whiteCard.setStyle(CARD_ACTIVE_STYLE);
                break;
            case NOIR:
                blackCard.setStyle(CARD_ACTIVE_STYLE);
                break;
            case ROUGE:
                redCard.setStyle(CARD_ACTIVE_STYLE);
                break;
        }

        // Update check indicators for all players
        updateCheckIndicators();
    }

    private void updateCheckIndicators() {
        Partie partie = Partie.getInstance();
        Utilisateur[] joueurs = partie.getJoueurs();

        Utilisateur blancJoueur = joueurs[0];
        Utilisateur noirJoueur = joueurs[1];
        Utilisateur rougeJoueur = joueurs[2];

        if (whiteCheckLabel != null) {
            whiteCheckLabel.setVisible(blancJoueur != null && blancJoueur.isEchec());
        }
        if (blackCheckLabel != null) {
            blackCheckLabel.setVisible(noirJoueur != null && noirJoueur.isEchec());
        }
        if (redCheckLabel != null) {
            redCheckLabel.setVisible(rougeJoueur != null && rougeJoueur.isEchec());
        }
    }

    private void updateAiIndicators() {
        Partie partie = Partie.getInstance();
        Utilisateur[] joueurs = partie.getJoueurs();

        updateAiIndicatorLabel(whiteAiLabel, joueurs[0]);
        updateAiIndicatorLabel(blackAiLabel, joueurs[1]);
        updateAiIndicatorLabel(redAiLabel, joueurs[2]);
    }

    private void updateAiIndicatorLabel(Label aiLabel, Utilisateur joueur) {
        if (aiLabel == null) {
            return;
        }

        boolean isAi = joueur instanceof IA;
        aiLabel.setVisible(isAi);
        aiLabel.setManaged(isAi);
    }

    @Override
    public void onTourChange(Utilisateur joueur) {
        if (joueur == null) {
            return;
        }
        Platform.runLater(() -> {
            updateAiIndicators();
            updateTurnIndicator(joueur.getCouleur());
        });
    }

    @Override
    public void onPartieFinie(Utilisateur perdant, TypeFin typeFin) {
        Platform.runLater(() -> showEndGamePopup(perdant, typeFin));
    }

    private void showEndGamePopup(Utilisateur perdant, TypeFin typeFin) {
        if (boardContainer == null) {
            return;
        }

        if (endGamePopup == null) {
            endGamePopup = new EndGamePopupView();
            endGamePopup.setMaxWidth(440);
            endGamePopup.setPickOnBounds(true);
            StackPane.setMargin(endGamePopup, new Insets(12));

            paa.App app = getApp();
            endGamePopup.getCloseButton().setOnAction(new EndGameController(app, EndGameController.typeAction.QUIT));
            endGamePopup.getMenuButton().setOnAction(new EndGameController(app, EndGameController.typeAction.MENU));
            endGamePopup.getRestartButton().setOnAction(new EndGameController(app, EndGameController.typeAction.RESTART));
        }

        TypeFin fin = typeFin != null ? typeFin : TypeFin.NULLE;

        switch (fin) {
            case ECHEC_ET_MAT:
                endGamePopup.getResultLabel().setText("Echec et mat");
                endGamePopup.getDetailLabel().setText("Le roi ne peut plus etre protege");
                break;
            case PAT:
                endGamePopup.getResultLabel().setText("Pat");
                endGamePopup.getDetailLabel().setText("Aucun coup legal disponible");
                break;
            case ABANDON:
                endGamePopup.getResultLabel().setText("Abandon");
                endGamePopup.getDetailLabel().setText("Un joueur a abandonne la partie");
                break;
            case NULLE:
            default:
                endGamePopup.getResultLabel().setText("Partie nulle");
                endGamePopup.getDetailLabel().setText("La partie se termine sans vainqueur");
                break;
        }

        if (perdant != null) {
            endGamePopup.getWinnerLabel().setText("Joueur " + perdant.getCouleur() + " elimine");
        } else {
            endGamePopup.getWinnerLabel().setText("Aucun joueur elimine");
        }

        if (!boardContainer.getChildren().contains(endGamePopup)) {
            boardContainer.getChildren().add(endGamePopup);
        }
        endGamePopup.toFront();
    }

    @FXML
    protected void handleFullscreenClick() {
        if (getScene() == null || getScene().getWindow() == null) {
            return;
        }

        Stage stage = (Stage) getScene().getWindow();
        stage.setFullScreen(true);
    }

    private paa.App getApp() {
        if (getScene() != null && getScene().getRoot() instanceof Main) {
            return ((Main) getScene().getRoot()).getApp();
        }
        return null;
    }
}