package initializers;

@FunctionalInterface
public interface Initializer {
    double produceWeight();

    default void fill(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = produceWeight();
        }
    }

    default void fill(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            fill(arr[i]);
        }
    }

}
