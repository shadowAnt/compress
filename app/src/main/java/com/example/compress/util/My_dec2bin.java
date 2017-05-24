package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class My_dec2bin {
    /**
     * function bitmap=my_dec2bin(dec,x)
     * len=numel(dec);
     * bitmap=zeros(len*x,1);
     * index=1;
     * for i=1:len
     * temp=dec(i);
     * for j=1:x
     * bitmap(index)=mod(temp,2);
     * index=index+1;
     * temp=floor(temp/2);
     * end
     * end
     *
     * @param dec
     * @param n
     * @return
     */
    public static double[] my_dec2bin(double[] dec, int n) {
        int len = dec.length;
        double[] bitmap = new double[len * n];
        int index = 0;
        for (int i = 0; i < len; i++) {
            int temp = (int) dec[i];
            for (int j = 0; j < n; j++) {
                bitmap[index] =  temp % 2;
                index++;
                temp =  temp / 2;
            }
        }
        return bitmap;
    }
}
