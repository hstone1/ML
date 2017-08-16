package models;

import backend.Problem;

/**
 * Created by henry on 8/14/17.
 */
public interface Model {
    int neededWeights();
    int[] compute(Problem p, int[] input);
    void setWeights(int[] weights);
}
