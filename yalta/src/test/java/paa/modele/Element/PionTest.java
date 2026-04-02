package paa.modele.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.Couleur;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class PionTest {
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
    void testPromotion() {
        Case casePion = plateau.getCaseById("B7");
        Case casePromotion = plateau.getCaseById("B8");

        Pion pion = new Pion(Couleur.BLANC, new StandardSwitchPartieStrategy());
        pion.setHasMoved(true);
        casePion.setPiece(pion);

        List<Case> destinationPromotion = pion.specials(plateau, plateau.getIndexCase(casePion));
        assertEquals(1, destinationPromotion.size(), "Le pion doit avoir exactement une case de promotion.");
        assertEquals(casePromotion, destinationPromotion.get(0), "La case de promotion doit être B8.");

    }

    @Test
    void testPromotion_Manger() {
        Case casePion = plateau.getCaseById("B7");
        String[] casesTest = {"A8", "B8", "C8"};

        plateau.getCaseById("A8").setPiece(new Tour(Couleur.NOIR, new StandardSwitchPartieStrategy()));
        plateau.getCaseById("C8").setPiece(new Fou(Couleur.NOIR, new StandardSwitchPartieStrategy()));

        Pion pion = new Pion(Couleur.BLANC, new StandardSwitchPartieStrategy());
        pion.setHasMoved(true);
        casePion.setPiece(pion);

        List<Case> destinationPromotion = pion.specials(plateau, plateau.getIndexCase(casePion));
        assertEquals(3, destinationPromotion.size(), "Le pion doit avoir exactement trois cases de promotion possibles.");
        for (Case c : destinationPromotion) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Les cases de promotion doivent être A8, B8 ou C8 :"+c.getId());
        }
    }

}
