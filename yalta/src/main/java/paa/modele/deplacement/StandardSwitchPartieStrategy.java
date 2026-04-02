package paa.modele.deplacement;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class StandardSwitchPartieStrategy implements SwitchPartieStrategy {
    
    @Override
    public Case resolve(Plateau plateau, int[] indexCase, int delta) {
        if(indexCase[1]<4){
            Case c =switch (indexCase[2]) {
                case 0 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 1);
                case 1 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 0);
                case 2 ->plateau.getCase(indexCase[0], 7-indexCase[1]+delta, 0);
                default->null;
            };
            return c;
        }else{
                Case c =switch (indexCase[2]) {
                case 0 ->plateau.getCase(indexCase[0], 7-indexCase[1]+delta, 2);
                case 1 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 2);
                case 2 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 1);
                default->null;
            };
            return c;
        }
    }

    @Override
    public Case resolveSpecial(Plateau plateau, int[] indexCase, int delta) {
        if(indexCase[1]<4){
            Case c =switch (indexCase[2]) {
                case 0 ->plateau.getCase(indexCase[0], 7-indexCase[1]+delta, 2);
                case 1 ->plateau.getCase(indexCase[0], 8-indexCase[1]+delta, 2);
                case 2 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 1);
                default->null;
            };
            return c;
        }else{
                Case c =switch (indexCase[2]) {
                case 0 ->plateau.getCase(indexCase[0], indexCase[1]+delta, 1);
                case 1 ->plateau.getCase(indexCase[0], 6-indexCase[1]+delta, 0);
                case 2 ->plateau.getCase(indexCase[0], 7-indexCase[1]+delta, 0);
                default->null;
            };
            return c;
        }
    }
    
}
