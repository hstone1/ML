package backend;

import java.util.Arrays;

/**
 * Created by henry on 8/13/17.
 */
public class Problem {
    private static final int CAP = 100;

    private final byte NOTHING = 0;
    private final byte ADD = 1;
    private final byte MULT = 2;
    private final byte SUM = 3;
    private final byte PROD = 4;
    private final byte TANH = 5;
    private final byte DOT = 6;
    private final byte SUB = 7;
    private final byte EXP = 8;
    private final byte DIV = 9;

    public Problem() {
        vals = new double[CAP];
        operations = new byte[CAP];
        operationElements = new int[CAP][];
    }

    public void expand () {
        cap *= 2;
        double[] vals = new double[cap];
        byte[] operations = new byte[cap];
        int[][] operationElements = new int[cap][];
        System.arraycopy(this.vals, 0, vals, 0, this.vals.length);
        System.arraycopy(this.operations, 0, operations, 0, this.operations.length);
        System.arraycopy(this.operationElements, 0, operationElements, 0, this.operationElements.length);
        this.vals = vals;
        this.operations = operations;
        this.operationElements = operationElements;
    }

    private double[] vals;
    private double[] derivs;
    private byte[] operations;
    private int[][] operationElements;

    private int next = 0;
    private int cap = CAP;

    public int add(int a, int b) {
        if (next == cap) { expand(); }
        vals[next] = vals[a] + vals[b];
        operations[next] = ADD;
        operationElements[next] = new int[]{a, b};
        return next++;
    }

    public int sub(int a, int b) {
        if (next == cap) { expand(); }
        vals[next] = vals[a] - vals[b];
        operations[next] = SUB;
        operationElements[next] = new int[]{a, b};
        return next++;
    }

    public int constant(double d) {
        if (next == cap) { expand(); }
        vals[next] = d;
        operations[next] = NOTHING;
        return next++;
    }

    public int mult(int a, int b) {
        if (next == cap) { expand(); }
        vals[next] = vals[a] * vals[b];
        operations[next] = MULT;
        operationElements[next] = new int[]{a, b};
        return next++;
    }

    public int div(int a, int b) {
        if (next == cap) { expand(); }
        vals[next] = vals[a] /vals[b];
        operations[next] = DIV;
        operationElements[next] = new int[]{a, b};
        return next++;
    }

    public int sum(int... arr) {
        if (next == cap) { expand(); }
        double s = vals[arr[0]];
        for (int i = 1; i < arr.length; i++) {
            s += vals[arr[i]];
        }
        vals[next] = s;
        operations[next] = SUM;
        operationElements[next] = Arrays.copyOf(arr, arr.length);
        return next++;
    }

    public int prod(int... arr) {
        if (next == cap) { expand(); }
        double s = vals[arr[0]];
        for (int i = 1; i < arr.length; i++) {
            s *= vals[arr[i]];
        }
        vals[next] = s;
        operations[next] = PROD;
        operationElements[next] = Arrays.copyOf(arr, arr.length);
        return next++;
    }

    public int tanh(int a) {
        if (next == cap) { expand(); }
        vals[next] = HMath.tanh(vals[a]);
        operations[next] = TANH;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int exp(int a) {
        if (next == cap) { expand(); }
        vals[next] = Math.exp(vals[a]);
        operations[next] = EXP;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int dot(int[] a, int[] b) {
        assert a.length == b.length;
        if (next == cap) { expand(); }
        int[] ops = new int[a.length * 2];

        double s = vals[a[0]] * vals[b[0]];
        ops[0] = a[0];
        ops[1] = b[0];

        for (int i = 1; i < a.length; i++) {
            int ind = 2 * i;
            ops[ind] = a[i];
            ops[ind + 1] = b[i];
            s += vals[a[i]] * vals[b[i]];
        }
        vals[next] = s;
        operations[next] = DOT;
        operationElements[next] = ops;
        return next++;
    }

    public void backprop(int a) {
        derivs = new double[next];
        derivs[a] = 1;
        while (next-- > 0) {
            int op = operations[next];
            if (op != NOTHING) {
                int[] ops = operationElements[next];
                switch (operations[next]) {
                    case ADD:
                        derivs[ops[0]] += derivs[next];
                        derivs[ops[1]] += derivs[next];
                        break;
                    case MULT:
                        derivs[ops[0]] += derivs[next] * vals[ops[1]];
                        derivs[ops[1]] += derivs[next] * vals[ops[0]];
                        break;
                    case DIV:
                        derivs[ops[0]] += derivs[next] / vals[ops[1]];
                        derivs[ops[1]] -= derivs[next] * vals[ops[0]] / vals[ops[1]] / vals[ops[1]];
                        break;
                    case SUM:
                        for (int i = 0; i < ops.length; i++) {
                            derivs[ops[i]] += derivs[next];
                        }
                        break;
                    case PROD:
                        for (int i = 0; i < ops.length; i++) {
                            derivs[ops[i]] += derivs[next] * vals[next] / vals[ops[i]];
                        }
                        break;
                    case TANH:
                        derivs[ops[0]] += derivs[next] * (1 - vals[next] * vals[next]);
                        break;
                    case EXP:
                        derivs[ops[0]] += derivs[next] * vals[next];
                        break;
                    case DOT:
                        for (int i = 0; i < ops.length; i+=2) {
                            derivs[ops[i]] += derivs[next] * vals[ops[i + 1]];
                            derivs[ops[i + 1]] += derivs[next] * vals[ops[i]];
                        }
                        break;
                    case SUB:
                        derivs[ops[0]] += derivs[next];
                        derivs[ops[1]] -= derivs[next];
                        break;

                }
            }
        }
    }





















    public int[][] convolve(int[][] im, int[][] wts) {
        int[][] out = new int[im.length + 1 - wts.length][im[0].length + 1 - wts[0].length];

        int l1 = out.length, l2 = out[0].length;
        int wl1 = wts.length, wl2 = wts[0].length;

        int dotSize = wl1 * wl2;

        while (next + l1 * l2 >= cap) {
            expand();
        }


        for (int x = 0; x < l1; x++) {
            for (int y = 0; y < l2; y++) {
                int[] ops = new int[dotSize * 2];

                double v = 0;
                int i = 0;
                for (int ix = 0; ix < wl1; ix++) {
                    for (int iy = 0; iy < wl2; iy++) {
                        int wt = wts[ix][iy];
                        int img = im[ix + x][iy + y];

                        v += vals[wt] * vals[img];
                        ops[i++] = wt;
                        ops[i++] = img;
                    }
                }

                vals[next] = v;
                operations[next] = DOT;
                operationElements[next] = ops;
                out[x][y] = next++;
            }
        }

        return out;
    }


    public int[] div(int[] a, int b) {
        int[] out = new int[a.length];
        for (int i = 0;i < out.length;i++) {
            out[i] = div(a[i], b);
        }
        return out;
    }

    public int[] softmax(int[] a) {
        int[] ex = exp(a);
        return div(ex, sum(ex));
    }

    public int[] mult(int[][] mat, int[] vec) {
        int[] out = new int[mat.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = dot(mat[i], vec);
        }
        return out;
    }

    public int[] add(int[] a, int[] b) {
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = add(a[i], b[i]);
        }
        return out;
    }

    public int[] sub(int[] a, int[] b) {
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = sub(a[i], b[i]);
        }
        return out;
    }

    public int[] tanh(int[] a) {
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = tanh(a[i]);
        }
        return out;
    }

    public int[] exp(int[] a) {
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = exp(a[i]);
        }
        return out;
    }


    public int[] constant(double[] arr) {
        int l = arr.length;
        int[] a = new int[l];
        for (int i = 0; i < l; i++) {
            a[i] = constant(arr[i]);
        }
        return a;
    }

    public int[][] constant(double[][] arr) {
        int l = arr.length;
        int[][] a = new int[l][];
        for (int i = 0; i < l; i++) {
            a[i] = constant(arr[i]);
        }
        return a;
    }

    public double get(int a) {
        return vals[a];
    }


    public double[] get(int[] a) {
        double[] out = new double[a.length];
        for(int i = 0;i < out.length;i++) {
            out[i] = get(a[i]);
        }
        return out;
    }


    public double[][] get(int[][] a) {
        double[][] out = new double[a.length][];
        for(int i = 0;i < out.length;i++) {
            out[i] = get(a[i]);
        }
        return out;
    }


    public double deriv(int a) {
        return derivs[a];
    }


    public double[] deriv(int[] a) {
        double[] out = new double[a.length];
        for(int i = 0;i < out.length;i++) {
            out[i] = deriv(a[i]);
        }
        return out;
    }


    public double[][] deriv(int[][] a) {
        double[][] out = new double[a.length][];
        for(int i = 0;i < out.length;i++) {
            out[i] = deriv(a[i]);
        }
        return out;
    }
}
