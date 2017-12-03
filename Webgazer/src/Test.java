import initializers.NumpyF8Initializer;

/**
 * Created by henry on 12/2/17.
 */
public class Test {
    public static void main(String[] args) {
        double[][] tobbi_locs = new double[72450][2];
        double[][] webgazer_locs = new double[72450][2];

        new NumpyF8Initializer("data/webgazer.npy").fill(webgazer_locs);
        new NumpyF8Initializer("data/train_eye_locations.npy").fill(tobbi_locs);

        double[] means = new double[2];
        for (int i = 0; i < 72450; i++) {
            means[0] += tobbi_locs[i][0];
            means[1] += tobbi_locs[i][1];
        }

        means[0] /= 72450;
        means[1] /= 72450;

        double errorSumWebgazer= 0;
        double errorMean = 0;
        for (int i = 0;i < 72450;i++) {
            double dx = tobbi_locs[i][0] - webgazer_locs[i][0];
            double dy = tobbi_locs[i][1] - webgazer_locs[i][1];
            double d = Math.pow(dx * dx + dy * dy, 0.01);
            errorSumWebgazer += d;

            dx = tobbi_locs[i][0] - means[0];
            dy = tobbi_locs[i][1] - means[1];
            d = Math.pow(dx * dx + dy * dy, 0.01);
            errorMean += d;
        }

        System.out.println(errorSumWebgazer / 72450 / 2);
        System.out.println(errorMean / 72450 / 2);

    }
}
