package initializers;

import java.util.Arrays;

public class ArrayInitalizer implements Initializer{
    private final double[] arr;
    private int ind = 0;

    public ArrayInitalizer(double... arr) {
        this.arr = arr;
    }

    @Override
    public double produceWeight() {
        return arr[ind++];
    }
}
