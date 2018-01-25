package optimizers;

import utils.Weights;

public interface Optimizer {
    double[] optimize(double[] weights, double[] derivs);

    default double[][] optimize(double[][] weights, double[][] derivs) {
        return Weights.reshape(optimize(
                    Weights.reshape(weights),
                    Weights.reshape(derivs)
            ), weights.length, weights[0]. length);
    }

    default double[][][] optimize(double[][][] weights, double[][][] derivs) {
        return Weights.reshape(optimize(
                Weights.reshape(weights),
                Weights.reshape(derivs)
        ), weights.length, weights[0]. length, weights[0][0].length);
    }
}