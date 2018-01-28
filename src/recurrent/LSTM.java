package recurrent;

import models.CombinedModel;
import models.MatrixMatrixBias;

public class LSTM extends CombinedModel{
    MatrixMatrixBias fModel;
    MatrixMatrixBias iModel;
    MatrixMatrixBias oModel;
    MatrixMatrixBias cModel;



    public LSTM(int inputSize, int hiddenSize) {
        fModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);
        iModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);
        oModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);
        cModel = new MatrixMatrixBias(inputSize, hiddenSize, hiddenSize);

        setupSubmodels(
                fModel,
                iModel,
                oModel,
                cModel
        );
    }

    // Returns next hidden
    public int[][] run(int[] input, int[] memory, int[] cellState) {
        int[] f = p.sigmoid(fModel.compute(input, memory));
        int[] i = p.sigmoid(iModel.compute(input, memory));
        int[] o = p.sigmoid(oModel.compute(input, memory));
        int[] cAdd = p.tanh(oModel.compute(input, memory));


        int[] nextCellState =
                p.add(
                        p.mult(f, cellState),
                        p.mult(i, cAdd)
                );

        int[] output = p.mult(
                o,
                p.tanh(nextCellState)
        );

        return new int[][]{output, nextCellState};
    }
}
