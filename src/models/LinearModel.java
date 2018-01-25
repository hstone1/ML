package models;

import backend.Problem;
import utils.Weights;

/**
 * Created by henry on 8/14/17.
 */
public class LinearModel extends BasicModel {
    BasicModel[] layers;

    public LinearModel(BasicModel... layers) {
        this.layers = layers;
    }

    @Override
    public int neededWeights() {
        int weights = 0;
        for (BasicModel m : layers) {
            weights += m.neededWeights();
        }
        return weights;
    }

    @Override
    public int[] compute(int[] input) {
        int[] out = input;
        for (BasicModel layer : layers) {
            out = layer.compute(out);
        }
        return out;
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        int end = 0;
        for (BasicModel layer : layers) {
            int start = end;
            end += layer.neededWeights();
            layer.setWeights(p, Weights.rip1(weights, start, end - start));
        }
    }
}
