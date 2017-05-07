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
//        System.out.println(Arrays.toString(EnAuthenticationArray));
        int[][] Ic = Matlab.im2col(originBitmap, m, n);
//        System.out.println(Ic[0].length);//256*256/16=16384列 块数
//        for(int i=0; i<16; i++)
//            System.out.println(i+"  "+Ic[i][0]);//0-15 0-255
        int blockNum = Ic[0].length;//列数 即是块数
        int[][] I_compress = new int[4][blockNum];//256*256/16=16384列 块数
        double[] xor_key = Rand_numbers.Rand_numbers(key, blockNum, 256);
//        System.out.println(Arrays.toString(xor_key));//102.0, 138.0, 19.0, 195.0, 234.0, 2.0,....
//        System.out.println(xor_key[16383]);//16385
        double[] sequence = Chaotic.chaotic_maping_sequence(key[0], key[1], blockNum);
//        System.out.println(sequence.length + Arrays.toString(sequence));//16384 + 0.78, 0.6160439999999999, 0.84915630632976, 0.45984264676307, 0.89171071926
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
//            System.out.println(Arrays.toString(bitmap));//[0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1]
            int b = highSum / highNum;   //高均值
            int a;
            if(highNum==m*n){
                a = b;
            } else{
                //这里不会出现除以0的情况
                a = (sum - highSum) / (m*n - highNum);  //低均值
            }
//            System.out.println(a + "  "+ b);//149  160
            bitmap = My_rand.my_rand(bitmap, sequence[j], key[1]);
//            System.out.println(Arrays.toString(bitmap));//[0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0]
            int[] dec = new int[2];
            dec = My_bin2dec.my_bin2dec(bitmap, 8);
//            System.out.println(Arrays.toString(dec));//[39, 153]

            double[] keyTemp = key;
            keyTemp[5] = xor_key[j];
            int[] group4 = Encryption.encryption(a, b, bitmap, keyTemp); //异或加密
//            System.out.println(Arrays.toString(group4));//[5, 54, 93, 183]
            a = group4[0];
            b = group4[1];
            bitmap[0] = group4[2];
            bitmap[1] = group4[3];
            group4 = Embed.embed(a, b, bitmap, EnAuthenticationArray[j]);     //嵌入认证信息
//            System.out.println(Arrays.toString(group4));//[101, 31, 4, 169]
            I_compress[0][j] = group4[0];
            I_compress[1][j] = group4[1];
            I_compress[2][j] = group4[2];
            I_compress[3][j] = group4[3];
        }
        int height = originBitmap.getHeight();
        int width = originBitmap.getWidth();
        height = (int)Math.ceil(height / m) * 2;
        width = (int)Math.ceil(width / n) * 2;
//        System.out.println("width  "+width);//256
        Bitmap resultBitmap = Matlab.col2im(I_compress, 2, 2, height, width);
//        System.out.println(Arrays.toString(RGB2Grey.bitmap2array(resultBitmap)));
        return resultBitmap;
    }
}
