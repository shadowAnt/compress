package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Embed {
    /**
     * @param a
     * @param b
     * @param bitmap
     * @param code
     * @return
     */
    public static int[] embed(int a, int b, int[] bitmap, int code) {
        if (a == b) {
            if (code == 1) {
                for (int i = 0; i < bitmap.length; i++) {
                    bitmap[i] = 255;
                }
            } else if (code == 0) {
                for (int i = 0; i < bitmap.length; i++) {
                    bitmap[i] = 0;
                }
            }
        }
        if (code == 1) {
            if (a < b) {
                int temp = a;
                a = b;
                b = temp;
            }
        } else if (code == 0) {
            if (a > b) {
                int temp = a;
                a = b;
                b = temp;
            }
        }
        int[] group4 = {a, b, bitmap[0], bitmap[1]};
        return group4;
    }
}
