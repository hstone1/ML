package models;

import backend.Problem;
import models.Model;
import utils.Weights;

public class Matrix extends BasicModel{
    int[][] mat;

    int inputSize;
    int outputSize;


    public Matrix(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public int[] compute(int[] input) {
        return p.mult(mat, input);
    }

    @Override
    public int neededWeights() {
        return inputSize * outputSize;
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        mat = Weights.reshape(weights, outputSize, inputSize);
    }
}
