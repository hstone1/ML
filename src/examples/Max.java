package examples;

import models.*;
import optimizers.Adagrad;
import optimizers.Optimizer;
import optimizers.SGD;

/**
 * Created by henry on 8/21/17.
 */
public class Max {
    public static void main(String[] args) {
        int samples = 70;
        int itters = 10000;

        double[][] X = new double[samples][2];
        double[][] y = new double[samples][1];

        for (int i = 0; i < samples; i++) {
            X[i][0] = Math.random();
            X[i][1] = Math.random();
            y[i][0] = X[i][0] > X[i][1] ? X[i][0] : X[i][1];
        }

        Model m = new LinearModel(
                new DenseLayer(2, 8, Activation.TANH),
                new DenseLayer(8, 1, Activation.LINEAR));
        Optimizer o = new SGD(0.1);
        Trainer t = new Trainer(m);
        for (int i = 0; i < itters; i++) {
            t.train(X, y, o);
        }
        System.out.println(t.train(X, y, o));

    }
}
