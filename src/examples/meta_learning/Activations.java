package examples.meta_learning;

import backend.MetaProblem;
import backend.Problem;
import initializers.Initializers;
import models.BasicModel;
import models.NeuralNetwork;
import optimizers.Adagrad;
import optimizers.Optimizer;
import utils.Weights;

import java.util.Arrays;

public class Activations {
    public static void main(String[] args) {
        BasicModel metaNN = new NeuralNetwork(1,5,1);

        double[] metaWeights = new double[metaNN.neededWeights()];
        Initializers.uniformSmall.fill(metaWeights);
        Optimizer metaOpt = new Adagrad(0.002);


        for (int i = 0;i < 1;i++) {
            Problem p = new Problem();
            int[] metaWts = p.constant(metaWeights);
            metaNN.setWeights(p, metaWts);

            int backtrackLoc = p.getMarker();

            int[] losses = new int[1];
            for (int l = 0; l < losses.length; l++) {
                losses[l] = randomLoss(p, metaNN);
            }
            int loss = p.average(losses);


            System.out.println(p.get(loss));
            p.backprop(loss);
            metaWeights = metaOpt.optimize(metaWeights, p.deriv(metaWts));
        }
    }

    public static int randomLoss(Problem p, BasicModel meta) {
        double[][] x = new double[5][2];
        double[] y = new double[5];
        Initializers.easy.fill(x);
        Initializers.easy.fill(y);
        return loss(p, meta, x, y);
    }

    public static int loss(Problem p, BasicModel meta, double[][] X, double[] Y) {
        int[][] pX = p.constant(X);
        int[] pY = p.constant(Y);


        double[] weightsOrig = new double[]{0, 1, 2, -1, 0, 1, -2, -1, 0, 0, 1, 2, 3, -1, -1, 1, 0, -1, 1, 1, 1};
        int[] weights = p.constant(weightsOrig);

        for (int itter = 0; itter < 5; itter++) {
            MetaProblem mp = new MetaProblem(p);

            int[] wts = mp.constantFromP(weights);

            int[][] mult1 = Weights.rip2(wts, 0, 5, 2);
            int[] bias1 = Weights.rip1(wts, 10, 5);
            int[] mult2 = Weights.rip1(wts, 15, 5);
            int bias2 = wts[20];

            int[][] x = mp.constantFromP(pX);
            int[] y = mp.constantFromP(pY);

            System.out.println("x: " + Arrays.deepToString(p.get(mp.get(x))));
            System.out.println("Mult1: " + Arrays.deepToString(p.get(mp.get(mult1))));
            System.out.println("Bias1: " + Arrays.toString(p.get(mp.get(bias1))));



            int[] losses = new int[x.length];
            for (int i = 0; i < x.length; i++) {
                int[] hidden = mp.multBias(mult1, x[i], bias1);
                System.out.println("Hidden" + (i + 1) + ": " + Arrays.toString(p.get(mp.get(hidden))));
                int out = mp.dotAdd(hidden, mult2, bias2);
                losses[i] = mp.square(mp.sub(out, y[i]));
            }
            int loss = mp.average(losses);

            System.out.println(p.get(mp.get(loss)));

            mp.backprop(loss);

            int[] derivs = mp.deriv(wts);

            //System.out.println(Arrays.toString(p.get(derivs)));

            weights = p.add(weights, p.mult(derivs, p.constant(-0.2)));
        }

        System.out.println();

        return 0;
    }
}
