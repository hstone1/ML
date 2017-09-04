package examples.Convolution;

import backend.Problem;
import data.MNIST;
import initializers.Initializers;
import optimizers.Optimizer;
import optimizers.SGD;
import utils.DataPrep;
import utils.WeightManager;
import utils.Weights;

import java.util.Arrays;

/**
 * Created by henry on 8/21/17.
 */
public class MnistTest {
    // NTHREADS = 1 -> 5 EPOCHS in 80s -> ~16s per epoch
    // NTHREADS = 2 -> 5 EPOCHS in 58s -> ~11.5s per epoch
    // NTHREADS = 4 -> 5 EPOCHS in 54s -> ~11s per epoch
    public static final int NTHREADS = 2;

    public static void main(String[] args) {
        boolean test = true;

        WeightManager wm = new WeightManager();
        wm.add(12, 5, 5).add(12).add(10, 12, 5, 5).add(10);
        wm.add(50, 160).add(50).add(10, 50);

        if (!test) {
            double[][][] X = MNIST.trainData();
            double[][] Y = MNIST.trainLabels();
            assert X != null;
            assert Y != null;

            double[][][][] sX = new double[NTHREADS][100 / NTHREADS][][];
            double[][][] sY = new double[NTHREADS][100 / NTHREADS][];

            double[] weights = new double[wm.getTotalSize()];
            Initializers.normalSmall.fill(weights);
            Optimizer o = new SGD(0.05);

            for (int epoch = 0; epoch < 400; epoch++) {
                System.out.println("Epoch: " + epoch + " - " + System.currentTimeMillis());
                DataPrep.shuffle(X, Y);

                for (int batch = 0; batch < 60; batch++) {

                    double[][] derivs = new double[NTHREADS][];
                    Thread[] threads = new Thread[NTHREADS];
                    for ( int thread = 0; thread < NTHREADS; thread++) {
                        double[] finalWeights = weights;
                        int t = thread;

                        for (int j = 0; j < 100 / NTHREADS; j++) {
                            sX[thread][j] = X[batch * 100 + thread * 100 / NTHREADS + j];
                            sY[thread][j] = Y[batch * 100 + thread * 100 / NTHREADS + j];
                        }

                        threads[thread] = new Thread(
                                () -> {
                                    Problem p = new Problem();
                                    int[] wts = p.constant(finalWeights);
                                    int l = loss(p, wm, sX[t], sY[t]);
                                    p.backprop(l);
                                    derivs[t] = p.deriv(wts);
                                }
                        );
                        threads[thread].start();
                    }

                    double[] realDerivs = new double[weights.length];
                    for ( int thread = 0; thread < NTHREADS; thread++) {
                        try {
                            threads[thread].join();
                        } catch (InterruptedException ignored) { }
                        for (int i = 0;i < realDerivs.length;i++) {
                            realDerivs[i] += derivs[thread][i] / NTHREADS;
                        }
                    }

                    weights = o.optimize(weights, realDerivs);
                }
            }

            Weights.save(weights, "data/conv_digit_weights5-142_error.dat");
        } else {
            double[][][] X = MNIST.testData();
            double[][] Y = MNIST.testLabels();
            double[] weights = Weights.load("data/conv_digit_weights5-142_error.dat");
            int corr = 0;

            for (int i = 2000;i < 3000;i++) {
                Problem p = new Problem();
                int[] wts = p.constant(weights);

                int[] pred = pred(p, p.constant(X[i]),
                        wm.get3(0), wm.get1(1), wm.get4(2), wm.get1(3), wm.get2(4), wm.get1(5), wm.get2(6));
                int m = argmax(p.get(pred));
                corr += (m == argmax(Y[i]) ? 1 : 0);
                if (m != argmax(Y[i])) {
                    MNIST.draw(X[i]);
                    System.out.println(i + "\t" + m + "\t" + argmax(Y[i]));
                }
            }
            System.out.println(corr / 10000.0);
        }
    }

    public static <T> void stuff(T[] o) {
        System.out.println(o.length);
    }

    public static int loss(Problem p, WeightManager wm, double[][][] X, double[][] Y) {
        int[][][] x = p.constant(X);
        int[][] y = p.constant(Y);

        int[] l = new int[y.length];

        int acc = 0;
        for (int i = 0; i < y.length; i++) {
            int[] pred = pred(p, x[i],
                    wm.get3(0), wm.get1(1), wm.get4(2), wm.get1(3), wm.get2(4), wm.get1(5), wm.get2(6));
            int corr = argmax(Y[i]);
            l[i] = p.sub(p.constant(0), p.ln(pred[corr]));
            if (argmax(p.get(pred)) == corr) {
                acc++;
            }
        }
        return p.average(l);
    }

    public static int argmax(double[] d) {
        int a = 0;
        double max = d[0];
        for (int i = 1; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
                a = i;
            }
        }
        return a;
    }
    

    public static int[] pred(Problem p, int[][] image,
                      int[][][] conv1, int[] bias1,
                      int[][][][] conv2, int[] bias2,
                      int[][] dense1, int[] bias3,
                      int[][] dense2) {
        // Image is 28 by 28
        int[][][] s1 = p.convolve(image, conv1, bias1);         // Weights: 250 + 10 = 260
        // Image is 10 by 24 by 24
        int[][][] s2 = p.relu(p.maxPool(s1));                   // Weights: 0
        // Image is now 10 by 12 by 12
        int[][][] s3 = p.convolve(s2, conv2, bias2);            // Weights: 216 + 3 = 219
        // Image is now 5 by 8 by 8
        int[][][] s4 = p.relu(p.maxPool(s3));                   // Weights: 0
        // Image is now 5 by 4 by 4
        int[] s5 = p.flatten(s4);                               // Weights: 0
        // Image is now 80
        int[] s6 = p.relu(p.multBias(dense1, s5, bias3));       // Weights: 76 * 20 = 1520
        // Image is now 20
        int[] s7 = p.softmax(p.mult(dense2, s6));               // Weights: 20 * 10 = 200
        return s7;                                              // Total Weights = 2147
    }
}
