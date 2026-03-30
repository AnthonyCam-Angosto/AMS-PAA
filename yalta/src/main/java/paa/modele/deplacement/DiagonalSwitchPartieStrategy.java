package paa.modele.deplacement;

import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;

public class DiagonalSwitchPartieStrategy  implements SwitchPartieStrategy {
    @Override
    public Case resolve(Plateau plateau, int[] indexCase, int delta) {
        int x = indexCase[0];
        int y;
        int z;
        if(indexCase[1]<4){
            switch (indexCase[2]) {
                case 0 -> {
                    y = indexCase[1] + delta;
                    z = 1;
                }
                case 1 -> {
                    y = indexCase[1] + delta;
                    z = 0;
                }
                case 2 -> {
                    y = 7 - indexCase[1] + delta;
                    z = 0;
                }
                default -> {
                    return null;
                }
            }
        }else{
            switch (indexCase[2]) {
                case 0 -> {
                    y = 7 - indexCase[1] + delta;
                    z = 2;
                }
                case 1 -> {
                    y = indexCase[1] + delta;
                    z = 2;
                }
                case 2 -> {
                    y = indexCase[1] + delta;
                    z = 1;
                }
                default -> {
                    return null;
                }
            }
        }
        if (x < 0 || x >= 4 || y < 0 || y >= 8 || z < 0 || z >= 3) {
            return null;
        }
        return plateau.getCase(x, y, z);
    }
    
}
