package initializers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by henry on 8/21/17.
 */
public class NumpyF8Initializer implements Initializer{
    BufferedInputStream reader;
    ByteBuffer bb = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);

    public NumpyF8Initializer(String filename) {
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
            byte[] data = new byte[8];
            reader.read(data);
            bb.put(data);
            double a = bb.getDouble(0);
            bb.position(0);
            return a;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
