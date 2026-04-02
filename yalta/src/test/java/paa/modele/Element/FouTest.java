package paa.modele.Element;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paa.modele.Couleur;
import paa.modele.deplacement.DiagonalSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FouTest {
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
    void fouNeTraversePasCroisementAvecUnSeulPivot() {
        String[] casesTest = {"E10", "F11", "G12", "I11", "J12", "I9","J5","K6","L7","F9","G4","H3"};
        // Place un fou sur E10 (pivot unique)
        Case caseE10 = plateau.getCaseById("E10");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseE10.setPiece(fou);
        int[] index = plateau.getIndexCase(caseE10);
        List<Case> deplacements = fou.deplacement(plateau, index);
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le fou ne doit pas traverser le croisement central avec un seul pivot :"+c.getId());
        }
    }

    @Test
    void fouI10NeTraversePasCroisementAvecUnSeulPivot() {
        // Place un fou sur I10 (pivot unique)
        String[] casesTest = {"J11", "K12", "E9", "F4", "G3", "H2", "E11", "F12", "J9", "K5", "L6"};
        Case caseI10 = plateau.getCaseById("I10");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseI10.setPiece(fou);
        int[] index = plateau.getIndexCase(caseI10);
        List<Case> deplacements = fou.deplacement(plateau, index);
        for (Case c : deplacements) {
            assertTrue(java.util.Arrays.asList(casesTest).contains(c.getId()), "Le fou ne doit pas traverser le croisement central avec un seul pivot :"+c.getId());
        }
    }

    @Test
    void fouD5AtteintI9(){
        String[] case1diago={"K8", "J7", "I6", "D5", "C4", "B3", "A2"};
        String[] case3diago={"A8","B7","C6","D5","E4","F3","G2","H1"};
        String[] case2diago={"A8","B7","C6","D5","I9","J10","K11","L12"};
        // Place un fou sur D5 (pivot sur I9)
        Case caseD5 = plateau.getCaseById("D5");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseD5.setPiece(fou);
        int[] index = plateau.getIndexCase(caseD5);
        List<Case> deplacements = fou.deplacement(plateau, index);
        for (Case c : deplacements) {
            if (java.util.Arrays.asList(case1diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la première diagonale classique");
            } else if (java.util.Arrays.asList(case2diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la deuxième diagonale classique");
            } else if (java.util.Arrays.asList(case3diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la troisième diagonale du croisement central");
            } else {
                fail("Le fou ne doit pas pouvoir se déplacer sur d'autres cases que les diagonales attendues :"+c.getId());
            }

        }

    }

    @Test
    void fouB2DoitAvoirTroisiemeDiagonale() {
        // Place un fou sur B2
        String[] case1diago={"C1", "B2", "A3"};
        String[] case3diago={"A1","B2","C3","D4","E9","F10","G11","H12"};
        String[] case2diago={"A1","B2","C3","D4","I5","J6","K7","L8"};
        Case caseB2 = plateau.getCaseById("B2");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseB2.setPiece(fou);
        int[] index = plateau.getIndexCase(caseB2);
        List<Case> deplacements = fou.deplacement(plateau, index);
        // On attend que la 3e diagonale (croisement) soit présente, donc que le fou puisse atteindre D4
        for (Case c : deplacements) {
            if (java.util.Arrays.asList(case1diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la première diagonale classique");
            } else if (java.util.Arrays.asList(case2diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la deuxième diagonale classique");
            } else if (java.util.Arrays.asList(case3diago).contains(c.getId())) {
                assertTrue(true, "Le fou doit pouvoir se déplacer sur la troisième diagonale du croisement central");
            } else {
                fail("Le fou ne doit pas pouvoir se déplacer sur d'autres cases que les diagonales attendues :"+c.getId());
            }

        }
    }

    @Test
    void fouJ6DoitAvoirTroisiemeDiagonale() {
        // Place un fou sur J6
        String[] case1diago={"L8", "K7","I5", "E9", "F10", "G11", "H12"};
        String[] case2diago={"D8", "I7", "K5", "L9"};
        String[] case3diago={"L8","K7","I5","D4","C3","B2","A1"};
        int sizeall = case1diago.length + case2diago.length + case3diago.length;
        Case caseJ6 = plateau.getCaseById("J6");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseJ6.setPiece(fou);
        int[] index = plateau.getIndexCase(caseJ6);
        List<Case> deplacements = fou.deplacement(plateau, index);
        System.out.println("deplacements : "+deplacements.stream().map(Case::getId).toList());
        System.out.println("sizeall : "+sizeall+" deplacements : "+deplacements.size());
        // On attend que la 3e diagonale (croisement) soit présente, donc que le fou puisse atteindre I5
        assertTrue(sizeall==deplacements.size(), "Le fou doit pouvoir se déplacer sur les 3 diagonales sans doublons");
        for(String id :case1diago){
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la première diagonale classique");
        }
        for( String id :case2diago){
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la deuxième diagonale classique");
        }
        for(String id :case3diago){
            System.out.println("test fin getpath : "+id);
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la troisième diagonale du croisement central");
        }
    }

    @Test
    void fouK11DoitAvoirTroisiemeDiagonale() {
        // Place un fou sur K11
        String[] case1diago={"L12", "J10", "I9", "D5", "C6", "B7", "A8"};
        String[] case2diago={"J12","L10"};
        String[] case3diago={"L12","J10","I9","E4","F3","G2","H1"};
        int sizeall = case1diago.length + case2diago.length + case3diago.length;

        Case caseK11 = plateau.getCaseById("K11");
        Fou fou = new Fou(Couleur.BLANC, new DiagonalSwitchPartieStrategy());
        caseK11.setPiece(fou);
        int[] index = plateau.getIndexCase(caseK11);
        List<Case> deplacements = fou.deplacement(plateau, index);
        System.out.println("sizeall : "+sizeall+" deplacements : "+deplacements.size());

        //System.out.println("deplacements : "+deplacements.stream().map(Case::getId).toList());
        // On attend que la 3e diagonale (croisement) soit présente, donc que le fou puisse se déplacer sur F6
        assertTrue(sizeall==deplacements.size(), "Le fou doit pouvoir se déplacer sur les 3 diagonales sans doublons");
        for(String id :case1diago){
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la première diagonale classique");
        }
        for( String id :case2diago){
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la deuxième diagonale classique");
        }
        for(String id :case3diago){
            assertTrue(deplacements.stream().anyMatch(c -> c.getId().equals(id)), "Le fou doit pouvoir se déplacer sur la troisième diagonale du croisement central");
        }
    }
}
