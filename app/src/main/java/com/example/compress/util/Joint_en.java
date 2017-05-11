package com.example.compress.util;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Joint_en {
    /**
     * @param originBitmap 载体图像
     * @param m 分块大小
     * @param n 分块大小
     * @param EnAuthenticationBitmap 认证信息
     * @param key 密钥6
     * @return 发送压缩加密认证后的图像
     */
    public static Bitmap joint_en(Bitmap originBitmap, int m, int n, Bitmap EnAuthenticationBitmap, double[] key) {
        //TODO Bitmap EnAuthenticationBitmap 认证信息变为一维数组
        int[] EnAuthenticationArray = RGB2Grey.bitmap2array(EnAuthenticationBitmap);
        for(int i=0; i<EnAuthenticationArray.length; i++){
            if(EnAuthenticationArray[i]==255){
                EnAuthenticationArray[i] = 1;
            }
        }
        //TODO 输出读入进来的加密认证信息
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.print(EnAuthenticationArray[i*EnAuthenticationBitmap.getWidth() + j]+ " ");
            }
            System.out.println();
        }
        System.out.println();
        //TODO 对载体图像进行处理
        Show.showBitmap(originBitmap);
        int[][] Ic = Matlab.im2col(originBitmap, m, n);
        Show.show2array(Ic);
        int blockNum = Ic[0].length;//列数 即是块数
        int[][] I_compress = new int[4][blockNum];//256*256/16=16384列 块数
        double[] xor_key = Rand_numbers.Rand_numbers(key, blockNum, 256);
        System.out.println("xor_key = "+ xor_key[0]+"  "+ xor_key[1]+"  "+ xor_key[2]);
        double[] sequence = Chaotic.chaotic_maping_sequence(key[0], key[1], blockNum);
        System.out.println("sequence " + sequence.length + Arrays.toString(sequence));//16384 + 0.78, 0.6160439999999999, 0.84915630632976, 0.45984264676307, 0.89171071926
        int[] bitmap = new int[m * n];
        for(int j=0; j<blockNum; j++){
            //TODO 对每块进行处理 AMBTC
            int sum = 0;
            for(int i=0; i<m * n; i++){
                sum += Ic[i][j];
            }
            int ave = sum/(m*n);//均值
            int highSum = 0;
            int highNum = 0;
            for(int i=0; i<m * n; i++){
                if(Ic[i][j]>=ave){
                    highNum++;
                    highSum += Ic[i][j];
                    bitmap[i] = 1;
                } else {
                    bitmap[i] = 0;
                }
            }
            if(j==0)
                System.out.println("位图 " + Arrays.toString(bitmap));
            int b = highSum / highNum;   //高均值
            int a;
            if(highNum==m*n){
                a = b;
            } else{
                //这里不会出现除以0的情况
                a = (sum - highSum) / (m*n - highNum);  //低均值
            }
            if(j==0)
            System.out.println("低均值 高均值"+a + "  "+ b);
            bitmap = My_rand.my_rand(bitmap, sequence[j], key[1], j);
            if(j==0)
                System.out.println("置乱后的位图 " + Arrays.toString(bitmap));
            int[] dec = My_bin2dec.my_bin2dec(bitmap, 8);
            if(j==0)
                System.out.println("转化后的两个十进制   "+Arrays.toString(dec));
            double[] keyTemp = key;
            keyTemp[5] = xor_key[j];
            int[] group4 = Encryption.encryption(a, b, dec, keyTemp); //异或加密
            if(j==0)
                System.out.println("四元组  " + Arrays.toString(group4));
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            group4 = Embed.embed(a, b, dec, EnAuthenticationArray[j]);     //嵌入认证信息
            if(j==0||j==1)
                System.out.println("嵌入认证信息之后的四元组  " + Arrays.toString(group4));
            I_compress[0][j] = group4[0];
            I_compress[1][j] = group4[1];
            I_compress[2][j] = group4[2];
            I_compress[3][j] = group4[3];
        }
        int height = originBitmap.getHeight();
        int width = originBitmap.getWidth();
        height = (int)Math.ceil(height / m) * 2;
        width = (int)Math.ceil(width / n) * 2;
        Bitmap resultBitmap = Matlab.col2im(I_compress, 2, 2, height, width);
        System.out.println("变为正常二维矩阵 2*2为一块");
        Show.showBitmap(resultBitmap);
        return resultBitmap;
    }
}
