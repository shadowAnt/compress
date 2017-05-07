package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Rand_numbers {
    /**
     * 伪随机数生成器，生成一行n列个随机数，只要三个参数给定，结果就一定
     *
     * @param key 6个数
     * @param n 要生成的个数
     * @param range 数的范围
     * @return 一维数组
     */
    public static double[] Rand_numbers(double[] key, int n, int range){
        double[] sequence = new double[n+1];
        for(int i=0; i<n; i++){
            sequence[i] = 0;
        }
        sequence[0] = key[5];

        for(int i=1; i<n; i++){
            sequence[i] = (key[2]*sequence[i-1] + key[3]) % key[4];
        }
        for(int i=0; i<n; i++){
            sequence[i] %= range;
        }
        return sequence;
//        sequence(1)=key(6);
//        for i=2:n
//        sequence(i)=mod(key(3)*sequence(i-1)+key(4), key(5));
//        % 获取随机数
//                end
//        sequence=mod(sequence,range);
    }
}
