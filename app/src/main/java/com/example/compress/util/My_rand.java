package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class My_rand {
    /**
     * %用key对bitmap置乱
     */
    public static double[] my_rand(double[] bitmap, double key1, double key2) {
        int len = bitmap.length;
        double[] bitmap2 = new double[len];
        double[] rand_order = Chaotic.chaotic_maping_order(key1, key2, 16);
        for (int i = 0; i < len; i++) {
            bitmap2[i] = bitmap[(int) rand_order[i]];
        }
        return bitmap2;
    }
}
