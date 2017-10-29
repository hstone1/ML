package examples.meta_learning;

import backend.Problem;
import utils.Weights;

public class Fit {
    public static int[] derivs(Problem p, int[] weights, int[][] input, int[] output) {
        int[][] multed = new int[input.length][5];
        for(int i = 0; i < input.length; i++) {
            for (int j = 0; j < multed[0].length; j++) {
                multed[i][j] = p.sum(
                        p.mult(input[i][0], weights[j * 3 + 0]),
                        p.mult(input[i][1], weights[j * 3 + 1]),
                        weights[j * 3 + 2]
                );
            }
        }

        int[][] multedTanh = new int[multed.length][multed[0].length];
        for(int i = 0; i < multed.length; i++) {
            for (int j = 0; j < multed[0].length; j++) {
                multedTanh[i][j] = p.tanh(multed[i][j]);
            }
        }

        int[] pred = new int[input.length];
        for(int i = 0; i < input.length; i++) {
            pred[i] = p.dotAdd(multedTanh[i], Weights.rip1(weights, 15, 5), weights[20]);
        }

        int[] diff = p.sub(pred, output);

        int loss = p.square(diff);

        //System.out.println(p.get(loss));

        int scale = p.constant(2.0 / input.length);

        int[] derivOfPred = p.mult(diff, scale);




        int[] weightDerivs = p.zeros(21);

        int[][] derivMultedTanh = new int[multed.length][multed[0].length];

        weightDerivs[20] = p.sum(derivOfPred);
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < 5; j++) {
                derivMultedTanh[i][j] = p.mult(derivOfPred[i], weights[15 + j]);
                weightDerivs[15 + j] = p.add(weightDerivs[15 + j], p.mult(derivOfPred[i], multedTanh[i][j]));
            }
        }

        int one = p.constant(1);
        int[][] derivMulted = p.elementwise(derivMultedTanh, multedTanh,
                (pr, d, v) -> pr.mult(d, pr.sub(one, pr.square(v)))
        );


        for (int i = 0; i < 5; i++) {
            int[] vals = new int[derivMulted.length];
            int[] vals0 = new int[derivMulted.length];
            int[] vals1 = new int[derivMulted.length];
            for (int j = 0; j < vals.length; j++) {
                vals[j] = derivMulted[j][i];
                vals0[j] = p.mult(derivMulted[j][i], input[j][0]);
                vals1[j] = p.mult(derivMulted[j][i], input[j][1]);
            }
            weightDerivs[i * 3 + 2] = p.sum(vals);
            weightDerivs[i * 3 + 1] = p.sum(vals1);
            weightDerivs[i * 3] = p.sum(vals0);
        }

        return weightDerivs;
    }

    public static int loss(Problem p, int[] weights, int[][] input, int[] output) {
        int[][] multed = new int[input.length][5];
        for(int i = 0; i < input.length; i++) {
            for (int j = 0; j < multed[0].length; j++) {
                multed[i][j] = p.sum(
                        p.mult(input[i][0], weights[j * 3 + 0]),
                        p.mult(input[i][1], weights[j * 3 + 1]),
                        weights[j * 3 + 2]
                );
            }
        }

        int[][] multedTanh = new int[multed.length][multed[0].length];
        for(int i = 0; i < multed.length; i++) {
            for (int j = 0; j < multed[0].length; j++) {
                multedTanh[i][j] = p.tanh(multed[i][j]);
            }
        }

        int[] pred = new int[input.length];
        for(int i = 0; i < input.length; i++) {
            pred[i] = p.dotAdd(multedTanh[i], Weights.rip1(weights, 15, 5), weights[20]);
        }

        int[] diff = p.sub(pred, output);

        return p.square(diff);
    }
}
