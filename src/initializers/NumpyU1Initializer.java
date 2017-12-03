package initializers;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

/**
 * Created by henry on 8/21/17.
 */
public class NumpyU1Initializer implements Initializer{
    BufferedInputStream reader;
    ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

    public NumpyU1Initializer(String filename) {
        try {
            reader = new BufferedInputStream(new FileInputStream(new File(filename)));
            reader.read(new byte[8]);
            byte[] hlen = new byte[2];
            reader.read(hlen);
            short s = ByteBuffer.allocate(2).put(hlen).order(ByteOrder.LITTLE_ENDIAN).getShort(0);
            reader.read(new byte[s]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double produceWeight() {
        try {
            byte[] data = new byte[1];
            reader.read(data);
            bb.put(data);
            int a = bb.getInt(0);
            bb.position(0);
            return a / 255.0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
