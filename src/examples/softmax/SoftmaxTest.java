package examples.softmax;

import backend.Problem;

import java.util.Arrays;

public class SoftmaxTest {
    public static void main(String[] args) {
        double[] a = new double[]{1,2,3};

        Problem p = new Problem();

        int[] in = p.constant(a);
        int[] out = p.softmax(in);
        int o = p.sum(out);

        p.backprop(o);
        System.out.println(Arrays.toString(p.get(out)));
        System.out.println(Arrays.toString(p.deriv(in)));
    }
}
