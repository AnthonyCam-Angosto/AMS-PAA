package paa.modele.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.Couleur;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class RoiTest {
    private Plateau plateau;

    @BeforeEach
    void setUp() throws Exception {
        // Reset le singleton Plateau
        java.lang.reflect.Field instanceField = Plateau.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        plateau = Plateau.getInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Reset le singleton Plateau après chaque test
        java.lang.reflect.Field instanceField = Plateau.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }


    @Test
    void testCastling() {
        Roi roi = new Roi(Couleur.BLANC, new StandardSwitchPartieStrategy());
        Tour tourGauche = new Tour(Couleur.BLANC, new StandardSwitchPartieStrategy());
        Tour tourDroite = new Tour(Couleur.BLANC, new StandardSwitchPartieStrategy());

        Case caseRoi = plateau.getCaseById("E1");
        Case caseTourGauche = plateau.getCaseById("A1");
        Case caseTourDroite = plateau.getCaseById("H1");

        caseRoi.setPiece(roi);
        caseTourGauche.setPiece(tourGauche);
        caseTourDroite.setPiece(tourDroite);

        int[] indexRoi = plateau.getIndexCase(caseRoi);
        List<Case> roquesPossibles = roi.specials(plateau, indexRoi);
        List<String> idsRoques = roquesPossibles.stream().map(Case::getId).toList();

        assertEquals(2, roquesPossibles.size(), "Le roi doit pouvoir roquer des deux côtés.");
        assertTrue(idsRoques.contains("A1"), "Le grand roque vers A1 doit être possible.");
        assertTrue(idsRoques.contains("H1"), "Le petit roque vers H1 doit être possible.");
        assertTrue(!idsRoques.contains("C1") && !idsRoques.contains("G1"), "Les cases de roque attendues sont les tours A1 et H1.");
    }
}
