package models;

public class MatrixBias extends CombinedModel{
    Matrix mat;
    Bias bias;

    public MatrixBias(int inputSize, int outputSize) {
        mat = new Matrix(inputSize, outputSize);
        bias = new Bias(outputSize);

        setupSubmodels(mat, bias);
    }

    public int[] compute(int[] input) {
        return bias.compute(mat.compute(input));
    }
}
