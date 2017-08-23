package models;

import backend.Problem;
import utils.Weights;

/**
 * Created by henry on 8/14/17.
 */
public class LinearModel implements Model{
    Model[] layers;

    public LinearModel(Model... layers) {
        this.layers = layers;
    }

    @Override
    public int neededWeights() {
        int weights = 0;
        for (Model m : layers) {
            weights += m.neededWeights();
        }
        return weights;
    }

    @Override
    public int[] compute(Problem p, int[] input) {
        int[] out = input;
        for (Model layer : layers) {
            out = layer.compute(p, out);
        }
        return out;
    }

    @Override
    public void setWeights(int[] weights) {
        int end = 0;
        for (Model layer : layers) {
            int start = end;
            end += layer.neededWeights();
            layer.setWeights(Weights.rip1(weights, start, end - start));
        }
    }
}
