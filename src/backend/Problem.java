package backend;

import java.util.Arrays;

/**
 * Created by henry on 8/13/17.
 */
public class Problem {
    private static final int CAP = 100;


    private static final byte NOTHING = 0;
    private static final byte ADD = 1;
    private static final byte MULT = 2;
    private static final byte SUM = 3;
    private static final byte PROD = 4;
    private static final byte TANH = 5;
    private static final byte DOT = 6;
    private static final byte SUB = 7;
    private static final byte EXP = 8;
    private static final byte DIV = 9;
    private static final byte DOT_ADD = 10;
    private static final byte SOFTPLUS = 11;
    private static final byte SQUARE = 12;
    private static final byte SQUARE_VEC = 13;
    private static final byte SQRT = 14;
    private static final byte INVERT = 15;
    private static final byte LN = 16;

    public Problem() {
        vals = new double[CAP];
        operations = new byte[CAP];
        operationElements = new int[CAP][];
    }

    public Problem(int c) {
        vals = new double[c];
        operations = new byte[c];
        operationElements = new int[c][];
        cap = c;
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

    public int sum(int[][] arr) {
        if (next == cap) { expand(); }

        int l = arr.length * arr[0].length;
        int[] ops = new int[l];
        double s = 0;
        int i = 0;
        for(int x = 0; x < arr.length; x++) {
            for(int y = 0; y < arr[0].length; y++) {
                int a = arr[x][y];
                ops[i++] = a;
                s += vals[a];
            }
        }
        vals[next] = s;
        operations[next] = SUM;
        operationElements[next] = ops;
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

    public int sqrt(int a) {
        if (next == cap) { expand(); }
        vals[next] = Math.sqrt(vals[a]);
        operations[next] = SQRT;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int ln(int a) {
        if (next == cap) { expand(); }
        vals[next] = Math.log(vals[a]);
        operations[next] = LN;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int softplus(int a) {
        if (next == cap) { expand(); }
        vals[next] = Math.log(1 + HMath.exp(vals[a]));
        operations[next] = SOFTPLUS;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int square(int a) {
        if (next == cap) { expand(); }
        vals[next] = vals[a] * vals[a];
        operations[next] = SQUARE;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int square(int[] a) {
        if (next == cap) { expand(); }
        double s = 0;
        for (int i = 0; i < a.length; i++) {
            s += vals[a[i]] * vals[a[i]];
        }
        vals[next] = s;
        operations[next] = SQUARE_VEC;
        operationElements[next] = Arrays.copyOf(a, a.length);
        return next++;
    }

    public int[] softplus(int[] a) {
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = softplus(a[i]);
        }
        return out;
    }

    public int exp(int a) {
        if (next == cap) { expand(); }
        vals[next] = Math.exp(vals[a]);
        operations[next] = EXP;
        operationElements[next] = new int[]{a};
        return next++;
    }

    public int invert(int a) {
        if (next == cap) { expand(); }
        vals[next] = 1 / vals[a];
        operations[next] = INVERT;
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

    public int dotAdd(int[] a, int[] b, int c) {
        assert a.length == b.length;
        if (next == cap) { expand(); }
        int[] ops = new int[a.length * 2 + 1];

        double s = vals[c];
        ops[0] = c;

        for (int i = 0; i < a.length; i++) {
            int ind = 2 * i + 1;
            ops[ind] = a[i];
            ops[ind + 1] = b[i];
            s += vals[a[i]] * vals[b[i]];
        }
        vals[next] = s;
        operations[next] = DOT_ADD;
        operationElements[next] = ops;
        return next++;
    }

    public void backprop(int a) {
        derivs = new double[next];
        derivs[a] = 1;
        while (next-- > 0) {
            int op = operations[next];
            if (op != NOTHING) {
                double d = derivs[next];
                if (d != 0) {
                    int[] ops = operationElements[next];
                    switch (operations[next]) {
                        case ADD:
                            derivs[ops[0]] += d;
                            derivs[ops[1]] += d;
                            break;
                        case MULT:
                            derivs[ops[0]] += d * vals[ops[1]];
                            derivs[ops[1]] += d * vals[ops[0]];
                            break;
                        case DIV:
                            derivs[ops[0]] += d / vals[ops[1]];
                            derivs[ops[1]] -= d * vals[ops[0]] / vals[ops[1]] / vals[ops[1]];
                            break;
                        case SUM:
                            for (int i = 0; i < ops.length; i++) {
                                derivs[ops[i]] += d;
                            }
                            break;
                        case PROD:
                            for (int i = 0; i < ops.length; i++) {
                                derivs[ops[i]] += d * vals[next] / vals[ops[i]];
                            }
                            break;
                        case TANH:
                            derivs[ops[0]] += d * (1 - vals[next] * vals[next]);
                            break;
                        case EXP:
                            derivs[ops[0]] += d * vals[next];
                            break;
                        case DOT:
                            for (int i = 0; i < ops.length; i += 2) {
                                derivs[ops[i]] += d * vals[ops[i + 1]];
                                derivs[ops[i + 1]] += d * vals[ops[i]];
                            }
                            break;
                        case DOT_ADD:
                            derivs[ops[0]] += d;
                            for (int i = 1; i < ops.length; i += 2) {
                                derivs[ops[i]] += d * vals[ops[i + 1]];
                                derivs[ops[i + 1]] += d * vals[ops[i]];
                            }
                            break;
                        case SUB:
                            derivs[ops[0]] += d;
                            derivs[ops[1]] -= d;
                            break;
                        case SOFTPLUS:
                            derivs[ops[0]] += d * HMath.sigmoid(vals[ops[0]]);
                            break;
                        case SQUARE:
                            derivs[ops[0]] += 2 * d * vals[ops[0]];
                            break;
                        case SQUARE_VEC:
                            for (int i = 0; i < ops.length; i++) {
                                derivs[ops[i]] += 2 * d * vals[ops[i]];
                            }
                            break;
                        case SQRT:
                            derivs[ops[0]] += 0.5 * d / vals[next];
                            break;
                        case INVERT:
                            derivs[ops[0]] -= d * vals[next] * vals[next];
                            break;
                        case LN:
                            derivs[ops[0]] = d / vals[ops[0]];
                            break;
                    }
                }
            }
        }
    }












    public int average(int[] in) {
        return mult(sum(in), constant(1.0 / in.length));
    }

    public int sd(int[] in) {
        int average = average(in);
        int[] varCont = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            varCont[i] = sub(average, in[i]);
        }
        int variance = square(varCont);
        return mult(variance, constant(1.0 / in.length));
    }

    public int[] normalize(int[] in) {
        int norm = constant(1.0 / in.length);
        int average = mult(sum(in), norm);
        int[] varCont = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            varCont[i] = sub(average, in[i]);
        }
        int variance = square(varCont);
        int sd = sqrt(mult(variance, norm));
        int factor = invert(sd);

        int[] out = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = mult(sub(in[i], average), factor);
        }

        return out;
    }


    public int[][][] convolve(int[][] im, int[][][] wts, int[] bias) {
        int[][][] out = new int[wts.length][][];
        for (int i = 0;i < out.length;i++) {
            out[i] = convolve(im, wts[i], bias[i]);
        }
        return out;
    }

    public int[][][] convolve(int[][][] im, int[][][][] wts, int[] bias) {
        int[][][] out = new int[wts.length][][];
        for (int i = 0;i < out.length;i++) {
            out[i] = convolve(im, wts[i], bias[i]);
        }
        return out;
    }

    public int[][] convolve(int[][][] im, int[][][] wts, int bias) {
        int[][] out = new int[im[0].length + 1 - wts[0].length][im[0][0].length + 1 - wts[0][0].length];

        int l1 = out.length, l2 = out[0].length;
        int wl1 = wts.length, wl2 = wts[0].length, wl3 = wts[0][0].length;

        int dotSize = wl1 * wl2 * wl3;

        while (next + l1 * l2 >= cap) {
            expand();
        }

        double bVal = vals[bias];

        for (int x = 0; x < l1; x++) {
            for (int y = 0; y < l2; y++) {
                int[] ops = new int[dotSize * 2 + 1];

                double v = bVal;
                ops[0] = bias;
                int i = 1;
                for (int l = 0; l < wl1; l++) {
                    int[][] weightLayer = wts[l];
                    int[][] imageLayer = im[l];
                    for (int ix = 0; ix < wl2; ix++) {
                        int[] weightRow = weightLayer[ix];
                        int[] imageRow = imageLayer[ix + x];
                        for (int iy = 0; iy < wl3; iy++) {
                            int wt = weightRow[iy];
                            int img = imageRow[iy + y];

                            v += vals[wt] * vals[img];
                            ops[i++] = wt;
                            ops[i++] = img;
                        }
                    }
                }

                vals[next] = v;
                operations[next] = DOT_ADD;
                operationElements[next] = ops;
                out[x][y] = next++;
            }
        }

        return out;
    }



    public int[][] convolve(int[][] im, int[][] wts, int bias) {
        int[][] out = new int[im.length + 1 - wts.length][im[0].length + 1 - wts[0].length];

        int l1 = out.length, l2 = out[0].length;
        int wl1 = wts.length, wl2 = wts[0].length;

        int dotSize = wl1 * wl2;

        while (next + l1 * l2 >= cap) {
            expand();
        }

        double bVal = vals[bias];

        for (int x = 0; x < l1; x++) {
            for (int y = 0; y < l2; y++) {
                int[] ops = new int[dotSize * 2 + 1];

                double v = bVal;
                ops[0] = bias;
                int i = 1;
                for (int ix = 0; ix < wl1; ix++) {
                    int[] weightsRow = wts[ix];
                    int[] imageRow = im[ix + x];
                    for (int iy = 0; iy < wl2; iy++) {
                        int wt = weightsRow[iy];
                        int img = imageRow[iy + y];

                        v += vals[wt] * vals[img];
                        ops[i++] = wt;
                        ops[i++] = img;
                    }
                }

                vals[next] = v;
                operations[next] = DOT_ADD;
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

    public int[] multBias(int[][] mat, int[] vec, int[] bias) {
        int[] out = new int[mat.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = dotAdd(mat[i], vec, bias[i]);
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

    public int[][] tanh(int[][] a) {
        int[][] out = new int[a.length][];
        for (int i = 0; i < out.length; i++) {
            out[i] = tanh(a[i]);
        }
        return out;
    }

    public int[][][] tanh(int[][][] a) {
        int[][][] out = new int[a.length][][];
        for (int i = 0; i < out.length; i++) {
            out[i] = tanh(a[i]);
        }
        return out;
    }


    public int[] relu(int[] a) {
        int l1 = a.length;
        int[] out = new int[l1];

        int z = constant(0);
        for (int i1 = 0; i1 < l1; i1++) {
                out[i1] = vals[a[i1]] > 0 ? a[i1] : z;
        }
        return out;
    }

    public int[][] relu(int[][] a) {
        int l1 = a.length, l2 = a[0].length;
        int[][] out = new int[l1][l2];

        int z = constant(0);
        for (int i1 = 0; i1 < l1; i1++) {
            for (int i2 = 0; i2 < l2; i2++) {
                out[i1][i2] = vals[a[i1][i2]] > 0 ? a[i1][i2] : z;
            }
        }
        return out;
    }

    public int[][][] relu(int[][][] a) {
        int l1 = a.length, l2 = a[0].length, l3 = a[0][0].length;
        int[][][] out = new int[l1][l2][l3];

        int z = constant(0);
        for (int i1 = 0; i1 < l1; i1++) {
            for (int i2 = 0; i2 < l2; i2++) {
                for (int i3 = 0; i3 < l3; i3++) {
                    out[i1][i2][i3] = vals[a[i1][i2][i3]] > 0 ? a[i1][i2][i3] : z;
                }
            }
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

    public int[][][] constant(double[][][] arr) {
        int l = arr.length;
        int[][][] a = new int[l][][];
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

    // TODO improve robustness
    public int[][] maxPool(int[][] image) {
        assert image.length % 2 == 0;
        assert image[0].length % 2 == 0;

        int[][] out = new int[image.length / 2][image[0].length / 2];
        int l = out[0].length;
        for (int x = 0; x < out.length; x++) {
            for (int y = 0; y < out.length; y++) {
                int a = image[2 * x][2 * y];
                double max = vals[image[2 * x][2 * y]];

                if (vals[image[2 * x + 1][2 * y]] > max) {
                    max = vals[image[2 * x + 1][2 * y]];
                    a = image[2 * x + 1][2 * y];
                }

                if (vals[image[2 * x][2 * y + 1]] > max) {
                    max = vals[image[2 * x][2 * y + 1]];
                    a = image[2 * x][2 * y + 1];
                }

                if (vals[image[2 * x + 1][2 * y + 1]] > max) {
                    a = image[2 * x + 1][2 * y + 1];
                }

                out[x][y] = a;
            }
        }

        return out;
    }

    public int[][][] maxPool(int[][][] image) {
        int[][][] im = new int[image.length][][];
        for (int i = 0; i < im.length; i++) {
            im[i] = maxPool(image[i]);
        }
        return im;
    }

    public int[] flatten(int[][] arr) {
        int l1 = arr.length;
        int l2 = arr[0].length;

        int[] out = new int[l1 * l2];

        int i = 0;
        for (int i1 = 0; i1 < l1; i1++) {
            for (int i2 = 0; i2 < l2; i2++) {
                out[i++] = arr[i1][i2];
            }
        }

        return out;
    }

    public int[] flatten(int[][][] arr) {
        int l1 = arr.length;
        int l2 = arr[0].length;
        int l3 = arr[0][0].length;

        int[] out = new int[l1 * l2 * l3];

        int i = 0;
        for (int i1 = 0; i1 < l1; i1++) {
            for (int i2 = 0; i2 < l2; i2++) {
                for (int i3 = 0; i3 < l3; i3++) {
                    out[i++] = arr[i1][i2][i3];
                }
            }
        }

        return out;
    }
}
