package examples.NetworkConstruction;

import backend.Problem;
import models.Activation;
import models.BasicModel;

public class Network2D extends BasicModel {
    private Activation activation;
    private int inputs;
    private int outputs;
    private int repSize;
    private int depth;
    private int[][][] weights;
    private int[][][] reps;

    public Network2D(int repSize, int depth, int inputs, int outputs, Activation activation) {
        // Assure that network cad divide to the point where all outputs are satisfied.
        assert depth > (outputs - inputs) / 2;

        this.activation = activation;
        this.inputs = inputs;
        this.outputs = outputs;
        this.repSize = repSize;


        this.reps = new int[depth][depth][repSize];
        this.weights = new int[depth][depth][3];
    }

    @Override
    public int neededWeights() {
        return inputs * repSize + repSize * repSize + 2 * repSize;
    }

    @Override
    public int[] compute(int[] input) {
        return new int[0];
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        
    }
}
