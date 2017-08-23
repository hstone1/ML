package examples.Convolution;

import backend.Problem;

import java.util.Arrays;

public class TestConvolution {
    public static void main(String[] args) {
        double[][][] weights = new double[][][]{{
                {2, -1},
                {1, 1}
        }, {
                {-1, -1},
                {-1, 1}
        }};

        double[][][] img = new double[][][]{{
                {0, 1, 1, 0},
                {0, 1, 0, 2},
                {1, 0, 1, 1},
                {2, 2, 0, 1}
        }, {
                {0, 1, 2, 2},
                {0, 1, 1, 1},
                {2, 2, 2, 0},
                {2, 0, 0, 1}
        }};

        Problem p = new Problem();
        int[][][] wts = p.constant(weights);
        int[][][] im = p.constant(img);
        int[][] out = p.convolve(im, wts, p.constant(0));
        int o = p.sum(out);
        p.backprop(o);
        System.out.println(p.deriv(wts[0][0][0]));
        System.out.println(p.get(o));
    }

    public static int[] pred(Problem p, int[][] board, int[][][] conv1, int[] bias1, int[][][][] conv2, int[] bias2, int[][] weights1, int[] bias3, int[][] weights2, int[] bias4) {
        int[][][] hidden = p.tanh(p.convolve(board, conv1, bias1));
        int[][][] done = p.tanh(p.convolve(hidden, conv2, bias2));

        int[] l = new int[]{done.length, done[0].length, done[0][0].length};

        int[] flat = new int[l[0] * l[1] * l[2]];
        for (int i = 0; i < l[0]; i++) {
            for (int x = 0; x < l[1]; x++) {
                for (int y = 0; y < l[2]; y++) {
                    flat[i * l[1] * l[2] + x * l[2] + y] = done[i][x][y];
                }
            }
        }

        int[] hidden2 = p.tanh(p.multBias(weights1, flat, bias3));
        return p.multBias(weights2, hidden2, bias4);
    }
}
