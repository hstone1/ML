package utils;

import java.util.Random;

public class DataPrep {
    public static <T, K> void shuffle(T[] arr, K[] arr2) {
        Random r = new Random();
        for (int i = 0; i < arr.length; i++) {
            int temp = r.nextInt(arr.length);
            T t = arr[i];
            K k = arr2[i];
            arr[i] = arr[temp];
            arr2[i] = arr2[temp];
            arr[temp] = t;
            arr2[temp] = k;
        }
    }
}
