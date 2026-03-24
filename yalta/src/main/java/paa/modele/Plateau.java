package paa.modele;

public class Plateau implements CaseComponent {
    private Case[][][] cases;

    private static Plateau instance = null;

    private Plateau() {
        cases = new Case[4][8][3];
        for (int i = 1; i < 4; i++) {
            initCases(i);
        }
    }

    private void printCases() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    if(cases[i][j][k] != null){
                        System.out.print(cases[i][j][k].getId() + " ");
                    }else{
                        System.out.print("null ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static Plateau getInstance() {
        if (instance == null) {
            instance = new Plateau();
        }
        return instance;
    }

    //plateau1
    private void initCases(int partie) {
        String lettre = switch (partie) {
            case 1 -> "ABCDEFGH";
            case 2 -> "ABCDIJKL";
            case 3 -> "HGFEIJLK";
            default -> "";
        };
        int ecart= switch (partie) {
            case 2 -> 4;
            case 3 -> 8;
            default -> 0;
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j][partie - 1] = new Case(lettre.charAt(j)+""+(i+1+ecart));
            }
        }
    }

    @Override
    public void mettreAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mettreAJour'");
    }

    public Case getCase(int x, int y, int z) {
        return cases[x][y][z];
    }

    public static void main(String[] args) {
        Plateau plateau = Plateau.getInstance();
        plateau.printCases();
    }
}
