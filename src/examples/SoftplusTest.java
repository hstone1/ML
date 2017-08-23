package examples;

import backend.HMath;
import backend.Problem;

/**
 * Created by henry on 8/21/17.
 */
public class SoftplusTest {
    public static void main(String[] args) {
        Problem p = new Problem();
        int in = p.constant(3);
        int o = p.softplus(in);
        System.out.println(p.get(o));
        p.backprop(o);
        System.out.println(p.deriv(in));
        System.out.println(HMath.sigmoid(3));
    }
}
