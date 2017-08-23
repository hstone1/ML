package examples.error_pred;

import backend.Problem;
import error.ErrorFunction;

/**
 * Created by henry on 8/20/17.
 */
public class DeviationError implements ErrorFunction {
    @Override
    public int error(Problem p, int[] real, int[] pred) {
        int error = p.div(p.square(p.sub(real[0], pred[0])), pred[1]);
        return p.add(error, pred[1]);
    }
}
