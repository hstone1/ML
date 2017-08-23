package models;

import backend.Problem;
import utils.Weights;

/**
 * Created by henry on 8/14/17.
 */
public class DenseLayer implements Model {
    private int inputSize;
    private int outputSize;

    private int[][] mult;
    private int[] bias;

    private Activation act;

    public DenseLayer(int inputSize, int outputSize, Activation act) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;

        this.act = act;
    }

    @Override
    public int neededWeights() {
        return outputSize + outputSize * inputSize;
    }


    @Override
    public int[] compute(Problem p, int[] input) {
        int[] res = p.add(p.mult(mult, input), bias);
        switch (act) {
            case TANH: return p.tanh(res);
            default: return res;
        }
    }

    @Override
    public void setWeights(int[] weights) {
        mult = Weights.rip2(weights, 0, outputSize, inputSize);
        bias = Weights.rip(weights, inputSize * outputSize, outputSize);
    }
}
