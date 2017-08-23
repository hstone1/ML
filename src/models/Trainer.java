package models;

import backend.Problem;
import error.ErrorFunction;
import error.MeanSquaredError;
import initializers.Initializer;
import initializers.Initializers;
import optimizers.Optimizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Trainer {
    private static Initializer def = Initializers.normal;
    private static ErrorFunction error = new MeanSquaredError();

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

    public double train(double[][] X, double[][] Y, Optimizer opt, ErrorFunction e) {
        assert X.length == Y.length;

        Problem p = new Problem();
        int[] wts = p.constant(weights);
        m.setWeights(wts);

        int[][] x = p.constant(X);
        int[][] y = p.constant(Y);

        int[] err = new int[y.length];
        for (int i = 0; i < y.length; i++) {
            err[i] = e.error(p, y[i], m.compute(p, x[i]));
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

    public double train(double[][] X, double[][] Y, Optimizer opt) {
        return train(X, Y, opt, error);
    }

    public double[] predict(double[] in) {
        Problem p = new Problem();
        int[] wts = p.constant(weights);
        m.setWeights(wts);
        return p.get(m.compute(p, p.constant(in)));
    }

    public void save(String filename) {
        File f = new File(filename);
        try {
            FileWriter w = new FileWriter(f);
            for (double weight : weights) {
                w.write(Double.toString(weight) + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
