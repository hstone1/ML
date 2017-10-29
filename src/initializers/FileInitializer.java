package initializers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by henry on 8/21/17.
 */
public class FileInitializer implements Initializer{
    Scanner s;
    public FileInitializer(String filename) {
        try {
            s = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double produceWeight() {
        return s.nextDouble();
    }
}
