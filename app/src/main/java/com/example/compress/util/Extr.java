package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class Extr {
    public static double extr(double a, double b, double[] dec) {
        int code;
        if (a == b) {
            if (dec[0] == 255 && dec[1] == 255) {
                code = 1;
                return code;
            } else if (dec[0] == 0 && dec[1] == 0) {
                code = 0;
                return code;
            }
            return -1;
        }
        if (a > b) {
            code = 1;
            return code;
        } else if (a < b) {
            code = 0;
            return code;
        }
        return -1;
    }
}
