package recurrent;

import backend.Problem;
import models.CombinedModel;
import models.MatrixMatrixBias;

public class GRU extends CombinedModel{
    MatrixMatrixBias rModel;
    MatrixMatrixBias zModel;
    MatrixMatrixBias hiddenModel;


    public GRU(int inputSize, int hiddenSize, int outputSize) {
        rModel = new MatrixMatrixBias(inputSize, hiddenSize, outputSize);
        zModel = new MatrixMatrixBias(inputSize, hiddenSize, outputSize);
        hiddenModel = new MatrixMatrixBias(inputSize, hiddenSize, outputSize);
    }

    // Returns next hidden
    public int[] run(int[] input, int[] hidden) {
        int[] r = p.sigmoid(rModel.compute(input, hidden));
        int[] z = p.sigmoid(zModel.compute(input, hidden));

        int one = p.constant(1);
        int[] hiddenNext = p.mult(z, hidden) +
                p.elementwise(, (p, val) -> p.sub(p))


                hiddenModel.compute(input, p.mult(r, hidden));
        return null;
    }

    @Override
    public int neededWeights() {
        return 0;
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {

    }
}
