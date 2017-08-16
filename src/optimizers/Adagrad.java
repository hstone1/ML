package optimizers;

public class Adagrad implements Optimizer{
    public double lr;

    public Adagrad(double lr) {
        this.lr = lr;
    }

    @Override
    public double[] optimize(double[] weights, double[] derivs) {
        double[] nw = new double[weights.length];

        double mag = 0;
        for(int i = 0; i < nw.length; i++) {
            mag += weights[i] * weights[i];
        }
        mag = Math.sqrt(mag);
        double tlr = lr / mag;

        for(int i = 0; i < nw.length; i++) {
            nw[i] = weights[i] - derivs[i] * tlr;
        }
        return nw;
    }
}
