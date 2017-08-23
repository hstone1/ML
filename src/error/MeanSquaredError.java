package error;

import backend.Problem;

/**
 * Created by henry on 8/20/17.
 */
public class MeanSquaredError implements ErrorFunction{
    @Override
    public int error(Problem p, int[] real, int[] pred) {
        int[] indivErr = p.sub(real, pred);
        return p.dot(indivErr, indivErr);
    }
}
