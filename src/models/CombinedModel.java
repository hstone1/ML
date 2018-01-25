package models;

import backend.Problem;
import utils.Weights;

public class CombinedModel extends Model {
    Model[] models;
    int totalWeights;
    int[] numWeights;

    public void setupSubmodels(Model... models) {
        this.models = models;
        this.numWeights = new int[models.length];
        totalWeights = 0;

        for (int i = 0; i < models.length; i++) {
            int wts = models[i].neededWeights();
            numWeights[i] = wts;
            totalWeights += wts;
        }
    }

    @Override
    public int neededWeights() {
        return totalWeights;
    }

    @Override
    public void _setWeights(Problem p, int[] weights) {
        int tot = 0;

        for (int i = 0; i < models.length; i++) {
            int wts = numWeights[i];
            models[i].setWeights(p, Weights.rip1(weights, tot, wts));
            tot += wts;
        }
    }
}
