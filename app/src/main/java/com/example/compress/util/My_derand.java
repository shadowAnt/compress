package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class My_derand {
    /**
     * 用key对bitmap置乱
     */
    public static double[] my_derand(double[] bitmap, double sequence, double key) {
        double[] bitmap2 = new double[bitmap.length];
        double[] rand_order = Chaotic.chaotic_maping_order(sequence, key, 16);
        for (int i = 0; i < 16; i++) {
            bitmap2[(int) rand_order[i]] = bitmap[i];
        }
        return bitmap2;
    }
}
