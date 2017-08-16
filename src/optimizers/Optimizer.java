package optimizers;

public interface Optimizer {
    double[] optimize(double[] weights, double[] derivs);
}