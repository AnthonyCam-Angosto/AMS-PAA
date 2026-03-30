package paa.modele.plateau;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.deplacement.DiagonalPathStrategy;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.deplacement.LinePathStrategy;
import paa.modele.deplacement.PathStrategy;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.deplacement.SwitchPartieStrategy;

class PlateauTest {

    private Plateau plateau;
    private final SwitchPartieStrategy standardSwitch = new StandardSwitchPartieStrategy();
    private final SwitchPartieStrategy diagonalSwitch = new DiagonalSwitchPartieStrategy();
    private final PathStrategy linePathStrategy = new LinePathStrategy();
    private final PathStrategy diagonalPathStrategy = new DiagonalPathStrategy();

    @BeforeEach
    void setUp() throws Exception {
        resetPlateauSingleton();
        plateau = Plateau.getInstance();
    }

    @Test
    void getLine_horizontal_surPlateauInitial() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("D2"));

        List<Case> ligne = linePathStrategy.getPath(plateau, index, 0, standardSwitch);

        assertIds(ligne, "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2");
    }

    @Test
    void getLine_vertical_traverseVersAutrePartie() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("A1"));

        List<Case> ligne = linePathStrategy.getPath(plateau, index, 1, standardSwitch);

        assertIds(ligne, "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8");
    }

    @Test
    void getDiagonal_C1() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("C1"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "C1", "B2", "A3");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "C1", "D2", "E3", "F4", "G9", "H10");
    }


    @Test
    void getDiagonal_C4() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("C4"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "F1", "E2", "D3", "C4", "B5", "A6");


        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "A2", "B3", "C4", "D5", "I6", "J7", "K8");
    }

    @Test
    void getDiagonal_E4() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("E4"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "H1", "G2", "F3", "E4", "I9", "J10", "K11", "L12");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "B1", "C2", "D3", "E4", "F9", "G10", "H11");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 2, diagonalSwitch);
        assertIds(diagonal, "E4", "D5", "C6", "B7", "A8");
    }

    @Test
    void getDiagonal_E9() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("E9"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "K12", "J11", "I10", "E9", "F4", "G3", "H2");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "H12", "G11", "F10", "E9", "D4", "C3", "B2", "A1");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 2, diagonalSwitch);
        assertIds(diagonal, "E9", "I5", "J6", "K7", "L8");
    }

    @Test
    void getDiagonal_D4() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("D4"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "G1", "F2", "E3", "D4", "C5", "B6", "A7");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "A1", "B2", "C3", "D4", "I5", "J6", "K7", "L8");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 2, diagonalSwitch);
        assertIds(diagonal, "D4", "E9", "F10", "G11", "H12");
    }

    @Test
    void getDiagonal_B2() {
        int[] index = plateau.getIndexCase(plateau.getCaseById("B2"));
        List<Case> diagonal = diagonalPathStrategy.getPath(plateau, index, 0, diagonalSwitch);
        assertIds(diagonal, "C1", "B2", "A3");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 1, diagonalSwitch);
        assertIds(diagonal, "A1", "B2", "C3", "D4", "I5", "J6", "K7", "L8");

        diagonal = diagonalPathStrategy.getPath(plateau, index, 2, diagonalSwitch);
        assertIds(diagonal, "A1", "B2", "C3", "D4", "E9", "F10", "G11", "H12");
    }

   
    private static void resetPlateauSingleton() throws Exception {
        Field instanceField = Plateau.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    private static void assertIds(List<Case> cases, String... expectedIds) {
        assertEquals(expectedIds.length, cases.size(), "Nombre de cases inattendu");

        for (int i = 0; i < expectedIds.length; i++) {
            assertEquals(expectedIds[i], cases.get(i).getId(), "Case inattendue a l'index " + i);
        }
    }
}
