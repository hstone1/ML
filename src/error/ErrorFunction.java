package error;

import backend.Problem;

/**
 * Created by henry on 8/20/17.
 */
public interface ErrorFunction {
    public int error(Problem p, int[] real, int[] pred);
}
