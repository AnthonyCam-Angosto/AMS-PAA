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
import paa.modele.plateau.CaseComponent;
import paa.modele.plateau.Plateau;

public class PlateauView extends Group implements CaseComponent, LoadFxml {
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

    private void addIndexTexts() {
        List<Text> labels = new ArrayList<>();

        for (CaseView caseView : getCaseViews()) {
            labels.add(createIndexText(caseView.getId(), caseView.getPoints(), 1.0));
        }

        getChildren().addAll(labels);
    }

    private List<CaseView> getCaseViews() {
        List<CaseView> polygons = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof CaseView caseView) {
                polygons.add(caseView);
            }
        }
        return polygons;
    }

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

    public CaseView getCaseViewById(String id) {
        for (CaseView caseView : getCaseViews()) {
            if (caseView.getId().equals(id)) {
                return caseView;
            }
        }
        return null; // Retourne null si aucune CaseView avec l'ID spécifié n'est trouvée
    }

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

    @Override
    public void mettreAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
    }
}
