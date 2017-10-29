package rl;

import backend.Problem;
import models.Model;

public class RLHelpers {
    public static int TDError(Problem p, Model m, int[] state, int actionIndex,int[] afterstate, int reward) {
        int predicted = m.compute(p, state)[actionIndex];
        int resulting = p.max(m.compute(p, afterstate));
        return p.sub(p.add(reward, resulting), predicted);
    }

    public static int TDError(Problem p, Model m, double[] state, int actionIndex, double[] afterstate, double reward) {
        return TDError(p, m, p.constant(state), actionIndex, p.constant(afterstate), p.constant(reward));
    }
}
