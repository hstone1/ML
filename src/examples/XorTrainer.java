package examples;

import models.*;
import optimizers.Adagrad;
import optimizers.Optimizer;

import java.util.Arrays;

/**
 * Created by henry on 8/13/17.
 */
public class XorTrainer {
    public static void main(String[] args) {
        int itter = 5000000;
        long t1 = System.currentTimeMillis();

        double[][] X = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] y = new double[][]{{0}, {1}, {1}, {0}};

        Model nn = new LinearModel(
                new DenseLayer(2, 3, Activation.TANH),
                new DenseLayer(3, 1, Activation.LINEAR));
        Trainer t = new Trainer(nn);
        Optimizer opt = new Adagrad(0.3);

        for (int i = 0;i < itter;i++) {
            t.train(X, y, opt);
        }
        System.out.println(t.train(X, y, opt));
        System.out.println(Arrays.toString(t.weights));

        System.out.println(String.format("Execution time for %d iterations: %.3fs", itter, 0.001 * (System.currentTimeMillis() - t1)));
    }
}
