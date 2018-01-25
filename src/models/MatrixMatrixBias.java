package models;

public class MatrixMatrixBias extends CombinedModel{
    Matrix mat1;
    Matrix mat2;

    Bias bias;

    public MatrixMatrixBias(int inputSize1, int inputSize2, int outputSize) {
        mat1 = new Matrix(inputSize1, outputSize);
        mat1 = new Matrix(inputSize2, outputSize);
        bias = new Bias(outputSize);

        setupSubmodels(mat1, mat2, bias);
    }

    public int[] compute(int[] input1, int[] input2) {
        return bias.compute(
                p.add(
                        mat1.compute(input1),
                        mat2.compute(input2)));
    }
}
