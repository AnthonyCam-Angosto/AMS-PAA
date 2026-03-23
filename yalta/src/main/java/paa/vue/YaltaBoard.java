package paa.vue;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class YaltaBoard extends Group {

    private static final int EXPECTED_CELLS = 96;
    private static final String CLICK_MESSAGE_PREFIX = "Case Yalta cliquee: ";
    private static final String BOARD_FXML_PATH = "/paa/yalta-board.fxml";

    public YaltaBoard() {
        loadFromFxml();
        addIndexTexts(1.0);

        int cellCount = getCellPolygons().size();
        System.out.println("Plateau Yalta initialise: " + cellCount + " cases.");
        if (cellCount != EXPECTED_CELLS) {
            System.out.println("Attention: attendu " + EXPECTED_CELLS + " cases.");
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCellClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        System.out.println(CLICK_MESSAGE_PREFIX + source.getId());
    }

    private void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(YaltaBoard.class.getResource(BOARD_FXML_PATH));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + BOARD_FXML_PATH, e);
        }
    }

    private void addIndexTexts(double scale) {
        List<Text> labels = new ArrayList<>();

        for (Polygon cell : getCellPolygons()) {
            labels.add(createIndexText(cell.getId(), cell.getPoints(), scale));
        }

        getChildren().addAll(labels);
    }

    private List<Polygon> getCellPolygons() {
        List<Polygon> polygons = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof Polygon polygon) {
                polygons.add(polygon);
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
}
