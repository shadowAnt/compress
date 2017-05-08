package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/8.
 */

public class Show {
    public static void showBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        //512  512  262144
        int[] pixels = new int[ans]; //通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < 4; i++) {//height
            for (int j = 0; j < 4; j++) {//width
                int temp = RGB2Grey.RGB2Grey(pixels, width, i, j);
                System.out.print(temp+" ");
            }
            System.out.println();
        }
        System.out.println("width: " + width + "  height:  " + height);
    }

    public static void show2array(int[][] array){
        int height = array.length;
        int width = array[0].length;
        for(int i=0; i<height; i++){
//            for(int j=0; j<width; j++){
//                System.out.print(array[i][j] + " ");
//            }
//            System.out.println();
            System.out.print(array[i][0]+" ");//显示一竖列，第一块的内容
        }
        System.out.println();
        System.out.println("width: " + width + "  height:  " + height);
    }

    public static void show1array(int[] array, int height, int width){

    }
}
