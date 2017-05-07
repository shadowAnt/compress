package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class My_bin2dec {
    /**
     * function dec=my_bin2dec(bin, n) %n个二进制数bin转化为一个十进制数
         len=numel(bin);
         for i=1:n:len-n+1 %每次处理n个二进制数
         temp=0;
         for j=i:i+n-1
         temp=temp+2^mod(j-1,n)*bin(j);
         end
         dec(ceil(i/n))=temp;
         end
     [0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0]
     8
     * @return
     */
    public static int[] my_bin2dec(int[] bin, int n){
        int len = bin.length;
        int[] dec = new int[2];
        for(int i=0; i<len-n+1; i+=n){
            int temp = 0;
            for(int j=i; j<i+n-1; j++){
                temp += bin[j] * Math.pow(2, (j-1)%n);
            }
            dec[(int)Math.ceil(i/n)] = temp;
        }
        return dec;
    }
}
