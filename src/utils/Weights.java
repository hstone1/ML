package utils;

/**
 * Created by henry on 8/14/17.
 */
public class Weights {
    public static int[] rip(int[] arr, int start, int width) {
        int[] out = new int[width];
        for (int i = 0; i < width; i++) {
            out[i] = arr[start + i];
        }
        return out;
    }

    public static int[][] rip2(int[] arr, int start, int height, int width) {
        int[][] out = new int[height][width];
        int l = height * width;
        for (int i = 0;i < l; i++) {
            out[i / width][i % width] = arr[i + start];
        }
        return out;
    }
}
