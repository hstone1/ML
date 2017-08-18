package examples.Convolution;

import backend.Problem;

import java.util.Arrays;

public class TestConvolution {
    public static void main(String[] args) {
        double[][] weights = new double[][]{
                {1,-1},
                {-1,1}};

        double[][] img = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {-1, 0.5, 0, 0},
                {-1, 0, 0, 1}};

        Problem p = new Problem();
        int[][] wts = p.constant(weights);
        int[][] im = p.constant(img);

        int[][] out = p.convolve(im, wts);
        double[][] conved = p.get(out);
        System.out.println(Arrays.deepToString(conved));
    }
}
