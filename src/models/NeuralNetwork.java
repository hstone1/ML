package models;

/**
 * Created by henry on 8/14/17.
 */
public class NeuralNetwork extends LinearModel {
    public NeuralNetwork(int... lSize) {
        super(generateModels(lSize));
    }

    private static BasicModel[] generateModels(int... lSize) {
        BasicModel[] layers = new BasicModel[lSize.length - 1];
        for (int i = 0;i < lSize.length - 2; i++) {
            layers[i] = new DenseLayer(lSize[i], lSize[i + 1], Activation.TANH);
        }
        layers[layers.length - 1] = new DenseLayer(lSize[lSize.length - 2], lSize[lSize.length - 1], Activation.LINEAR);
        return layers;
    }
}
