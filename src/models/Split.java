package models;

import backend.Problem;
import utils.Weights;

/**
 * Created by henry on 8/20/17.
 */
public class Split implements Model{
    Model[] models;

    public Split(Model... models){
        this.models = models;
    }

    @Override
    public int neededWeights() {
        int nw = 0;
        for (Model m : models) {
            nw += m.neededWeights();
        }
        return nw;
    }

    @Override
    public int[] compute(Problem p, int[] input) {
        int[][] outs = new int[models.length][];
        int len = 0;
        for (int i = 0; i < models.length; i++) {
            outs[i] = models[i].compute(p, input);
            len += outs[i].length;
        }

        int[] out = new int[len];
        int pos = 0;
        for (int i = 0;i < models.length; i++) {
            System.arraycopy(outs[i], 0, out, pos, outs[i].length);
            pos += outs[i].length;
        }
        return out;
    }

    @Override
    public void setWeights(Problem p, int[] weights) {
        int end = 0;
        for (Model m : models) {
            int start = end;
            end += m.neededWeights();
            m.setWeights(p, Weights.rip1(weights, start, end - start));
        }
    }
}
