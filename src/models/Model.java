package models;

import backend.Problem;

/**
 * Created by henry on 8/14/17.
 */
public abstract class Model {
    public Problem p;

    public abstract int neededWeights();

    protected abstract void _setWeights(Problem p, int[] weights);

    public void setWeights(Problem p, int[] weights){
        this.p = p;
        _setWeights(p, weights);
    }
}
