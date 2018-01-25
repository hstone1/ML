package examples;

import backend.Problem;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by henry on 12/1/17.
 */
public class ArraySumTest {
    public static void main(String[] args) {
        Problem p = new Problem();
        int[] a = p.constant(new double[]{1, 2, 3, 5});
        int[] b = p.constant(new double[]{4, 3, 4, 3});
        int[] c = p.constant(new double[]{1, 2, 3, 4});

        int[] out = p.sum_arrays(a, b, c);
        System.out.println(Arrays.toString(out));
        System.out.println(Arrays.toString(p.get(out)));
        p.backprop(p.sum(out));
        System.out.println(Arrays.toString(p.deriv(b)));
    }
}
