package examples.error_pred;

import error.ErrorFunction;
import models.*;
import optimizers.Optimizer;
import optimizers.SGD;

import java.util.Random;

/**
 * Created by henry on 8/20/17.
 */
public class TestErrorPred {
    public static void main(String[] args) {
        double[][] X = new double[40][1];
        double[][] y = new double[40][1];

        Random r = new Random();

        for(int i = 0; i < 40;i++) {
            X[i][0] = Math.random();
            y[i][0] = Math.exp(X[i][0] + Math.random() * 0.2 - 0.1);
        }

        Optimizer o = new SGD(0.01);
        ErrorFunction e = new DeviationError();
        Model m = new LinearModel(
                new DenseLayer(1, 5, Activation.TANH),
                new DenseLayer(5, 5, Activation.TANH),
                new Split(
                        new DenseLayer(5, 1, Activation.LINEAR),
                        new DenseLayer(5, 1, Activation.SOFTPLUS)
                ));
        Trainer t = new Trainer(m);

        for (int i = 0; i < 100000; i++) {
            t.train(X, y, o, e);
        }
        System.out.println(t.train(X, y, o, e));

        for (int i = 0; i <= 100; i += 1) {
            double[] in = new double[]{i / 100.0};
            double[] ot = t.predict(in);
            System.out.println(String.format("%.5f\t%.5f\t%.5f", in[0], ot[0], ot[1]));
        }

        for (int i = 0; i < 40; i += 1) {
            System.out.println(String.format("%.5f\t%.5f", X[i][0], y[i][0]));
        }


    }
}
