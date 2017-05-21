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
    public static int[] my_rand(int[] bitmap, double key1, double key2, int j) {
        int len = bitmap.length;
        int[] bitmap2 = new int[len];
        int[] rand_order = Chaotic.chaotic_maping_order(key1, key2, 16);
        for (int i = 0; i < 16; i++) {
            bitmap2[i] = bitmap[rand_order[i]];
        }
        return bitmap2;
    }
}
