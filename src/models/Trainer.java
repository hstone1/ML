package models;

import backend.Problem;
import initializers.Initializer;
import initializers.Initializers;
import optimizers.Optimizer;

public class Trainer {
    private static Initializer def = Initializers.normal;

    public double[] weights;
    Model m;

    public Trainer(Model m, Initializer init) {
        this.m = m;
        weights = new double[m.neededWeights()];
        for (int i = 0;i < weights.length;i++) {
            weights[i] = init.produceWeight();
        }
    }

    public Trainer(Model m) {
        this(m, def);
    }

    public double train(double[][] X, double[][] Y, Optimizer opt) {
        assert X.length == Y.length;

        Problem p = new Problem();
        int[] wts = p.constant(weights);
        m.setWeights(wts);

        int[][] x = p.constant(X);
        int[][] y = p.constant(Y);

        int[] err = new int[y.length];
        for (int i = 0; i < y.length; i++) {
            int[] error = p.sub(y[i], m.compute(p, x[i]));
            err[i] = p.dot(error, error);
        }

        int l = p.mult(p.sum(err), p.constant(1.0 / X.length));
        p.backprop(l);

        double[] derivs = new double[weights.length];
        for (int  i = 0; i < derivs.length; i ++) {
            derivs[i] = p.deriv(wts[i]);
        }

        weights = opt.optimize(weights, derivs);

        return p.get(l);
    }
}
