package examples.xor;

import backend.Problem;
import initializers.Initializers;
import models.BasicModel;
import models.NeuralNetwork;

/**
 * Created by henry on 8/13/17.
 */
public class XorModel {
    public static void main(String[] args) {
        int itter = 5000000;
        long t1 = System.currentTimeMillis();

        double[] weights = new double[13];
        Initializers.normal.fill(weights);

        double[][] X = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[] y = new double[]{0, 1, 1, 0};

        BasicModel nn = new NeuralNetwork(2, 3, 1);

        for (int i = 0;i < itter;i++) {
            Problem p = new Problem();
            nn.setWeights(p, p.constant(weights));
            int l = loss(p, nn, X, y);
            p.backprop(l);
            for (int j = 0; j < 13; j++) {
                weights[j] -= p.deriv(j) * 0.05;
            }
        }

        System.out.println(String.format("Execution time for %d iterations: %.3fs", itter, 0.001 * (System.currentTimeMillis() - t1)));
    }

    public static int loss(Problem p, BasicModel m, double[][] X, double[] Y) {
        int[][] x = p.constant(X);
        int[] y = p.constant(Y);

        int[] err = new int[y.length];
        for (int i = 0; i < y.length; i++) {
            err[i] = p.sub(y[i], m.compute(x[i])[0]);
        }
        return p.dot(err, err);
    }

}
