package examples;

import backend.Problem;

import java.util.Arrays;

public class ConvolveCircTest {
    public static void main(String[] args) {
        Problem p = new Problem();

        int[][] vals = p.constant(new double[][]{{1,0,2,1,1,0,2,-1,1,0,0,1}, {1,-1,0,0,1,1,1,2,-1,0,0,2}});
        int[][] conv = p.constant(new double[][]{{-1, 1, 0, 1, -1}, {1,1,0,-1,0}});

        int[] out = p.convolveCirc1d(vals, conv, p.constant(0));
        System.out.println(Arrays.toString(p.get(out)));
    }
}
