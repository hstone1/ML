package utils;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.HashMap;

/**
 * Created by henry on 8/22/17.
 */
public class WeightManager {
    int totalSize = 0;
    Object[] arrays = new Object[1];
    int max = 1;
    int s = 0;

    public int getTotalSize(){
        return totalSize;
    }

    public void expand(){
        Object[] narr = new Object[max * 2];
        System.arraycopy(arrays, 0, narr, 0, max);
        arrays = narr;
        max *= 2;
    }

    public WeightManager add(int... size) {
        if (s == max) { expand(); }

        totalSize += prod(size);
        if (size.length == 1) {
            int[] arr = new int[size[0]];
            for (int i = 0; i < size[0]; i++) {
                arr[i] = totalSize++;
            }
            arrays[s++] = arr;
        } else if (size.length == 2) {
            int[][] arr = new int[size[0]][size[1]];
            for (int i1 = 0; i1 < size[0]; i1++) {
                for (int i2 = 0; i2 < size[1]; i2++) {
                    arr[i1][i2] = totalSize++;
                }
            }
            arrays[s++] = arr;
        } else if (size.length == 3) {
            int[][][] arr = new int[size[0]][size[1]][size[2]];
            for (int i1 = 0; i1 < size[0]; i1++) {
                for (int i2 = 0; i2 < size[1]; i2++) {
                    for (int i3 = 0; i3 < size[2]; i3++) {
                        arr[i1][i2][i3] = totalSize++;
                    }
                }
            }
            arrays[s++] = arr;
        } else if (size.length == 4) {
            int[][][][] arr = new int[size[0]][size[1]][size[2]][size[3]];
            for (int i1 = 0; i1 < size[0]; i1++) {
                for (int i2 = 0; i2 < size[1]; i2++) {
                    for (int i3 = 0; i3 < size[2]; i3++) {
                        for (int i4 = 0; i4 < size[3]; i4++) {
                            arr[i1][i2][i3][i4] = totalSize++;
                        }
                    }
                }
            }
            arrays[s++] = arr;
        }
        return this;
    }

    public int prod(int[] a) {
        int p = a[0];
        for (int i = 1; i < a.length; i++) {
            p *= a[i];
        }
        return p;
    }

    public Object get(int i) {
        return arrays[i];
    }

    public int[] get1(int i) {
        return (int[]) arrays[i];
    }

    public int[][] get2(int i) {
        return (int[][]) arrays[i];
    }

    public int[][][] get3(int i) {
        return (int[][][]) arrays[i];
    }

    public int[][][][] get4(int i) {
        return (int[][][][]) arrays[i];
    }
}
