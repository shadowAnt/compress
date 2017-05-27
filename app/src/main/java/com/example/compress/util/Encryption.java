package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Encryption {

    public static double[] encryption(double a, double b, double[] bitmap, double[] key) {
        double[] xor_data = Rand_numbers.Rand_numbers(key, 3, 256);
        a = (int) xor_data[0] ^ (int) Math.round(a);
        b = (int) xor_data[0] ^ (int) Math.round(b);
//        bitmap[0] = (int) xor_data[1] ^ (int) bitmap[0];
//        bitmap[1] = (int) xor_data[2] ^ (int) bitmap[1];
        bitmap[0] = (int) bitmap[0];
        bitmap[1] = (int) bitmap[1];
        double[] group4 = {a, b, bitmap[0], bitmap[1]};
        return group4;
    }
}
