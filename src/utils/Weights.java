package utils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by henry on 8/14/17.
 */
public class Weights {
    public static int[] rip1(int[] arr, int start, int width) {
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

    public static int[][][] rip3(int[] arr, int start, int d1, int d2, int d3) {
        int[][][] out = new int[d1][d2][d3];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    out[i1][i2][i3] = arr[start + i1 * d2 * d3 + i2 * d3 + i3];
                }
            }
        }
        return out;
    }

    public static int[][][][] rip4(int[] arr, int start, int d1, int d2, int d3, int d4) {
        int[][][][] out = new int[d1][d2][d3][d4];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    for (int i4 = 0; i4 < d4; i4++) {
                        out[i1][i2][i3][i4] = arr[start + i1 * d2 * d3 * d4 + i2 * d3 * d4 + i3 * d4 + i4];
                    }
                }
            }
        }
        return out;
    }

    public static void save(double[] weights, String filename) {
        try {
            Writer fr = new FileWriter(filename);
            fr.write(""+ weights.length + '\n');
            for (int i = 0; i < weights.length; i++) {
                fr.write("" + weights[i] + '\n');
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] load(String filename) {
        try {
            Scanner s = new Scanner(new File(filename));
            int l = s.nextInt();
            double[] weights = new double[l];
            for (int i = 0;i < l;i++) {
                weights[i] = s.nextDouble();
            }
            s.close();
            return weights;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
