package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Authentication_codes {
    /**
     * 认证图像进行简单的加密
     *
     * @param codes 原认证图像
     * @param key   6个密钥
     * @return 返回0加密后的认证图像, 1二值图像
     */
    public static Bitmap[] authentication_codes(Bitmap codes, double[] key) {
        Bitmap[] resultArray = new Bitmap[2];
        int alpha = 0xFF << 24;
        int num = Matlab.numel(codes);
        double[] sequence = Rand_numbers.Rand_numbers(key, num, 2);
        int[] codesArray = RGB2Grey.bitmap2array(codes);

        int sum = 0;
        for (int i = 0; i < num; i++) {
            sum += codesArray[i];
        }
        sum /= num;

        //TODO 二值化处理
        for (int i = 0; i < num; i++) {
            if (codesArray[i] >= sum) {
                codesArray[i] = 1;
            } else {
                codesArray[i] = 0;
            }
        }

        //TODO 二值图像，保存结果输出
        int[] twoDataBitmap = new int[codesArray.length];
        System.arraycopy(codesArray, 0, twoDataBitmap, 0, codesArray.length);
        for (int i = 0; i < num; i++) {
            if (twoDataBitmap[i] == 1) {
                twoDataBitmap[i] = 255;
            }
        }
        for (int i = 0; i < num; i++) {
            twoDataBitmap[i] = alpha | (twoDataBitmap[i] << 16) | (twoDataBitmap[i] << 8) | twoDataBitmap[i];
        }
        Bitmap resultTwoDataBitmap = Bitmap.createBitmap(codes.getWidth(), codes.getHeight(), Bitmap.Config.ARGB_8888);//int数组转为bitmap
        resultTwoDataBitmap.setPixels(twoDataBitmap, 0, codes.getWidth(), 0, 0, codes.getWidth(), codes.getHeight());
        resultArray[1] = resultTwoDataBitmap;

        //TODO 异或加密
        for (int i = 0; i < num; i++) {
            codesArray[i] ^= (int) sequence[i];
        }

        //TODO 把codesArray转为bitmap
        for (int i = 0; i < num; i++) {
            if (codesArray[i] == 1) {
                codesArray[i] = 255;
            }
        }
        for (int i = 0; i < num; i++) {
            codesArray[i] = alpha | (codesArray[i] << 16) | (codesArray[i] << 8) | codesArray[i];
        }
        Bitmap result = Bitmap.createBitmap(codes.getWidth(), codes.getHeight(), Bitmap.Config.ARGB_8888);//int数组转为bitmap
        result.setPixels(codesArray, 0, codes.getWidth(), 0, 0, codes.getWidth(), codes.getHeight());
        resultArray[0] = result;

        return resultArray;
    }
}
