package rl;

import backend.Problem;
import models.BasicModel;

public class RLHelpers {
    public static int TDError(Problem p, BasicModel m, int[] state, int actionIndex, int[] afterstate, int reward) {
        int predicted = m.compute(state)[actionIndex];
        int resulting = p.max(m.compute(afterstate));
        return p.sub(p.add(reward, resulting), predicted);
    }

    public static int TDError(Problem p, BasicModel m, double[] state, int actionIndex, double[] afterstate, double reward) {
        return TDError(p, m, p.constant(state), actionIndex, p.constant(afterstate), p.constant(reward));
    }
}
