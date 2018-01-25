package initializers;

import java.util.Random;

public class Initializers {
    public static Random r = new Random();

    public static Initializer uniform = () -> r.nextDouble() * 2 - 1;

    public static Initializer easy = () -> ((int) (r.nextDouble() * 20 - 10)) / 10.0;

    public static Initializer normal = () -> r.nextGaussian();

    public static Initializer uniformSmall = () -> r.nextDouble() - 0.2;

    public static Initializer normalSmall = () -> r.nextGaussian() * 0.2;

    public static Initializer constant(double c) {
            return () -> c;
    }


}
