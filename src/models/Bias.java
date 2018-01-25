package models;

import backend.Problem;
import utils.Weights;

public class Bias extends BasicModel{
    int[] bias;

    int size;


    public Bias(int size) {
        this.size = size;
    }

    public int[] compute(int[] input) {
        return p.add(input, bias);
    }

    @Override
    public int neededWeights() {
        return size;
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        bias = weights;
    }
}
