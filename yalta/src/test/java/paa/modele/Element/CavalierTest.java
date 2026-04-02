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

public class CavalierTest {
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

    @Test // cas simple
    void testDeplacementCavalierD2() {
        String[] casesTest = {"B1", "B3", "C4", "E4", "F1", "F3"};
        Case caseD2 = plateau.getCaseById("D2");
        Cavalier cavalier = new Cavalier(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseD2.setPiece(cavalier);
        int[] index = plateau.getIndexCase(caseD2);
        List<Case> deplacements = cavalier.deplacement(plateau, index);

        assertEquals(casesTest.length, deplacements.size(), "Le cavalier doit avoir exactement "+casesTest.length+" déplacements possibles.");
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le cavalier doit pouvoir se déplacer vers :"+c.getId());
        }
    }

    @Test //cas de la transition de partie
    void testDeplacementCavalierB5() {
        String[] casesTest = {"A3", "A7", "C3", "C7", "D4", "D6"};
        Case caseB5 = plateau.getCaseById("B5");
        Cavalier cavalier = new Cavalier(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseB5.setPiece(cavalier);
        int[] index = plateau.getIndexCase(caseB5);
        List<Case> deplacements = cavalier.deplacement(plateau, index);

        assertEquals(casesTest.length, deplacements.size(), "Le cavalier doit avoir exactement "+casesTest.length+" déplacements possibles.");
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le cavalier doit pouvoir se déplacer vers :"+c.getId());
        }
    }

    @Test // cas du centre du plateau
    void testDeplacementCavalierD4() {
        String[] casesTest = {"C2","E2","F3","F9","E10","J5","I6","C6","B5","B3"};
        Case caseD4 = plateau.getCaseById("D4");
        Cavalier cavalier = new Cavalier(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseD4.setPiece(cavalier);
        int[] index = plateau.getIndexCase(caseD4);
        List<Case> deplacements = cavalier.deplacement(plateau, index);
        System.out.println("Déplacements possibles pour le cavalier en D4 :");
        for (Case c : deplacements) {
            System.out.println(" - " + c.getId());
        }

        assertEquals(casesTest.length, deplacements.size(), "Le cavalier doit avoir exactement "+casesTest.length+" déplacements possibles.");
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le cavalier doit pouvoir se déplacer vers :"+c.getId());
        }
    }

    @Test // cas complexe
    void testDeplacementCavalierI6() {
        String[] casesTest = {"J8","D8","C7","C5","D4","E9","J9","K5","K7"};
        Case caseI6 = plateau.getCaseById("I6");
        Cavalier cavalier = new Cavalier(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseI6.setPiece(cavalier);
        int[] index = plateau.getIndexCase(caseI6);
        List<Case> deplacements = cavalier.deplacement(plateau, index);
        System.out.println("Déplacements possibles pour le cavalier en I6:");
        for (Case c : deplacements) {
            System.out.println(" - " + c.getId());
        }

        assertEquals(casesTest.length, deplacements.size(), "Le cavalier doit avoir exactement "+casesTest.length+" déplacements possibles.");
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le cavalier doit pouvoir se déplacer vers :"+c.getId());
        }
    }

    @Test
    void testDeplacementCavalierE4() {
        String[] casesTest = {"J9","I10","F10","G9","G3","F2","D2","C3","C5","D6"};
        Case caseE4 = plateau.getCaseById("E4");
        Cavalier cavalier = new Cavalier(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseE4.setPiece(cavalier);
        int[] index = plateau.getIndexCase(caseE4);
        List<Case> deplacements = cavalier.deplacement(plateau, index);
        System.out.println("Déplacements possibles pour le cavalier en E4:");
        for (Case c : deplacements) {
            System.out.println(" - " + c.getId());
        }

        assertEquals(casesTest.length, deplacements.size(), "Le cavalier doit avoir exactement "+casesTest.length+" déplacements possibles.");
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le cavalier doit pouvoir se déplacer vers :"+c.getId());
        }
    }
}
