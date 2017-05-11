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
        grey = grey & 0x000000FF;
        return grey;
    }

    public static int[] bitmap2array(Bitmap bitmap){
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        int[] codes = new int[ans+1];
        //512  512  262144
        int[] pixels = new int[ans]; //通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int index = 0;
        for (int i = 0; i < height; i++) {//height
            for (int j = 0; j < width; j++) {//width
                int temp = RGB2Grey.RGB2Grey(pixels, width, i, j);
                codes[index++] = temp;
            }
        }
        return codes;
    }

    public static int[][] im2array(Bitmap bitmap){
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        int[][] codes = new int[height][width];
        //512  512  262144
        int[] pixels = new int[ans]; //通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {//height
            for (int j = 0; j < width; j++) {//width
                int temp = RGB2Grey.RGB2Grey(pixels, width, i, j);
                codes[i][j] = temp;
            }
        }
        return codes;
    }
}
