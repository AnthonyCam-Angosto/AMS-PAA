package paa.vue;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import paa.modele.Couleur;

public class PieceView extends StackPane implements LoadFxml {
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

    public void setPieceSize(double size) {
        if (icon == null) {
            return;
        }
        icon.setFitWidth(size);
        icon.setFitHeight(size);
    }

    @Override
    public void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/paa/piece-view.fxml"));
        try {
            StackPane content = loader.load();
            getChildren().setAll(content);
            icon = (ImageView) loader.getNamespace().get("icon");
        } catch (IOException ex) {
            System.getLogger(PieceView.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}