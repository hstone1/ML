package examples.batch_norm;

import backend.Problem;

import java.util.Arrays;

/**
 * Created by henry on 8/21/17.
 */
public class NormalizationTest {
    public static void main(String[] args) {
        Problem p = new Problem();
        int[] cost = p.constant(new double[]{2, 2, 2});
        System.out.println(Arrays.toString(p.get(p.normalize(cost))));
        p.backprop(p.sum(p.normalize(cost)));
        System.out.println(Arrays.toString(p.deriv(cost)));
    }
}
