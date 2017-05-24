package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class My_derand {
    /**
     * function bitmap=my_derand(bitmap,key)%用key对bitmap置乱
     * bitmap2=bitmap;
     * [sequence,rand_order]=chaotic_maping(key(1),key(2),16);
     * for i=1:16
     * bitmap2(rand_order(i))=bitmap(i);
     * end
     * bitmap=bitmap2;
     *
     * @param bitmap
     * @return
     */
    public static double[] my_derand(double[] bitmap, double sequence, double key) {
        double[] bitmap2 = new double[bitmap.length];
        double[] rand_order = Chaotic.chaotic_maping_order(sequence, key, 16);
        for (int i = 0; i < 16; i++) {
            bitmap2[(int)rand_order[i]] = bitmap[i];
        }
        return bitmap2;
    }
}
