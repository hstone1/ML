package examples.recur;

import backend.Problem;
import initializers.Initializers;
import optimizers.Adagrad;
import optimizers.Optimizer;
import recurrent.GRU;
import recurrent.LSTM;

public class TryLstm {
    public static void main(String[] args){
        LSTM lstm = new LSTM(2, 15);
        Optimizer o = new Adagrad(0.005);


        double[] weights = new double[lstm.neededWeights()];
        Initializers.normalSmall.fill(weights);


        for (int i = 0; i < 50000; i++) {
            double[] init = new double[15];

            Problem p = new Problem();

            int[] hiddenInit = p.constant(init);
            int[] wts = p.constant(weights);
            lstm.setWeights(p, wts);

            int loss = p.zero();



            for (int j = 0; j < 1000; j++) {
                double[][] inputs = new double[][]{
                        {Math.random(), Math.random()},
                        {Math.random(), Math.random()},
                        {Math.random(), Math.random()},
                        {Math.random(), Math.random()},
                        {Math.random(), Math.random()},
                };
                double max = inputs[0][0] + inputs[1][1];

                int y = p.constant(max);

                int[] memory = hiddenInit;
                int[] cState = hiddenInit;
                int[][] inp = p.constant(inputs);

                for (int k = 0; k < inputs.length; k++) {
                    int[][] out = lstm.run(inp[k], memory, cState);
                    memory = out[0];
                    cState = out[1];
                }



                loss = p.add(loss, p.square(p.sub(p.mult(memory[0], p.constant(2)), y)));
            }

            p.backprop(loss);

            weights = o.optimize(weights, p.deriv(wts));


            if (i % 100 == 0) {
                // System.out.println(Arrays.toString(p.deriv(wts)));
                System.out.println((i / 100) + ": " + (p.get(loss) / 1000));
            }



        }

    }

    public static double max(double[][] d){
        double m = d[0][0] + d[0][1];
        for (int i = 1; i < d.length; i++){
            if (d[i][0] + d[i][1] > m) {
                 m = d[i][0] + d[i][1];
            }
        }
        return m;
    }
}
