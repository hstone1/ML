package utils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by henry on 8/14/17.
 */
public class Weights {
    public static int[] rip1(int[] arr, int start, int width) {
        int[] out = new int[width];
        System.arraycopy(arr, start, out, 0, width);
        return out;
    }

    public static int[][] rip2(int[] arr, int start, int d1, int d2) {
        int[][] out = new int[d1][d2];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                    out[i1][i2] = arr[start++];
            }
        }
        return out;
    }

    public static int[][][] rip3(int[] arr, int start, int d1, int d2, int d3) {
        int[][][] out = new int[d1][d2][d3];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    out[i1][i2][i3] = arr[start++];
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
                        out[i1][i2][i3][i4] = arr[start++];
                    }
                }
            }
        }
        return out;
    }

    public static int[][][][] reshape(int[] arr, int d1, int d2, int d3, int d4) {
        return rip4(arr, 0, d1, d2, d3, d4);
    }

    public static int[][][] reshape(int[] arr, int d1, int d2, int d3) {
        return rip3(arr, 0, d1, d2, d3);
    }

    public static int[][] reshape(int[] arr, int d1, int d2) {
        return rip2(arr, 0, d1, d2);
    }

    public static int[] reshape(int[][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;

        int[] out = new int[d1 * d2];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                out[start++] = arr[i1][i2];
            }
        }

        return out;
    }

    public static int[] reshape(int[][][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;
        int d3 = arr[0][0].length;

        int[] out = new int[d1 * d2 * d3];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    out[start++] = arr[i1][i2][i3];
                }
            }
        }

        return out;
    }

    public static int[] reshape(int[][][][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;
        int d3 = arr[0][0].length;
        int d4 = arr[0][0][0].length;


        int[] out = new int[d1 * d2 * d3 * d4];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    for (int i4 = 0; i4 < d4; i4++) {
                        out[start++] = arr[i1][i2][i3][i4];
                    }
                }
            }
        }

        return out;
    }









    public static double[] rip1(double[] arr, int start, int width) {
        double[] out = new double[width];
        System.arraycopy(arr, start, out, 0, width);
        return out;
    }

    public static double[][] rip2(double[] arr, int start, int d1, int d2) {
        double[][] out = new double[d1][d2];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                out[i1][i2] = arr[start++];
            }
        }
        return out;
    }

    public static double[][][] rip3(double[] arr, int start, int d1, int d2, int d3) {
        double[][][] out = new double[d1][d2][d3];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    out[i1][i2][i3] = arr[start++];
                }
            }
        }
        return out;
    }

    public static double[][][][] rip4(double[] arr, int start, int d1, int d2, int d3, int d4) {
        double[][][][] out = new double[d1][d2][d3][d4];
        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    for (int i4 = 0; i4 < d4; i4++) {
                        out[i1][i2][i3][i4] = arr[start++];
                    }
                }
            }
        }
        return out;
    }

    public static double[][][][] reshape(double[] arr, int d1, int d2, int d3, int d4) {
        return rip4(arr, 0, d1, d2, d3, d4);
    }

    public static double[][][] reshape(double[] arr, int d1, int d2, int d3) {
        return rip3(arr, 0, d1, d2, d3);
    }

    public static double[][] reshape(double[] arr, int d1, int d2) {
        return rip2(arr, 0, d1, d2);
    }

    public static double[] reshape(double[][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;

        double[] out = new double[d1 * d2];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                out[start++] = arr[i1][i2];
            }
        }

        return out;
    }

    public static double[] reshape(double[][][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;
        int d3 = arr[0][0].length;

        double[] out = new double[d1 * d2 * d3];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    out[start++] = arr[i1][i2][i3];
                }
            }
        }

        return out;
    }

    public static double[] reshape(double[][][][] arr) {
        int d1 = arr.length;
        int d2 = arr[0].length;
        int d3 = arr[0][0].length;
        int d4 = arr[0][0][0].length;


        double[] out = new double[d1 * d2 * d3 * d4];
        int start = 0;

        for (int i1 = 0; i1 < d1; i1++) {
            for (int i2 = 0; i2 < d2; i2++) {
                for (int i3 = 0; i3 < d3; i3++) {
                    for (int i4 = 0; i4 < d4; i4++) {
                        out[start++] = arr[i1][i2][i3][i4];
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
