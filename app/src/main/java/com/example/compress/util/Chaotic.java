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

    /**
     * s=sequence;
     * rand_order=zeros(1,n);
     * for i=1:n
     * %循环找最小的，把坐标放到rand_order中，然后把该值设置为100最大，继续循环
     * pos=find(s==min(s));
     * rand_order(i)=pos(1);
     * s(pos(1))=100;
     * end
     *
     * @param a
     * @param b
     * @param n
     * @return
     */
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
