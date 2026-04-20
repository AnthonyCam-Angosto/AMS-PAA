package paa.modele.plateau;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.Couleur;
import paa.modele.Element.Piece;
import paa.modele.Element.Tour;
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
    void copy_creeUnPlateauIndependant() {
        Case caseSource = plateau.getCaseById("A1");
        Piece pieceSource = new Tour(Couleur.BLANC, standardSwitch);
        caseSource.setPiece(pieceSource);

        Plateau copie = plateau.copy();

        assertNotSame(plateau, copie);
        assertNotSame(plateau.getCaseById("A1"), copie.getCaseById("A1"));
        assertNotSame(plateau.getCaseById("A1").getPiece(), copie.getCaseById("A1").getPiece());
        assertEquals(plateau.getCaseById("A1").getPiece().getClass(), copie.getCaseById("A1").getPiece().getClass());

        copie.deplacementPiece(copie.getCaseById("A1"), copie.getCaseById("A2"), true);

        assertEquals(pieceSource, plateau.getCaseById("A1").getPiece());
        assertNull(plateau.getCaseById("A2").getPiece());
        assertNull(copie.getCaseById("A1").getPiece());
        assertNotSame(plateau.getCaseById("A1").getPiece(), copie.getCaseById("A2").getPiece());
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
