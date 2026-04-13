package paa.vue;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import paa.modele.Couleur;

/**
 * Représente la vue d'une pièce du plateau.
 */
public class PieceView extends StackPane implements LoadFxml {
    private static final String PIECE_FXML_PATH = "/paa/piece-view.fxml";
    private ImageView icon;

    
    public PieceView(String type,Couleur couleur) {
        loadFromFxml();

        if (icon != null) {
            String imagePath = "/images/" + couleur.name() + "-" + type + ".png";
            icon.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            icon.setPreserveRatio(true);
            icon.setSmooth(true);
        }
    }

    /**
     * Ajuste la taille de l'icône de la pièce.
     * @param size la nouvelle taille (largeur et hauteur) de l'icône
     */
    public void setPieceSize(double size) {
        if (icon == null) {
            return;
        }
        icon.setFitWidth(size);
        icon.setFitHeight(size);
    }

    @Override
    public String getFxmlPath() {
        return PIECE_FXML_PATH;
    }

    @Override
    public void configureLoader(FXMLLoader loader) {
        // PieceView utilise le noeud charge comme contenu interne.
    }

    @Override
    public void afterLoad(FXMLLoader loader, Object loadedRoot) {
        StackPane content = (StackPane) loadedRoot;
        getChildren().setAll(content);
        icon = (ImageView) loader.getNamespace().get("icon");
    }

}