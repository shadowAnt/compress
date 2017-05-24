package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class Decryption {
    /**
     * @param a
     * @param b
     * @param key
     * @return
     */
    public static double[] decrption(double a, double b, double[] dec, double[] key) {
        double[] xor_data = Rand_numbers.Rand_numbers(key, 3, 256);
        a = (int) xor_data[0] ^ (int) a;
        b = (int) xor_data[0] ^ (int) b;
//        dec[0] = (int) xor_data[1] ^ (int) dec[0];
//        dec[1] = (int) xor_data[2] ^ (int) dec[1];
        dec[0] = (int) dec[0];
        dec[1] = (int) dec[1];
        if (a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double[] group4 = {a, b, dec[0], dec[1]};
        return group4;
    }
}
