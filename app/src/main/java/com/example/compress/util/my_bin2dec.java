package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class My_bin2dec {
    /**
     * n个二进制数bin转化为一个十进制数
     */
    public static double[] my_bin2dec(double[] bin, int n) {
        int len = bin.length;
        double[] dec = new double[2];
        for (int i = 0; i <= len - n; i += n) {
            int temp = 0;
            for (int j = 0; j < n; j++) {
                temp += bin[j + i] * Math.pow(2, j);
            }
            dec[(i / n)] = temp;
        }
        return dec;
    }
}
