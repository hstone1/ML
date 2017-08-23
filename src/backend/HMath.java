package backend;

/**
 * Created by henry on 8/13/17.
 */
public class HMath {
    public static double tanh(double x) {
        if (x > 20) {
            return 1.0;
        } else if (x < -20) {
            return -1.0;
        }
        double sx = x / 32;
        double s = 1;
        double t = 1;
        for (int i = 1;i <= 7;i++) {
            t *= sx / i;
            s += t;
        }
        s *= s;
        s *= s;
        s *= s;
        s *= s;
        double ex = s * s;
        double enx = 1 / ex;
        return (ex - enx) / (ex + enx);
    }

    public static double sigmoid(double x) {
        if (x > 20) {
            return 1.0;
        } else if (x < -20) {
            return 0.0;
        }
        double sx = x / 32;
        double s = 1;
        double t = 1;
        for (int i = 1;i <= 7;i++) {
            t *= sx / i;
            s += t;
        }
        s *= s;
        s *= s;
        s *= s;
        s *= s;
        double ex = s * s;
        double enx = 1 / ex;
        return 1 / (1 + enx);
    }

    public static double exp(double x) {
        if (x > 20) {
            return Math.exp(x);
        } else if (x < -20) {
            return 0.0;
        }
        double sx = x / 32;
        double s = 1;
        double t = 1;
        for (int i = 1; i <= 7; i++) {
            t *= sx / i;
            s += t;
        }
        s *= s;
        s *= s;
        s *= s;
        s *= s;
        return s * s;
    }
}
