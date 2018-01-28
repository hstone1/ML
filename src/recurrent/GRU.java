package recurrent;

import backend.Problem;
import models.CombinedModel;
import models.MatrixMatrixBias;

public class GRU extends CombinedModel{
    MatrixMatrixBias rModel;
    MatrixMatrixBias zModel;
    MatrixMatrixBias hiddenModel;

    public int hiddenSize;
    public int inputSize;


    public GRU(int inputSize, int hiddenSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;

        rModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);
        zModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);
        hiddenModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);

        setupSubmodels(
                rModel,
                zModel,
                hiddenModel
        );
    }

    // Returns next hidden
    public int[] run(int[] input, int[] hidden) {
        int[] r = p.sigmoid(rModel.compute(input, hidden));
        int[] z = p.sigmoid(zModel.compute(input, hidden));

        int[] oneMinusZ = p.elementwise(z, (p, e) -> p.sub(p.one(), e));
        int[] hiddenNext =
                p.add(
                        p.mult(z, hidden),
                        p.tanh(p.mult(
                                oneMinusZ,
                                hiddenModel.compute(input, p.mult(r, hidden)))));

        return hiddenNext;
    }
}
