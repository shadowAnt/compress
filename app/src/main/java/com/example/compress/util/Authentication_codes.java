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
     * @param key 6个密钥
     * @return 返回加密后的认证图像
     *
     * ```matlab
     * function codes=authentication_codes(codes, key)
            %codes为传入的待认证的图像
            sequence=rand_numbers(key, numel(codes), 2);
            %numel返回图像像素个数
            for i=1:numel(codes)
            codes(i)=bitxor(codes(i),sequence(i));
            % 图像中的每个像素和0 1异或
    end
     */
    public static Bitmap authentication_codes(Bitmap codes, double[] key){
        int alpha = 0xFF << 24;
        int num = Matlab.numel(codes);
        double[] sequence = Rand_numbers.Rand_numbers(key, num, 2);
        int[] codesArray = RGB2Grey.bitmap2array(codes);
//        System.out.println("codesArray"+ Arrays.toString(codesArray));

        //TODO 二值化处理
        for(int i=0; i<num; i++){
            if(codesArray[i]>=128){
                codesArray[i] = 1;
            } else {
                codesArray[i] = 0;
            }
        }
        for(int i=0; i<num; i++){
            codesArray[i] ^= (int)sequence[i];
        }
//        System.out.println("codesArray after ^"+ Arrays.toString(codesArray));//0 1

        //TODO 把codesArray转为bitmap
        for(int i=0; i<num; i++){
            if(codesArray[i]==1){
                codesArray[i] = 255;
            }
        }
        for(int i=0; i<num; i++){
            codesArray[i] = alpha | (codesArray[i] << 16) | (codesArray[i] << 8) | codesArray[i];
        }
        Bitmap result = Bitmap.createBitmap(codes.getWidth(), codes.getHeight(), Bitmap.Config.ARGB_8888);//int数组转为bitmap
        result.setPixels(codesArray, 0, codes.getWidth(), 0, 0, codes.getWidth(), codes.getHeight());
//        int[] resultArray = RGB2Grey.bitmap2array(result);
//        System.out.println("resultArray"+ Arrays.toString(resultArray));
        return result;
    }
}
