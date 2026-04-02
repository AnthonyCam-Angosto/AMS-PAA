package paa.vue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import paa.modele.plateau.Plateau;

/**
 * Représente la vue du plateau de jeu.
 */
public class PlateauView extends Group implements LoadFxml {
    private static final String BOARD_FXML_PATH = "/paa/plateau.fxml";


    public PlateauView() {
        loadFromFxml();
        addIndexTexts();

        Plateau plateau = Plateau.getInstance();
        plateau.createPieces();

        initObservers();

        int cellCount = getCaseViews().size();
        System.out.println("Plateau Yalta initialise: " + cellCount + " cases.");
    }

    @Override
    public void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(PlateauView.class.getResource(BOARD_FXML_PATH));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + BOARD_FXML_PATH, e);
        }
    }

    /**
     * Ajoute les id des cases en tant que texte centré sur chaque case du plateau, pour faciliter le développement et les tests.
     */
    private void addIndexTexts() {
        List<Text> labels = new ArrayList<>();

        for (CaseView caseView : getCaseViews()) {
            labels.add(createIndexText(caseView.getId(), caseView.getPoints(), 1.0));
        }

        getChildren().addAll(labels);
    }

    /**
     * Récupère la liste des CaseView présentes dans les enfants de ce PlateauView.
     * @return la liste des CaseView
     */
    private List<CaseView> getCaseViews() {
        List<CaseView> polygons = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof CaseView caseView) {
                polygons.add(caseView);
            }
        }
        return polygons;
    }

    /**
     * Crée un objet Text pour afficher l'index d'une case, centré sur la case elle-même.
     * @param label le texte à afficher (généralement l'ID de la case)
     * @param points les coordonnées des sommets de la case, utilisées pour calculer le centre de la case
     * @param scale un facteur de mise à l'échelle pour ajuster la taille du texte en fonction de la taille de la case
     * @return un objet Text configuré pour afficher l'index de la case
     */
    private Text createIndexText(String label, List<Double> points, double scale) {
        double centerX = 0.0;
        double centerY = 0.0;
        int vertexCount = points.size() / 2;

        for (int i = 0; i < points.size(); i += 2) {
            centerX += points.get(i);
            centerY += points.get(i + 1);
        }

        centerX /= vertexCount;
        centerY /= vertexCount;

        double fontSize = Math.max(9.0, 10.2 * scale);
        Text text = new Text(label);
        text.setFill(Color.web("#1f1f1f"));
        text.setMouseTransparent(true);
        text.setFont(Font.font("Consolas", FontWeight.BOLD, fontSize));

        // Center the index text in the polygon.
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        text.setX(centerX - textWidth / 2.0);
        text.setY(centerY + textHeight / 4.0);

        return text;
    }

    /**
     * Initialise les observateurs pour chaque case du plateau.
     */
    private void initObservers() {
        for (CaseView caseView : getCaseViews()) {
            String id = caseView.getId();
            if (id != null) {
                paa.modele.plateau.Case modelCase = Plateau.getInstance().getCaseById(id);
                if (modelCase != null) {
                    modelCase.ajouterObservateur(caseView);
                    caseView.onPieceChanged(modelCase);
                    caseView.onActionChanged(modelCase);
                }
            }
        }
    }
}
