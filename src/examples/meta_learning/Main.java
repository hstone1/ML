package examples.meta_learning;

import backend.Problem;
import initializers.Initializers;
import models.BasicModel;
import models.NeuralNetwork;
import optimizers.Adagrad;
import optimizers.Optimizer;

public class Main {
    public static void main(String[] args) {
        BasicModel m = new NeuralNetwork(2,5,1);
        double[] weights = new double[m.neededWeights()];
        Initializers.uniformSmall.fill(weights);

        Optimizer o = new Adagrad(0.002);

        for (int i = 0;i < 50000;i++) {
            Problem p = new Problem();
            int[] wts = p.constant(weights);
            m.setWeights(p, wts);

            int[] losses = new int[1000];
            for (int l = 0; l < losses.length; l++) {
                losses[l] = randomLoss(p, m);
            }

            int loss = p.average(losses);
            System.out.println(p.get(loss));
            p.backprop(loss);
            weights = o.optimize(weights, p.deriv(wts));
        }
    }

    public static int randomLoss(Problem p, BasicModel m) {
        double[][] x = new double[5][2];
        double[] y = new double[5];
        Initializers.uniform.fill(x);
        Initializers.uniform.fill(y);
        return loss(p, m, x, y);
    }

    public static int loss(Problem p, BasicModel m, double[][] X, double[] Y) {
        int[][] x = p.constant(X);
        int[] y = p.constant(Y);

        double[] weights = new double[21];
        Initializers.uniform.fill(weights);
        int[] wts = p.constant(weights);

        int itterations = (int) (Math.random() * 10 + 20);

        for (int i = 0 ;i < itterations; i++) {
            int[] derivs = Fit.derivs(p, wts, x, y);

            int averageDerivsSquared = p.mult(p.constant(1 / derivs.length), p.square(derivs));
            int averageWeightsSquared = p.mult(p.constant(1 / derivs.length), p.square(wts));

            int[] lrs = new int[derivs.length];
            for (int d = 0; d < lrs.length; d++) {
                lrs[d] = m.compute(new int[]{
                        averageDerivsSquared,
                        derivs[d]})[0];
            }

            wts = p.add(wts, p.elementwise(lrs, derivs, Problem::mult));
        }

        return Fit.loss(p, wts, x, y);
    }
}
