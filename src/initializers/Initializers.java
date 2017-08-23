package initializers;

import java.util.Random;

public class Initializers {
    public static Random r = new Random();

    public static Initializer uniform = () -> {
        return r.nextDouble() * 2 - 1;
    };

    public static Initializer normal = () -> {
        return r.nextGaussian();
    };

    public static Initializer uniformSmall = () -> {
        return r.nextDouble() - 0.2;
    };

    public static Initializer normalSmall = () -> {
        return r.nextGaussian() * 0.2;
    };
}
