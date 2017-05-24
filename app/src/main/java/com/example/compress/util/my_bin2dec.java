package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class My_bin2dec {
    /**
     * function dec=my_bin2dec(bin, n) %n个二进制数bin转化为一个十进制数
     * len=numel(bin);
     * for i=1:n:len-n+1 %每次处理n个二进制数
     * temp=0;
     * for j=i:i+n-1
     * temp=temp+2^mod(j-1,n)*bin(j);
     * end
     * dec(ceil(i/n))=temp;
     * end
     * [0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0]
     * 8
     *
     * @return
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
