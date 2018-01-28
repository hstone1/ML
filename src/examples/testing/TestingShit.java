package examples.testing;

import backend.Problem;
import initializers.ArrayInitalizer;
import initializers.Initializers;
import models.MatrixBias;

import java.util.Arrays;

public class TestingShit {
    public static void main(String[] args) {
        Problem p = new Problem();
        int a = p.constant(1);
        int ans = p.sigmoid(a);
        p.backprop(ans);
        System.out.println(p.get(ans));
        System.out.println(p.deriv(a));

    }
}
