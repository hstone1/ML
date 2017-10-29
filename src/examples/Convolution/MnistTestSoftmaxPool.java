package examples.Convolution;

import data.MNIST;
import error.ErrorFunction;
import initializers.FileInitializer;
import initializers.Initializers;
import models.*;
import optimizers.Optimizer;
import optimizers.SGD;
import utils.DataPrep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by henry on 8/21/17.
 */
public class MnistTestSoftmaxPool {
    public static final int BATCH_SIZE = 100;
    public static final int NTHREADS = 20;

    public static void main(String[] args) {
        boolean test = false;

        Model m = new LinearModel(
                new Convolution(5, 5, 1,8,28,28, Activation.RELU),
                new SoftmaxPool(8, 24, 24, 2, 2),
                new Convolution(5, 5, 8,10,12,12, Activation.RELU),
                new SoftmaxPool(10, 8, 8, 2, 2),
                new DenseLayer(10 * 4 * 4, 40, Activation.RELU),
                new DenseLayer(40, 10, Activation.SOFTMAX));

//        Model m = new LinearModel(
//                new Convolution(3, 3, 1,5,28,28, Activation.RELU),
//                new Convolution(3, 3, 5,8,26,26, Activation.RELU),
//                new SoftmaxPool(8, 24, 24, 2, 2),
//                new Convolution(3, 3, 8,10,12,12, Activation.RELU),
//                new Convolution(3, 3, 10,10,10,10, Activation.RELU),
//                new SoftmaxPool(10, 8, 8, 2, 2),
//                new DenseLayer(10 * 4 * 4, 40, Activation.RELU),
//                new DenseLayer(40, 10, Activation.SOFTMAX));


//        Model m = new LinearModel(
//                new DenseLayer(28 * 28, 50, Activation.RELU),
//                new DenseLayer(50, 10, Activation.SOFTMAX));

        System.out.println(m.neededWeights());

        Trainer train =
                test ?
                        new Trainer(m, new FileInitializer("data/trainVersion")) :
                        new Trainer(m, Initializers.normalSmall);

        double[][] X = MNIST.trainDataFlat(); assert X != null;
        double[][] Y = MNIST.trainLabels(); assert Y != null;

        double[][] Xtest = MNIST.testDataFlat(); assert X != null;
        double[][] Ytest = MNIST.testLabels(); assert Y != null;



        ExecutorService pool = Executors.newFixedThreadPool(15);

        Optimizer o = new SGD(0.05);


        for (int epoch = 0; epoch < 50; epoch++) {
            DataPrep.shuffle(X, Y);
            double loss = train.trainEpoch(X, Y, o, ErrorFunction.catagoricalCrossEntropy, 100, pool, 20);
            double tacc = testAccuracy(train, pool, Xtest, Ytest);
            System.out.println(loss + "\t" + tacc);
            train.save("data/softmaxPool");

        }

        //train.save("data/trainVersion");
        //train = new Trainer(m, new FileInitializer("data/trainVersion"));

        pool.shutdown();


    }

    public static int argmax(double[] d) {
        int a = 0;
        double max = d[0];
        for (int i = 1; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
                a = i;
            }
        }
        return a;
    }

    public static double testAccuracy(Trainer t, ExecutorService pool, double[][] Xtest, double[][] Ytest) {
        double[][] Xt = MNIST.testDataFlat();
        double[][] Yt = MNIST.testLabels();

        int corr = 0;

        double[][] predY = t.predict(Xt, pool, 500);

        for (int i = 0;i < 10000;i++) {
            int pred = argmax(predY[i]);
            int real = argmax(Yt[i]);
            corr += (pred == real ? 1 : 0);
        }
        return corr / 10000.0;
    }
}
