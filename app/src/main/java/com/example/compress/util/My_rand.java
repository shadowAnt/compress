package com.example.compress.util;

import java.util.Arrays;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class My_rand {
    /**
     * function bitmap=my_rand(bitmap,key)%用key对bitmap置乱
     * bitmap2=bitmap;
     * [sequence,rand_order]=chaotic_maping(key(1),key(2),16);
     * for i=1:16
     * bitmap2(i)=bitmap(rand_order(i));
     * end
     * bitmap=bitmap2;
     *
     * @param bitmap
     * @param key1
     * @param key2
     * @return
     */
    public static double[] my_rand(double[] bitmap, double key1, double key2) {
        int len = bitmap.length;
        double[] bitmap2 = new double[len];
        double[] rand_order = Chaotic.chaotic_maping_order(key1, key2, 16);
        for (int i = 0; i < len; i++) {
            bitmap2[i] = bitmap[(int)rand_order[i]];
        }
        return bitmap2;
    }
}
