package examples;

import models.CombinedModel;
import models.Matrix;
import models.MatrixBias;
import models.MatrixMatrixBias;

import static examples.Tree.hiddenSize;

public class Cell extends CombinedModel{
    MatrixBias forgetModel;
    MatrixBias changeModel;
    MatrixBias newCellModel;

    MatrixBias shouldSplit;
    Matrix generateCharacter;

    public int size;
    public int chars;

    public Cell(int size, int chars) {
        this.size = size;
        this.chars = chars;

        forgetModel = new MatrixBias(size, size);
        changeModel = new MatrixBias(size, size);
        newCellModel = new MatrixBias(size, size);

        shouldSplit = new MatrixBias(size, 1);
        generateCharacter = new Matrix(size, chars);


        setupSubmodels(
                forgetModel,
                changeModel,
                newCellModel,
                shouldSplit,
                generateCharacter
        );
    }

    // Returns next hidden
    public int[][] split(int[] state) {
        int[] forget = p.sigmoid(forgetModel.compute(state));
        int[] change = p.tanh(changeModel.compute(state));

        int[] newState = p.add(p.mult(forget, state), change);
        int[] childState = p.tanh(newCellModel.compute(state));

        return new int[][]{newState, childState};
    }

    public int shouldSplit(int[] state) {
        return p.sigmoid(shouldSplit.compute(state)[0]);
    }

    public int[] charDist(int[] state) {
        return p.softmax(generateCharacter.compute(state));
    }
}
