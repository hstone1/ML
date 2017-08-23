package data;

import java.io.*;

/**
 * Created by henry on 8/21/17.
 */
public class MNIST {
    public static double[][][] trainData() {
        try {
            FileInputStream fr = new FileInputStream("data/train-images.idx3-ubyte");
            fr.read(new byte[16]);
            double[][][] data = new double[60000][28][28];
            for (int i = 0; i < 60000; i++) {
                byte[] r = new byte[28 * 28];
                fr.read(r);
                for (int j = 0; j < 28 * 28; j++) {
                    data[i][j / 28][j % 28] = ((r[j] & 0xff) / 255.0);
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[][] trainLabels() {
        try {
            FileInputStream fr = new FileInputStream("data/train-labels.idx1-ubyte");
            fr.read(new byte[8]);
            double[][] data = new double[60000][10];
            for (int i = 0; i < 60000; i++) {
                switch (fr.read()) {
                    case 0: data[i] = new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 1: data[i] = new double[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 2: data[i] = new double[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 3: data[i] = new double[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0}; break;
                    case 4: data[i] = new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0}; break;
                    case 5: data[i] = new double[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0}; break;
                    case 6: data[i] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0}; break;
                    case 7: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0}; break;
                    case 8: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}; break;
                    case 9: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1}; break;
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[][] testLabels() {
        try {
            FileInputStream fr = new FileInputStream("data/t10k-labels.idx1-ubyte");
            fr.read(new byte[8]);
            double[][] data = new double[10000][10];
            for (int i = 0; i < 10000; i++) {
                switch (fr.read()) {
                    case 0: data[i] = new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 1: data[i] = new double[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 2: data[i] = new double[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0}; break;
                    case 3: data[i] = new double[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0}; break;
                    case 4: data[i] = new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0}; break;
                    case 5: data[i] = new double[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0}; break;
                    case 6: data[i] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0}; break;
                    case 7: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0}; break;
                    case 8: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}; break;
                    case 9: data[i] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1}; break;
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[][][] testData() {
        try {
            FileInputStream fr = new FileInputStream("data/t10k-images.idx3-ubyte");
            fr.read(new byte[16]);
            double[][][] data = new double[10000][28][28];
            for (int i = 0; i < 10000; i++) {
                byte[] r = new byte[28 * 28];
                fr.read(r);
                for (int j = 0; j < 28 * 28; j++) {
                    data[i][j / 28][j % 28] = ((r[j] & 0xff) / 255.0);
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
