package models;

import backend.Problem;
import utils.Weights;

public class SoftmaxPool implements Model{
    private int layers, imageHeight, imageWidth;
    private int size, stride;

    public SoftmaxPool(int layers, int imageHeight, int imageWidth, int size, int stride) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.layers = layers;
        this.size = size;
        this.stride = stride;
    }

    @Override
    public int neededWeights() {
        return layers;
    }

    @Override
    public int[] compute(Problem p, int[] input) {
        return Weights.reshape(p.softmaxPool(
                Weights.reshape(input, layers, imageHeight, imageWidth),
                scaleConstants, size, stride));
    }

    int[] scaleConstants;

    @Override
    public void setWeights(Problem p, int[] weights) {
        scaleConstants = weights;
    }
}
