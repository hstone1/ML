import backend.Problem;
import initializers.Initializers;
import optimizers.Adagrad;
import optimizers.Optimizer;
import optimizers.SGD;
import utils.Weights;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        double[] weights = new double[561];
        Initializers.normalSmall.fill(weights);

        Problem p = new Problem();

        int[] wts = p.constant(weights);

        int[][][] conv1 = Weights.rip3(wts, 0, 4, 2, 2);
        int[] bias1 = Weights.rip1(wts, 16, 4);
        int[][][][] conv2 = Weights.rip4(wts, 20, 6, 4, 2, 2);
        int[] bias2 = Weights.rip1(wts, 116, 6);
        int[][] mult1 = Weights.rip2(wts, 122, 15, 24);
        int[] bias3 = Weights.rip1(wts, 482, 15);
        int[][] mult2 = Weights.rip2(wts, 497, 4, 15);
        int[] bias4 = Weights.rip1(wts, 557, 4);

        int[] out = pred(p, p.constant(new double[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}}),
                conv1, bias1,
                conv2, bias2,
                mult1, bias3,
                mult2, bias4);

        int[] out2 = pred(p, p.constant(new double[][]{
                        {0, 0, 0, 0},
                        {0, 0.1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}}),
                conv1, bias1,
                conv2, bias2,
                mult1, bias3,
                mult2, bias4);

//        System.out.println(Arrays.toString(p.get(out)));
//        System.out.println(Arrays.toString(p.get(out2)));
        train();
    }

    public static void train() {
        Optimizer o = new Adagrad(0.001);
        double[] weights = new double[561];
        Initializers.normalSmall.fill(weights);

        for (int j = 0; j < 50000; j++) {
            int[][] pos = Board.start();
            while (true) {
                boolean[] plays = Board.canPlay(pos);
                int a = 1;
                int pick = -1;
                for (int i = 0; i < 4; i++) {
                    if (plays[i]) {
                        if (Math.random() < 1.0 / a++) {
                            pick = i;
                        }
                    }
                }

                if (pick == -1) {
                    //System.out.println("Finished Game");
                    break;
                }

                Problem p = new Problem();
                int[] wts = p.constant(weights);
                int[][][] conv1 = Weights.rip3(wts, 0, 4, 2, 2);
                int[] bias1 = Weights.rip1(wts, 16, 4);
                int[][][][] conv2 = Weights.rip4(wts, 20, 6, 4, 2, 2);
                int[] bias2 = Weights.rip1(wts, 116, 6);
                int[][] mult1 = Weights.rip2(wts, 122, 15, 24);
                int[] bias3 = Weights.rip1(wts, 482, 15);
                int[][] mult2 = Weights.rip2(wts, 497, 4, 15);
                int[] bias4 = Weights.rip1(wts, 557, 4);

                int[] pred = pred(p, p.constant(Board.toTrainable(pos)),
                        conv1, bias1,
                        conv2, bias2,
                        mult1, bias3,
                        mult2, bias4);

                pos = Board.move(pos, pick);

                boolean[] cp2 = Board.canPlay(pos);
                int l = 0;
                if (hasTrue(cp2)) {
                    double[] pred2 = p.get(pred(p,  p.constant(Board.toTrainable(pos)),
                            conv1, bias1,
                            conv2, bias2,
                            mult1, bias3,
                            mult2, bias4));

                    double max = Double.MIN_VALUE;
                    for (int i = 0; i < 4; i++) {
                        if (cp2[i] && pred2[i] > max) {
                            max = pred2[i];
                        }
                    }

                    int err = p.sub(p.constant(max + 0.1), pred[pick]);
                    l = p.mult(err, err);
                    //System.out.println(Arrays.toString(p.get(pred)));

                } else {
                    int err = p.sub(pred[pick], p.constant(0));
                    l = p.mult(err, err);
                    System.out.println(String.format("%.5f", p.get(l)));
                    p.backprop(l);
                    weights = o.optimize(weights, p.deriv(wts));
                }


                //System.out.println(ts(pos));
            }
        }
    }

    public static String ts(int[][] b) {
        StringBuilder s = new StringBuilder();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                s.append(b[y][x]).append('\t');
            }
            s.append('\n');
        }
        return s.toString();
    }

    public static double max(double[] d) {
        double max = Double.MIN_VALUE;
        for (int i = 0;i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    public static boolean hasTrue(boolean[] d) {
        for (int i = 0;i < d.length; i++) {
            if (d[i]) {
                return true;
            }
        }
        return false;
    }


    /*
        2 * 2 * 4 + 4
        2 * 2 * 6 * 4 + 6
        (2 * 2 * 6 + 1) * 15
        (15 + 1) * 4
    */

    public static int[] pred(Problem p, int[][] board, int[][][] conv1, int[] bias1, int[][][][] conv2, int[] bias2, int[][] weights1, int[] bias3, int[][] weights2, int[] bias4) {
        int[][][] hidden = p.tanh(p.convolve(board, conv1, bias1));
        int[][][] done = p.tanh(p.convolve(hidden, conv2, bias2));

        int[] l = new int[]{done.length, done[0].length, done[0][0].length};

        int[] flat = new int[l[0] * l[1] * l[2]];
        for (int i = 0; i < l[0]; i++) {
            for (int x = 0; x < l[1]; x++) {
                for (int y = 0; y < l[2]; y++) {
                    flat[i * l[1] * l[2] + x * l[2] + y] = done[i][x][y];
                }
            }
        }

        int[] hidden2 = p.tanh(p.multBias(weights1, flat, bias3));
        return p.multBias(weights2, hidden2, bias4);
    }
}
