package optimizers;

public class SGD implements Optimizer{
    public double lr;

    public SGD (double lr) {
        this.lr = lr;
    }

    @Override
    public double[] optimize(double[] weights, double[] derivs) {
        double[] nw = new double[weights.length];
        for(int i = 0; i < nw.length; i++) {
            nw[i] = weights[i] - derivs[i] * lr;
        }
        return nw;
    }
}
