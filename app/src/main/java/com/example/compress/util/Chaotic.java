package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Chaotic {

    public static double[] chaotic_maping_sequence(double a, double b, int n) {
        double[] sequence = new double[n];
        sequence[0] = a;
        for (int i = 1; i < n; i++) {
            sequence[i] = b * sequence[i - 1] * (1 - sequence[i - 1]);
        }
        return sequence;
    }

    public static double[] chaotic_maping_order(double a, double b, int n) {
        double[] sequence = new double[n];
        sequence[0] = a;
        for (int i = 1; i < n; i++) {
            sequence[i] = b * sequence[i - 1] * (1 - sequence[i - 1]);
        }
        double[] rand_order = new double[n];
        for (int i = 0; i < n; i++) {
            int pos = Matlab.find(sequence, Matlab.min(sequence));
            rand_order[i] = pos;
            sequence[pos] = 2147483647;
        }
        return rand_order;
    }
}
