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
    public static int[] decrption(int a, int b, int[] dec, double[] key) {
        double[] xor_data = Rand_numbers.Rand_numbers(key, 3, 256);
        a ^= (int) xor_data[0];
        b ^= (int) xor_data[0];
        dec[0] ^= (int) xor_data[1];
        dec[1] ^= (int) xor_data[2];
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        int[] group4 = {a, b, dec[0], dec[1]};
        return group4;
    }
}
