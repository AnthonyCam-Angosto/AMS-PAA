package paa.modele.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.Couleur;
import paa.modele.Partie;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class PionTest {
    private Plateau plateau;

    @BeforeEach
    void setUp() throws Exception {
        java.lang.reflect.Field partieField = Partie.class.getDeclaredField("instance");
        partieField.setAccessible(true);
        partieField.set(null, null);

        // Reset le singleton Plateau
        java.lang.reflect.Field instanceField = Plateau.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        plateau = Plateau.getInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        java.lang.reflect.Field partieField = Partie.class.getDeclaredField("instance");
        partieField.setAccessible(true);
        partieField.set(null, null);

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

    @Test
    void testEnPassantCapture() {
        Case caseDepart = plateau.getCaseById("B2");
        Case caseCapture = plateau.getCaseById("C3");
        Case caseVulnerable = plateau.getCaseById("C2");

        Pion pionActeur = new Pion(Couleur.BLANC, new StandardSwitchPartieStrategy());
        caseDepart.setPiece(pionActeur);

        Pion pionVulnerable = new Pion(Couleur.NOIR, new StandardSwitchPartieStrategy());
        pionVulnerable.setHasMoved(true);
        pionVulnerable.setEnPassant(true);
        caseVulnerable.setPiece(pionVulnerable);

        List<Case> captures = pionActeur.manger(plateau, plateau.getIndexCase(caseDepart));

        assertTrue(captures.contains(caseCapture), "La case vide doit être proposée pour la capture en passant.");

        plateau.deplacementPiece(caseDepart, caseCapture, false);

        assertEquals(pionActeur, caseCapture.getPiece(), "Le pion doit arriver sur la case cible.");
        assertTrue(caseVulnerable.isEmpty(), "Le pion capturable doit être retiré du plateau.");
    }

    private void remplirPlateauPourManger() {
        Plateau p = Plateau.getInstance();
        for(int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                for (int z = 0; z < 3; z++) {
                    Case c = p.getCase(x, y, z);
                    if (c.isEmpty()) {
                        c.setPiece(new Pion(Couleur.NOIR, new StandardSwitchPartieStrategy()));
                    }
                }
            }
        }
    }

    @Test
    void testMangerF4(){
        Case casePion = plateau.getCaseById("F4");
        List<String> casesTest = List.of("E9","G9");

        Pion pion = new Pion(Couleur.BLANC, new StandardSwitchPartieStrategy());
        pion.setHasMoved(true);
        casePion.setPiece(pion);
        remplirPlateauPourManger();

        List<Case> captures = pion.manger(plateau, plateau.getIndexCase(casePion));
        System.out.println("Captures possibles pour le pion en F4 : " + captures.stream().map(Case::getId).toList());
        assertEquals(2, captures.size(), "Le pion doit pouvoir capturer sur les cases E9 et G9.");
        for (Case c : captures) {
            assertTrue(casesTest.contains(c.getId()), "Les cases de capture doivent être E9 ou G9 :"+c.getId());
        }
    }



}
