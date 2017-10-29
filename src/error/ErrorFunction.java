package error;

import backend.Problem;

import java.util.Arrays;

/**
 * Created by henry on 8/20/17.
 */
@FunctionalInterface
public interface ErrorFunction {
    ErrorFunction MeanSquaredError = (p, real, pred) -> {
        int[] indivErr = p.sub(real, pred);
        return p.dot(indivErr, indivErr);
    };


    double a = 0;
    ErrorFunction catagoricalCrossEntropy = (p, real, pred) -> {
        int err = p.dot(
                real,
                p.elementwise(pred, (pr, v) -> pr.ln(v))
        );
        int e = p.sub(p.constant(0), err);
        return e;
    };

    public int error(Problem p, int[] real, int[] pred);

    static int argmax(double[] d) {
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
}
