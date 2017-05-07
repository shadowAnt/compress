package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class Joint_de {
    public static Bitmap joint_de(Bitmap resultBitmap, int m, int n, Bitmap EnAuthenticationBitmap, double[] key){
        int[] EnAuthenticationArray = RGB2Grey.bitmap2array(EnAuthenticationBitmap);
        for(int i=0; i<EnAuthenticationArray.length; i++){
            if(EnAuthenticationArray[i]==255){
                EnAuthenticationArray[i] = 1;
            }
        }
        int height = resultBitmap.getHeight();
        int width = resultBitmap.getWidth();
        int h = height/2*m;
        int w = width/2*n;
        int[][] Ic = Matlab.im2col(resultBitmap, m, n);
        int blockNum = Ic[0].length;
//        int[][] Ic2 = new int[h][w];
//        for(int i=0; i<h; i++)
//            for(int j=0; j<w; j++){
//                Ic2[i][j] = Ic[i][j] + 255;
//            }
//        int[][] Ic3 = Ic2;
        int[][] I_compress = Matlab.im2col(resultBitmap, 2, 2);
        double[] xor_key = Rand_numbers.Rand_numbers(key, blockNum, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(key[0], key[1], blockNum);
        int[] temp_bitmap = new int[m*n];
        for(int j=0; j<blockNum; j++){
            int a = I_compress[0][j];
            int b = I_compress[1][j];
            int[] dec = new int[2];
            dec[0] = I_compress[2][j];
            dec[1] = I_compress[3][j];
//%嵌入认证信息
            int code = Extr.extr(a, b, dec);
            if(code != EnAuthenticationArray[j]){
                for(int k=0; k<m*n; k++){
                    Ic[k][j] = 0;
                }
            }
// %为直接解压缩保留
            int[] bitmap2 = My_dec2bin.my_dec2bin(dec, 8);
            int[] dec2 = dec;
            for(int k=0; k<m*n; k++){
                if(bitmap2[k]==0){
                    temp_bitmap[k] = a;
                }
                if(bitmap2[k]==1){
                    temp_bitmap[k] = b;
                }
            }
//            for(int k=0; k<m*n; k++){
//                Ic3[k][j] = temp_bitmap[k];
//            }
//  %解密
            key[5] = xor_key[j];
            int[] group4 = Decryption.decrption(a, b, dec, key);
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            int[] longBitmap = My_dec2bin.my_dec2bin(dec, 8);
// %用key对bitmap置乱
            longBitmap = My_derand.my_derand(longBitmap, sequence[j], key[1]);
            for(int k=0; k<m*n; k++){
                if(longBitmap[k]==0){
                    temp_bitmap[k] = a;
                }
                if(longBitmap[k]==1){
                    temp_bitmap[k] = b;
                }
            }
            for(int k=0; k<m*n; k++){
                Ic[k][j] = temp_bitmap[k];
            }
        }
        Bitmap I2 = Matlab.col2im(Ic, m, n, h, w);
        return I2;
    }
}
