package examples.xor;

import backend.Problem;
import initializers.Initializer;
import initializers.Initializers;

/**
 * Created by henry on 8/13/17.
 */
public class Xor {
    public static void main(String[] args) {
        int itter = 5000000;
        long t1 = System.currentTimeMillis();

        double[] weights = new double[13];
        Initializers.normal.fill(weights);

        double[][] X = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[] y = new double[]{0, 1, 1, 0};

        for (int i = 0;i < itter;i++) {

            Problem p = new Problem();
            int l = loss(p, weights, X, y);
            p.backprop(l);
            for (int j = 0; j < 13; j++) {
                weights[j] -= p.deriv(j) * 0.05;
            }
        }

        System.out.println(String.format("Execution time for %d iterations: %.3fs", itter, 0.001 * (System.currentTimeMillis() - t1)));
    }

    public static int loss(Problem p, double[] weights, double[][] X, double[] Y) {
        int[] wts = p.constant(weights);
        int[][] x = p.constant(X);
        int[] y = p.constant(Y);

        int[][] mult1 = rip2(wts, 0, 3, 2);
        int[] bias1 = rip(wts, 6, 3);
        int[] mult2 = rip(wts, 9, 3);

        int[] err = new int[y.length];
        for (int i = 0; i < y.length; i++) {
            err[i] = p.sub(y[i], predict(p, x[i], mult1, bias1, mult2, wts[12]));
        }
        return p.dot(err, err);
    }

    public static int predict(Problem p, int[] x, int[][] mult, int[] bias, int[] mult2, int bias2) {
        int[] hidden = p.tanh(p.multBias(mult, x, bias));
        return p.dotAdd(mult2, hidden, bias2);
    }

    public static int[] rip(int[] arr, int start, int width) {
        int[] out = new int[width];
        for (int i = 0; i < width; i++) {
            out[i] = arr[start + i];
        }
        return out;
    }

    public static int[][] rip2(int[] arr, int start, int height, int width) {
        int[][] out = new int[height][width];
        int l = height * width;
        for (int i = 0;i < l; i++) {
            out[i / width][i % width] = arr[i + start];
        }
        return out;
    }
}
