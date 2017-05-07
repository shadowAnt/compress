package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class RGB2Grey {
    /**
     * rgb按照0-255范围输出
     *
     * @param pixels 图像一维矩阵
     * @param width 宽，一行的列数
     * @return 返回当前的0-255
     */
    public static int RGB2Grey(int[] pixels, int width, int i, int j){
        int grey = pixels[width * i + j];
        int red = ((grey & 0x00FF0000) >> 16);
        int green = ((grey & 0x0000FF00) >> 8);
        int blue = (grey & 0x000000FF);
        grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
        return grey;
    }

    public static int[] bitmap2array(Bitmap bitmap){
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        int[] codes = new int[ans+1];
        bitmap.getPixels(codes, 0, width, 0, 0, width, height);
        //-1
        for(int i=0; i<ans; i++){
            int grey = codes[i];
            int red = ((grey & 0x00FF0000) >> 16);
            int green = ((grey & 0x0000FF00) >> 8);
            int blue = (grey & 0x000000FF);
            grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
            //255 0
            //if(grey==255) grey = 1;
            codes[i] = grey;
        }
        return codes;
    }
}
