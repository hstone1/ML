import java.util.List;

public class Molecule {
    public static final int MAX_BONDS = 4;

    int[][] bonds;
    int[] numBonds;
    int bondData;

    public Molecule (int numAtoms, List<int[]> bonds) {
        numBonds = new int[numAtoms];
        this.bonds = new int[numAtoms][MAX_BONDS];
        for (int i = 0;i < numAtoms;i++) {
            numBonds[i] = 0;
        }

        for (int[] bond : bonds) {
            int a1 = bond[0];
            int a2 = bond[1];

            this.bonds[a1][numBonds[a1]] = a2;
            this.bonds[a2][numBonds[a2]] = a1;
        }
    }

    public int[][] group(int[][] atomData) {
        // TODO
        return null;
    }
}
