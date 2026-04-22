package paa.modele;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import paa.modele.Element.Piece;
import paa.modele.Element.Roi;
import paa.modele.Element.Tour;
import paa.modele.Utilisateur.Joueur;
import paa.modele.Utilisateur.Utilisateur;
import paa.modele.deplacement.StandardSwitchPartieStrategy;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

class PartieTest {
    private Partie partie;
    private Plateau plateau;

    @BeforeEach
    void setUp() throws Exception {
        resetSingletons();
        partie = Partie.getInstance();
        plateau = Plateau.getInstance();
        clearBoard();
    }

    @AfterEach
    void tearDown() throws Exception {
        resetSingletons();
    }

    @Test
    void echec_retourneTrue_quandRoiEstAttaque() {
        Utilisateur joueurBlanc = new Joueur(Couleur.BLANC);

        Case caseRoi = plateau.getCase(0, 0, 0);
        Case caseTourAdverse = plateau.getCase(3, 0, 0);

        caseRoi.setPiece(new Roi(Couleur.BLANC, new StandardSwitchPartieStrategy()));
        caseTourAdverse.setPiece(new Tour(Couleur.NOIR, new StandardSwitchPartieStrategy()));

        assertTrue(partie.echec(joueurBlanc));
    }

    @Test
    void echec_retourneFalse_quandRoiNonMenace() {
        Utilisateur joueurBlanc = new Joueur(Couleur.BLANC);

        Case caseRoi = plateau.getCase(0, 0, 0);
        Case caseTourAdverse = plateau.getCase(3, 1, 0);

        caseRoi.setPiece(new Roi(Couleur.BLANC, new StandardSwitchPartieStrategy()));
        caseTourAdverse.setPiece(new Tour(Couleur.NOIR, new StandardSwitchPartieStrategy()));

        assertFalse(partie.echec(joueurBlanc));
    }

    @Test
    void pat_retourneFalse_siJoueurEnEchec() {
        Utilisateur joueurBlanc = new Joueur(Couleur.BLANC);

        Case caseRoi = plateau.getCase(0, 0, 0);
        Case caseTourAdverse = plateau.getCase(3, 0, 0);

        caseRoi.setPiece(new Roi(Couleur.BLANC, new StandardSwitchPartieStrategy()));
        caseTourAdverse.setPiece(new Tour(Couleur.NOIR, new StandardSwitchPartieStrategy()));

        assertFalse(partie.pat(joueurBlanc));
    }

    @Test
    void pat_retourneTrue_siAucunCoupLegalExiste() {
        Utilisateur joueurBlanc = new Joueur(Couleur.BLANC);

        // Roi bloque par ses propres pieces immobiles, sans menace adverse.
        plateau.getCase(0, 0, 0).setPiece(new Roi(Couleur.BLANC, new StandardSwitchPartieStrategy()));
        plateau.getCase(0, 1, 0).setPiece(new PieceImmobile(Couleur.BLANC));
        plateau.getCase(1, 0, 0).setPiece(new PieceImmobile(Couleur.BLANC));
        plateau.getCase(1, 1, 0).setPiece(new PieceImmobile(Couleur.BLANC));

        assertTrue(partie.pat(joueurBlanc));
    }

    @Test
    void finPartie_notifieObserver_pourChaqueTypeDeFin() {
        ObservateurCapture observateur = new ObservateurCapture();
        partie.addObserver(observateur);

        Utilisateur perdant = new Joueur(Couleur.NOIR);

        for (TypeFin typeFin : TypeFin.values()) {
            partie.finPartie(perdant, typeFin);
            assertSame(perdant, observateur.dernierPerdant);
            assertEquals(typeFin, observateur.dernierTypeFin);
        }
    }

    private void clearBoard() {
        for (int z = 0; z < 3; z++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 8; y++) {
                    plateau.getCase(x, y, z).setPiece(null);
                }
            }
        }
    }

    private void resetSingletons() throws Exception {
        java.lang.reflect.Field partieField = Partie.class.getDeclaredField("instance");
        partieField.setAccessible(true);
        partieField.set(null, null);

        java.lang.reflect.Field plateauField = Plateau.class.getDeclaredField("instance");
        plateauField.setAccessible(true);
        plateauField.set(null, null);
    }

    private static final class ObservateurCapture implements PartieObserver {
        private Utilisateur dernierPerdant;
        private TypeFin dernierTypeFin;

        @Override
        public void onTourChange(Utilisateur joueur) {
            // Non utilise dans ces tests.
        }

        @Override
        public void onPartieFinie(Utilisateur perdant, TypeFin typeFin) {
            this.dernierPerdant = perdant;
            this.dernierTypeFin = typeFin;
        }
    }

    private static final class PieceImmobile extends Piece {
        PieceImmobile(Couleur couleur) {
            super(0, couleur, new StandardSwitchPartieStrategy());
        }

        @Override
        public List<Case> deplacement(Plateau plateau, int[] indexActuel) {
            return List.of();
        }

        @Override
        public List<Case> manger(Plateau plateau, int[] indexActuel) {
            return List.of();
        }

        @Override
        public Piece copy() {
            return new PieceImmobile(getCouleur());
        }
    }

}
