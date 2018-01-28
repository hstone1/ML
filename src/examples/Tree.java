package examples;

import backend.Problem;
import initializers.Initializers;
import models.CombinedModel;
import optimizers.Adagrad;
import optimizers.Optimizer;
import recurrent.GRU;

import java.util.Arrays;

public class Tree {
    public static int hiddenSize = 60;

    public static void main(String[] args) {
        CombinedModel model = new CombinedModel();
        Optimizer o = new Adagrad(0.08);


        GRU encoder = new GRU(T.options, hiddenSize);
        Cell cell = new Cell(hiddenSize, T.options);

        model.setupSubmodels(encoder, cell);


        double[] weights = new double[model.neededWeights()];
        Initializers.normalSmall.fill(weights);


        for (int epoch = 1; epoch <= 30000; epoch++) {
            Problem p = new Problem();

            int[] wts = p.constant(weights);
            model.setWeights(p, wts);

            int[] losses = new int[100];

            for (int datum = 0; datum < 100; datum++) {
                T t = new T((int) (6 * Math.random() + 9));
                int[] treeData = t.encode(p, encoder);
                losses[datum] = t.decode(p, cell, treeData, t.valTree);

            }

            int loss = p.average(losses);
            p.backprop(loss);
            weights = o.optimize(weights, p.deriv(wts));

            if (epoch % 100 == 0) {

                System.out.println("Batch " + epoch + ": Loss: " + p.get(loss));


                p = new Problem();
                wts = p.constant(weights);
                model.setWeights(p, wts);
                T t = new T((int) (6 * Math.random() + 9));
                int[] treeData = t.encode(p, encoder);
                System.out.println(t.valTree);
                System.out.println(t.decode(p, cell, treeData, 0));

            }
        }


    }
}


class T{
    public static int options = 6;

    int[] vals;
    Tr valTree;

    public T(int len) {
        vals = new int[len];

        for (int i = 0; i < len; i++) {
            vals[i] = (int) (options * Math.random());
        }

        valTree = new Tr(vals);
    }

    public int[] encode(Problem p, GRU gru) {
        int[] hidden = p.zeros(gru.hiddenSize);

        for (int i = 0;i < vals.length;i++) {
            hidden = gru.run(p.onehot(options, vals[i]), hidden);
        }

        return hidden;
    }

    public int decode(Problem p, Cell c, int[] state, Tr tr) {
        int[] charDist = c.charDist(state);
        int loss =  p.sub(p.zero(), p.ln(charDist[tr.val]));

        if (tr.left != null) {
            loss = p.sub(loss, p.ln(c.shouldSplit(state)));
            int[][] out = c.split(state);
            state = out[0];
            loss = p.add(loss, decode(p, c, out[1], tr.left));
        }

        if (tr.right != null) {
            loss = p.sub(loss, p.ln(c.shouldSplit(state)));
            int[][] out = c.split(state);
            state = out[0];
            loss = p.add(loss, decode(p, c, out[1], tr.right));
        }

        loss = p.sub(loss, p.ln(p.sub(p.one(), c.shouldSplit(state))));

        return loss;
    }

    public String decode(Problem p, Cell c, int[] state, int depth) {
        int[] charDist = c.charDist(state);


        int ch = p.argmax(charDist);
        String outS = "(" + ch;


        if (depth >= 5) {
            return outS + "To Depth)";
        }

        if (p.get(c.shouldSplit(state)) > 0.5) {
            int[][] out = c.split(state);
            state = out[0];
            outS += " " + decode(p, c, out[1], depth + 1);
        } else {
            outS += " null";
        }

        if (p.get(c.shouldSplit(state)) > 0.5) {
            int[][] out = c.split(state);
            state = out[0];
            outS += " " + decode(p, c, out[1], depth + 1);
        } else {
            outS += " null";
        }

        //if (p.get(c.shouldSplit(state)) > 0.5) {
            //outS += " FFF";
        //}

        return outS + ")";
    }




}

class Tr{
    int val;
    Tr left = null;
    Tr right = null;

    public Tr(int[] vals) {
        this.val = vals[0];


        if (vals.length == 2) {
            left = new Tr(Arrays.copyOfRange(vals, 1, 2));
            right = null;
        } else if (vals.length > 2) {
            left = new Tr(Arrays.copyOfRange(vals, 1, (vals.length + 2) / 2));
            right = new Tr(Arrays.copyOfRange(vals, (vals.length + 2) / 2, vals.length));
        }
    }

    @Override
    public String toString() {
        return "(" + val + " " + left + " " + right + ")";
    }
}