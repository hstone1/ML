package models;

import backend.Problem;
import utils.Weights;

public class MaxPool extends BasicModel {
    private int layers, imageHeight, imageWidth;
    private int size, stride;

    public MaxPool(int layers, int imageHeight, int imageWidth, int size, int stride) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.layers = layers;
        this.size = size;
        this.stride = stride;
    }

    @Override
    public int neededWeights() {
        return 0;
    }

    @Override
    public int[] compute(int[] input) {
        return Weights.reshape(p.maxPool(
                Weights.reshape(input, layers, imageHeight, imageWidth),
                size, stride));
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        // do nothing, there are no weights
    }
}
