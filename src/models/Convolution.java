package models;

import backend.Problem;
import utils.Weights;

public class Convolution extends BasicModel {
    private int inputLayers, outputLayers;
    private int imageHeight, imageWidth;
    private int convolutionHeight, convolutionWidth;
    private Activation activation;

    public Convolution(
            int convolutionHeight, int convolutionWidth,
            int inputLayers, int outputLayers,
            int imageHeight, int imageWidth,
            Activation a) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.convolutionHeight = convolutionHeight;
        this.convolutionWidth = convolutionWidth;
        this.inputLayers = inputLayers;
        this.outputLayers = outputLayers;
        this.activation = a;
    }

    @Override
    public int neededWeights() {
        return (inputLayers * convolutionWidth * convolutionHeight + 1) * outputLayers;
    }

    @Override
    public int[] compute(int[] input) {
        int[][][] image = Weights.reshape(input, inputLayers, imageHeight, imageWidth);
        int[][][] output = p.convolve(image, wts, biases);
        switch (activation){
            case RELU: output = p.relu(output); break;
            case SOFTPLUS: output = p.elementwise(output, (pr, v) -> pr.softplus(v)); break;
            case TANH: output = p.tanh(output); break;
            case LINEAR: break;

        }

        return Weights.reshape(output);
    }


    private int[][][][] wts;
    private int[] biases;

    @Override
    public void _setWeights(Problem p, int[] weights) {
        biases = Weights.rip1(weights, 0, outputLayers);
        wts = Weights.rip4(weights, outputLayers,
                outputLayers, inputLayers, convolutionHeight, convolutionWidth);
    }
}
