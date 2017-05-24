package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Matlab {
    public static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int find(double[] array, double num) {
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public static double mean(double[][] input) {
        int height = input.length;
        int width = input[0].length;
        double sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum += input[i][j];
            }
        }
        sum = sum / (double) (height * width);
        return sum;
    }

    public static double[][] multip2(double[][] x) {
        int m = x.length;
        int n = x[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = x[i][j] * x[i][j];
            }
        }
        return x;
    }
}
