import backend.Problem;
import initializers.Initializers;
import initializers.NumpyF8Initializer;
import initializers.NumpyU1Initializer;
import optimizers.Adagrad;
import optimizers.Optimizer;
import utils.Weights;

/**
 * Created by henry on 12/1/17.
 */
public class Main {
    public static void main(String[] args) {
        double[][][][] imagesDoubles = new double[24838][12][40][3];
        double[][] locationsDoubles = new double[24838][2];

        new NumpyU1Initializer("data/test_eye_images.npy").fill(imagesDoubles);
        new NumpyF8Initializer("data/test_eye_locations.npy").fill(locationsDoubles);

        double[] means = new double[2];
        for (int i = 0; i < 24838; i++) {
            means[0] += locationsDoubles[i][0];
            means[1] += locationsDoubles[i][1];
        }

        means[0] /= 24838;
        means[1] /= 24838;

        double[] vars = new double[2];
        for (int i = 0; i < 24838; i++) {
            vars[0] += (locationsDoubles[i][0] - means[0]) * (locationsDoubles[i][0] - means[0]);
            vars[1] += (locationsDoubles[i][1] - means[1]) * (locationsDoubles[i][1] - means[1]);
        }

        vars[0] /= 24838;
        vars[1] /= 24838;

        System.out.println("ERROR: " + (vars[0] + vars[1]) / 2);

        // DataPrep.shuffle(imagesDoubles, locationsDoubles);

        int numLocs = 50;
        double[] weights = new double[2 * numLocs + 2 * 3 * numLocs + 2];
        Initializers.normal.fill(weights);

        Optimizer o = new Adagrad(0.01);

        for (int epoch = 0; epoch < 1000; epoch++) {
            double agr_loss = 0;
            for (int batch = 0; batch < 400; batch++) {
                Problem p = new Problem();

                int[][][][] images = new int[50][][][];
                int[][] y = new int[50][];
                for (int i = 0; i < 50; i++) {
                    images[i] = p.constant(imagesDoubles[batch * 50 + i]);
                    y[i] = p.constant(locationsDoubles[batch * 50 + i]);
                }

                int[] x = p.constant(new double[]{6, 10});
                int[] wts = p.constant(weights);
                int[][] locs = Weights.rip2(wts, 0, numLocs, 2);
                int[][] mult1 = Weights.rip2(wts, numLocs * 2, numLocs, 3);
                int[][] mult2 = Weights.rip2(wts, numLocs * 5, numLocs, 3);
                int bias1 = wts[numLocs * 8];
                int bias2 = wts[numLocs * 8 + 1];


                int[] diffsy = new int[images.length];
                int[] diffsx = new int[images.length];
                for (int i = 0; i < images.length; i++) {
                    int valy = classifyLocation(p, images[i], x, locs, mult1, bias1);
                    int valx = classifyLocation(p, images[i], x, locs, mult2, bias2);

                    diffsy[i] = p.sub(valy, y[i][1]);
                    diffsx[i] = p.sub(valx, y[i][0]);
                }

                int lossy = p.mult(p.square(diffsy), p.constant(1.0 / 50));
                int lossx = p.mult(p.square(diffsx), p.constant(1.0 / 50));
                int loss = p.average(lossx, lossy);
                p.backprop(loss);

                agr_loss += p.get(loss);

                weights = o.optimize(weights, p.deriv(wts));
            }
            System.out.println(agr_loss / 400);
            //System.out.println(Arrays.deepToString(Weights.rip2(weights, 0, numLocs, 2)));
        }
    }

    public static int classifyLocation(Problem p, int[][][] image, int[] center, int[][] locs, int[][] weights, int bias) {
        int[] pixContribs = new int[locs.length];
        for (int i = 0; i < locs.length; i++) {
            int[] pix = getPixel(p, image, p.add(center, p.mult(locs[i], p.constant(5))));
            pixContribs[i] = p.dot(weights[i], pix);
        }
        return p.add(p.sum(pixContribs), bias);
    }

    public static int[] getPixel(Problem p, int[][][] image, int[] loc) {
        int ind_fy = (int) p.get(loc[0]);
        int ind_fx = (int) p.get(loc[1]);

        if (ind_fx < 0 || ind_fy < 0 || ind_fy + 1 >= image.length || ind_fx + 1 >= image[0].length) {
            return p.zeros(image[0][0].length);
        }

        int fy = p.constant(ind_fy);
        int fx = p.constant(ind_fx);

        int l = p.sub(loc[0], fx);
        int t = p.sub(loc[1], fy);
        int r = p.sub(p.one(), l);
        int b = p.sub(p.one(), t);

        int[] tl_contrib = p.mult(image[ind_fy][ind_fx], p.mult(b, r));
        int[] tr_contrib = p.mult(image[ind_fy + 1][ind_fx], p.mult(b, l));
        int[] bl_contrib = p.mult(image[ind_fy][ind_fx + 1], p.mult(t, r));
        int[] br_contrib = p.mult(image[ind_fy + 1][ind_fx + 1], p.mult(t, l));

        return p.sum_arrays(tl_contrib, tr_contrib, bl_contrib, br_contrib);
    }
}
