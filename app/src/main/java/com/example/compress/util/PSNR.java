package com.example.compress.util;

import android.graphics.Bitmap;

import static java.lang.Math.log10;

/**
 * Created by ShadowAnt on 2017/5/9.
 */

public class PSNR {
    /**
     * function psnr=PSNR(I1,I2)
     * I1=double(I1);
     * I2=double(I2);
     * E=I1-I2;
     * MSE=mean2(E.*E);
     * if MSE==0
     * psnr=-1;
     * else
     * psnr=10*log10(255*255/MSE);
     * end
     *
     * @param I1
     * @param I2
     * @return
     */
    public static double psnr(Bitmap I1, Bitmap I2) {
        int[][] i1 = RGB2Grey.im2array(I1);
        int[][] i2 = RGB2Grey.im2array(I2);
        int height = I1.getHeight();
        int width = I2.getWidth();
        int[][] e = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                e[i][j] = i1[i][j] - i2[i][j];
            }
        }
        double MSE = Matlab.mean(Matlab.multip2(e));
        double ans;
        if (MSE == 0) ans = -1;
        else ans = 10 * log10(255 * 255 / MSE);
        return ans;
    }
}
