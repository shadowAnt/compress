package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class My_dec2bin {
    /**
     * 两个十进制数转化为16位bitmap
     */
    public static double[] my_dec2bin(double[] dec, int n) {
        int len = dec.length;
        double[] bitmap = new double[len * n];
        int index = 0;
        for (int i = 0; i < len; i++) {
            int temp = (int) dec[i];
            for (int j = 0; j < n; j++) {
                bitmap[index] = temp % 2;
                index++;
                temp = temp / 2;
            }
        }
        return bitmap;
    }
}
