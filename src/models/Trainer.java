package models;

import backend.Problem;
import error.ErrorFunction;
import initializers.Initializer;
import initializers.Initializers;
import optimizers.Optimizer;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Trainer {
    private static Initializer def = Initializers.normal;
    private static ErrorFunction error = ErrorFunction.MeanSquaredError;

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
        m.setWeights(p, wts);

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

//                List<Callable<double[]>> threads = new ArrayList<>();
//
//                for ( int thread = 0; thread < NTHREADS; thread++) {
//                    double[] finalWeights = weights;
//                    int t = thread;
//
//                    for (int j = 0; j < 100 / NTHREADS; j++) {
//                        sX[thread][j] = X[batch * 100 + thread * 100 / NTHREADS + j];
//                        sY[thread][j] = Y[batch * 100 + thread * 100 / NTHREADS + j];
//                    }
//
//                    threads.add(new Callable<double[]>() {
//                        @Override
//                        public double[] call() throws Exception {
//                            Problem p = new Problem();
//                            int[] wts = p.constant(finalWeights);
//                            int l = loss(p, wm, sX[t], sY[t]);
//                            p.backprop(l);
//                            return p.deriv(wts);
//                        }
//                    });
//                }
//
//                try {
//                    double[] realDerivs = new double[weights.length];
//                    List<Future<double[]>> res = pool.invokeAll(threads);
//                    for (Future<double[]> f : res) {
//                        double[] derivs = f.get();
//                        for (int i = 0;i < realDerivs.length;i++) {
//                            realDerivs[i] += derivs[i] / NTHREADS;
//                        }
//                    }
//
//                    weights = o.optimize(weights, realDerivs);
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        pool.shutdown();
//
//        Weights.save(weights, "data/conv_digit_weights5-142_error.dat");

    public double train(double[][] X, double[][] Y, Optimizer opt, ErrorFunction ef, ExecutorService pool, int breaks) {
        assert X.length == Y.length;
        assert X.length % breaks == 0;

        double[][][] threadX = new double[breaks][X.length / breaks][];
        double[][][] threadY = new double[breaks][X.length / breaks][];

        List<Callable<DerivAndLoss>> threads = new ArrayList<>();

        for ( int thread = 0; thread < breaks; thread++) {
            double[] finalWeights = weights;
            int t = thread;

            for (int j = 0; j < X.length / breaks; j++) {
                threadX[thread][j] = X[thread * X.length / breaks + j];
                threadY[thread][j] = Y[thread * X.length / breaks + j];
            }

            threads.add(new Callable<DerivAndLoss>() {
                @Override
                public DerivAndLoss call() throws Exception {
                    Problem p = new Problem();
                    int[] wts = p.constant(weights);
                    m.setWeights(p, wts);

                    int[][] x = p.constant(threadX[t]);
                    int[][] y = p.constant(threadY[t]);

                    int[] err = new int[y.length];
                    for (int i = 0; i < y.length; i++) {
                        err[i] = ef.error(p, y[i], m.compute(p, x[i]));
                    }

                    int l = p.mult(p.sum(err), p.constant(1.0 / X.length));
                    p.backprop(l);
                    return new DerivAndLoss(p.deriv(wts), p.get(l));
                }
            });
        }

        try {
            double[] realDerivs = new double[weights.length];
            double realLoss = 0;
            List<Future<DerivAndLoss>> res = pool.invokeAll(threads);
            for (Future<DerivAndLoss> f : res) {
                DerivAndLoss dal = f.get();
                double[] derivs = dal.deriv;
                for (int i = 0;i < realDerivs.length;i++) {
                    realDerivs[i] += derivs[i];
                }
                realLoss += dal.loss;
            }

            weights = opt.optimize(weights, realDerivs);

            return realLoss;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -3.14159265;
        }
    }

    public double trainEpoch(double[][] X, double[][] Y, Optimizer opt, ErrorFunction e, int batchSize) {
        assert X.length == Y.length;

        double loss = 0;

        int a = 0;
        while (a < X.length) {
            int bs = batchSize < X.length - a ? batchSize : X.length - a;
            double[][] batchX = new double[bs][];
            double[][] batchY = new double[bs][];
            System.arraycopy(X, a, batchX, 0, bs);
            System.arraycopy(Y, a, batchY, 0, bs);

            loss += train(batchX, batchY, opt, e) * bs;
            a += bs;
        }

        return loss / X.length;
    }

    public double trainEpoch(double[][] X, double[][] Y, Optimizer opt, ErrorFunction e, int batchSize, ExecutorService pool, int breaks) {
        assert X.length == Y.length;

        double loss = 0;

        int a = 0;
        while (a < X.length) {
            int bs = batchSize < X.length - a ? batchSize : X.length - a;

            double[][] batchX = new double[bs][];
            double[][] batchY = new double[bs][];
            System.arraycopy(X, a, batchX, 0, bs);
            System.arraycopy(Y, a, batchY, 0, bs);

            double l = train(batchX, batchY, opt, e, pool, breaks) * bs;
            loss += l;

            a += bs;
        }

        return loss / X.length;
    }

    public double train(double[][] X, double[][] Y, Optimizer opt) {
        return train(X, Y, opt, error);
    }

    public double[] predict(double[] in) {
        Problem p = new Problem();
        int[] wts = p.constant(weights);
        m.setWeights(p, wts);
        return p.get(m.compute(p, p.constant(in)));
    }

    public double[][] predict(double[][] in) {
        double[][] out = new double[in.length][];

        Problem p = new Problem();
        int[] wts = p.constant(weights);
        m.setWeights(p, wts);

        for (int i = 0;i < in.length; i++) {
            out[i] = p.get(m.compute(p, p.constant(in[i])));
        }

        return out;
    }

    public double[][] predict(double[][] in, ExecutorService pool, int chunks) {
        assert in.length % chunks == 0;

        double[][][] threadIn = new double[chunks][in.length / chunks][];

        List<Callable<double[][]>> threads = new ArrayList<>();
        for ( int thread = 0; thread < chunks; thread++) {
            int t = thread;
            for (int j = 0; j < in.length / chunks; j++) {
                threadIn[thread][j] = in[thread * in.length / chunks + j];
            }

            threads.add(new Callable<double[][]>() {
                @Override
                public double[][] call() throws Exception {
                    Problem p = new Problem();
                    int[] wts = p.constant(weights);
                    m.setWeights(p, wts);

                    double[][] tin = threadIn[t];

                    double[][] out = new double[tin.length][];
                    for (int i = 0;i < tin.length; i++) {
                        out[i] = p.get(m.compute(p, p.constant(tin[i])));
                    }

                    return out;
                }
            });
        }

        try {
            double[][] realOut = new double[in.length][];
            int ind = 0;
            List<Future<double[][]>> res = pool.invokeAll(threads);
            for (Future<double[][]> f : res) {
                double[][] out = f.get();
                System.arraycopy(out, 0, realOut, ind, out.length);
                ind += out.length;
            }

            return realOut;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[][] predict(double[][] in, int chunk) {
        assert in.length % chunk == 0;

        double[][] out = new double[in.length][];

        int a = 0;
        while (a < in.length) {
            Problem p = new Problem();
            int[] wts = p.constant(weights);
            m.setWeights(p, wts);

            for (int i = 0;i < chunk; i++) {
                out[a] = p.get(m.compute(p, p.constant(in[a])));
                a++;
            }
        }

        return out;
    }

    public void save(String filename) {
        File f = new File(filename);
        try (FileWriter w = new FileWriter(f)) {
            for (double weight : weights) {
                w.write(Double.toString(weight) + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DerivAndLoss{
    public double[] deriv;
    public double loss;

    public DerivAndLoss(double[] deriv, double loss){
        this.deriv = deriv;
        this.loss = loss;
    }
}
