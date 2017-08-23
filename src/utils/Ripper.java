package utils;

/**
 * Created by henry on 8/22/17.
 */
public class Ripper {
    int[] wts;
    int index = 0;
    public Ripper(int[] wts){
        this.wts = wts;
    }

    public int[] rip1(int l1) {
        int[] out = Weights.rip1(wts, index, l1);
        index += l1;
        return out;
    }

    public int[][] rip2(int l1, int l2) {
        int[][] out = Weights.rip2(wts, index, l1, l2);
        index += l1 * l2;
        return out;
    }

    public int[][][] rip3(int l1, int l2, int l3) {
        int[][][] out = Weights.rip3(wts, index, l1, l2, l3);
        index += l1 * l2 * l3;
        return out;
    }

    public int[][][][] rip4(int l1, int l2, int l3, int l4) {
        int[][][][] out = Weights.rip4(wts, index, l1, l2, l3, l4);
        index += l1 * l2 * l3 * l4;
        return out;
    }


}
