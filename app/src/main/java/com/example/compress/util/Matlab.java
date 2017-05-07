package com.example.compress.util;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Matlab {
    public static int numel(Bitmap bitmap) {
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        return ans;
    }

    /**
     *
     * @param bitmap
     * @param m 分块大小
     * @param n
     * @return bitmap的二维矩阵，按照mn分块，第i列是第i块的所有像素值
     */
    public static int[][] im2col(Bitmap bitmap, int m, int n) {
        int[] bitmap1Array = RGB2Grey.bitmap2array(bitmap);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[][] bitmap2Array = new int[height][width];

        //TODO 一维数组变二维数组
        int index=0;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                bitmap2Array[i][j] = bitmap1Array[index++];
            }
        }

        //TODO 二维数组分块处理
        int blockHeight = (int)Math.ceil(height/m);//横着分可以分几块
        int blockWidth = (int)Math.ceil(width/n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int blockPixel = m * n;
        int[][] block2Array = new int[blockPixel][sumBlock];
        for(int j=0; j<sumBlock; j++){
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j * m) % height;
            int col = j / blockHeight * n;
            for(int i=0; i<blockPixel; i++){
                int rowIndex = i % m;
                int colIndex = i / n;
                block2Array[i][j] = bitmap2Array[row + rowIndex][col + colIndex];
//                if(j==0) System.out.println(block2Array[i][j]);
            }
        }
        return block2Array;
    }

    /**
     *
     * @param I_compress 行数为 m*n
     * @param m
     * @param n
     * @param height 复原后普通矩阵的高
     * @param width
     * @return
     */
    public static Bitmap col2im(int[][] I_compress, int m, int n, int height, int width){
        //TODO 二维分块矩阵转为二维普通矩阵
        int blockNum = I_compress[0].length;
        int[][] bitmap2Array = new int[height][width];
        int blockHeight = (int)Math.ceil(height/m);//横着分可以分几块
        int blockWidth = (int)Math.ceil(width/n);//竖着分可以分几块
//        System.out.println("blockWidth  "+ blockWidth);//128
        int blockPixel = m * n;
//        System.out.println(blockNum);//16384

//        for(int j=0; j<blockNum; j++){
//            for(int i=0; i<blockPixel; i++){
//                System.out.print(I_compress[i][j]+" ");
//                if(i==3) System.out.println();
//            }
//        }

        for(int j=0; j<blockNum; j++){
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j * m) % height;
            int col = j / blockHeight * n;

            for(int i=0; i<blockPixel; i++){//4
                int rowIndex = i % m;
                int colIndex = i / n;
                bitmap2Array[row + rowIndex][col + colIndex] = I_compress[i][j];
//                if(bitmap2Array[0][0]!=249)System.out.println(i+" "+j);
            }
        }
//        System.out.println(bitmap2Array[0][0]);
        //TODO 二维普通矩阵转为一维矩阵
        int sumPixel = height * width;
        int[] bitmap1Array = new int[sumPixel];
        int index=0;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                bitmap1Array[index++] = bitmap2Array[i][j];

            }
        }
        //TODO 一维矩阵转为Bitmap
        int alpha = 0xFF << 24;
        for(int i=0; i<sumPixel; i++){
            bitmap1Array[i] = alpha | (bitmap1Array[i] << 16) | (bitmap1Array[i] << 8) | bitmap1Array[i];
        }
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//int数组转为bitmap
        resultBitmap.setPixels(bitmap1Array, 0, width, 0, 0, width, height);

        return resultBitmap;
    }

    public static double min(double[] array){
        double min = array[0];
        for(int i=1; i<array.length; i++){
            if(array[i]<min){
                min = array[i];
            }
        }
        return min;
    }

    public static int find(double[] array, double num){
        int pos = 0;
        for(int i=0; i<array.length; i++){
            if(array[i]==num){
                pos = i;
                break;
            }
        }
        return pos;
    }

}
