package com.example.compress.util;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class Joint_de {
    public static Bitmap joint_de(Bitmap resultBitmap, int oH, int oW, int m, int n, Bitmap EnAuthenticationBitmap, double[] key) {
        int[] EnAuthenticationArray = RGB2Grey.bitmap2array(EnAuthenticationBitmap);
        for (int i = 0; i < EnAuthenticationArray.length; i++) {
            if (EnAuthenticationArray[i] == 255) {
                EnAuthenticationArray[i] = 1;
            }
        }
        int blockHeight = (int) Math.ceil(oH / m);//横着分可以分几块
        int blockWidth = (int) Math.ceil(oW / n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int sumPixel = m * n;
        int[][] Ic = new int[sumPixel][sumBlock];
        for (int i = 0; i < sumPixel; i++) {
            for (int j = 0; j < sumBlock; j++) {
                Ic[i][j] = 0;
            }
        }
//        int[][] Ic2 = new int[h][w];
//        for(int i=0; i<h; i++)
//            for(int j=0; j<w; j++){
//                Ic2[i][j] = Ic[i][j] + 255;
//            }
//        int[][] Ic3 = Ic2;
        int[][] I_compress = Matlab.im2col(resultBitmap, 2, 2);
        System.out.println("把压缩加密矩阵分块 ");
        Show.show2array(I_compress);
        double[] xor_key = Rand_numbers.Rand_numbers(key, sumBlock, 256);
        System.out.println("xor_key = " + xor_key[0] + "  " + xor_key[1] + "  " + xor_key[2]);

        double[] sequence = Chaotic.chaotic_maping_sequence(key[0], key[1], sumBlock);
        System.out.println("sequence " + sequence.length + Arrays.toString(sequence));
        int[] temp_bitmap = new int[m * n];
        for (int j = 0; j < sumBlock; j++) {
            int a = I_compress[0][j];
            int b = I_compress[1][j];
            int[] dec = new int[2];
            dec[0] = I_compress[2][j];
            dec[1] = I_compress[3][j];
            //TODO %认证信息
            int code = Extr.extr(a, b, dec);
            if (j == 0) System.out.println("code 0= " + code);
            if (code != EnAuthenticationArray[j]) {
                for (int k = 0; k < m * n; k++) {
                    Ic[k][j] = 0;
                }
            }
            //TODO 为直接解压缩保留
            int[] bitmap2 = My_dec2bin.my_dec2bin(dec, 8);
            for (int k = 0; k < m * n; k++) {
                if (bitmap2[k] == 0) {
                    temp_bitmap[k] = a;
                }
                if (bitmap2[k] == 1) {
                    temp_bitmap[k] = b;
                }
            }
            if (j == 0) System.out.println("bitmp = " + Arrays.toString(temp_bitmap));
            //  解密
            key[5] = xor_key[j];
            int[] group4 = Decryption.decrption(a, b, dec, key);
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            int[] longBitmap = My_dec2bin.my_dec2bin(dec, 8);
            // 用key对bitmap置乱
            longBitmap = My_derand.my_derand(longBitmap, sequence[j], key[1]);
            for (int k = 0; k < m * n; k++) {
                if (longBitmap[k] == 0) {
                    temp_bitmap[k] = a;
                }
                if (longBitmap[k] == 1) {
                    temp_bitmap[k] = b;
                }
            }
            if (j == 0) System.out.println("解密之后的bitmp = " + Arrays.toString(temp_bitmap));
            for (int k = 0; k < sumPixel; k++) {
                Ic[k][j] = temp_bitmap[k];
            }
        }
        Bitmap I2 = Matlab.col2im(Ic, m, n, oH, oW);
        return I2;
    }
}
